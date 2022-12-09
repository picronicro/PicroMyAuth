package me.picro.myauth;

import me.picro.myauth.commands.AddPlayerCommand;
import me.picro.myauth.enums.PlayerData;
import me.picro.myauth.managers.PlayerAuthManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {

    private PluginDescriptionFile pdf = getDescription();
    private Database database;

    private boolean canPlayerJoin = false;
    private String kickMessage = "preparing the server...";

    private PlayerAuthManager authManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("=========================================");
        Bukkit.getLogger().info("  Loading MyAuth, by picroPancer");
        Bukkit.getLogger().info("  Version: " + pdf.getVersion());
        Bukkit.getLogger().info("=========================================");

        // config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // database
        FileConfiguration c = getConfig();
        database = new Database(
                c.getString("host"),
                c.getInt("port"),
                c.getString("database"),
                c.getString("username"),
                c.getString("password")
        );

        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bukkit.getLogger().info("[i] Connected to the database: " + database.isConnected());
        if (database.isConnected()) {
            canPlayerJoin = true;
        } else {
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
            kickMessage = "MySQL error. Admin, check logs. Timestamp: " + timeStamp;
            canPlayerJoin = false;
        }

        // init authManager
        authManager = new PlayerAuthManager(this, database);

        // events
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(authManager, this);

        // commands
        getCommand("smpadd").setExecutor(new AddPlayerCommand(database));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // database
        database.disconnect();
    }

    // events

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!canPlayerJoin) {
            e.getPlayer().kickPlayer(
                    ChatColor.RED + "Ошибка: " + ChatColor.GRAY + kickMessage +
                    ChatColor.AQUA + "\n\nОбратиться за помощью можно в тг группе GCIssues"
            );
        } else {
            Player p = e.getPlayer();
            // check whitelist and prompt password
            PlayerData data = getPlayerFromDB(p.getName());
            if (data == null) {
                p.kickPlayer(
                        ChatColor.RED + "Ошибка: " + ChatColor.GRAY + "Вас нет в белом списке!" +
                        ChatColor.AQUA + "\n\nПодайте заявку на вступление в " + ChatColor.RED + "Geek" + ChatColor.BLUE + ChatColor.BOLD + "Craft" + ChatColor.RESET + " SMP"
                );
            } else {
                p.sendMessage("id: " + data.getId() + "\nnick: " + data.getNickname() + "\npassword: " + data.getPassword()); // debug

                authManager.authenticating(p, data);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("login_msg"))));
                p.sendMessage(ChatColor.AQUA + "[i] Перед тем, как начать игру, войдите в сессию:" + ChatColor.RED + "\n/login ваш_пароль");

                p.sendTitle(ChatColor.RED + "Войдите в сессию", "Используя /login ваш_пароль", 0, 100, 20);

                // переместить логику логина, arraylist и ивенты в отдельный класс (commands)
            }
        }
    }

    // private methods
    private PlayerData getPlayerFromDB(String nickname) {
        try {
            PreparedStatement ps = database.getConnection().prepareStatement("SELECT * FROM players WHERE NICK = ?;");

            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            rs.next();
            PlayerData data = new PlayerData(
                    rs.getString("ID"),
                    rs.getString("NICK"),
                    rs.getString("PASSWORD")
            );

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
