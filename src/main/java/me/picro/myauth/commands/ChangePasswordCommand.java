package me.picro.myauth.commands;

import me.picro.myauth.Database;
import me.picro.myauth.managers.PlayerAuthManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChangePasswordCommand implements CommandExecutor {

    private final PlayerAuthManager authManager;
    private final Database database;

    public ChangePasswordCommand(PlayerAuthManager authManager, Database database) {
        this.authManager = authManager;
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (!authManager.isAuthenticated(p)) {
                if (args != null) {
                    if (args.length > 0) {
                        String password = args[0];
                        if (password.length() >= 6 && password.length() <= 36) {
                            // mysql
                            try {
                                PreparedStatement ps = database.getConnection().prepareStatement("UPDATE players SET PASSWORD = ? WHERE NICK = ?;");
                                ps.setString(1, password);
                                ps.setString(2, p.getName());
                                ps.executeUpdate();

                                p.sendMessage(ChatColor.GREEN + "[i] Password changed successfully");
                            } catch (SQLException e) {
                                e.printStackTrace();
                                String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                                p.sendMessage(ChatColor.RED + "[i] MySql error." + " Timestamp: " + timeStamp);
                            }

                        } else {
                            p.sendMessage(ChatColor.RED + "[i] Ваш пароль больше 36 или меньше 6 символов");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Где аргументы, " + p.getName() + "?" + " Usage: /changepwd <password>");
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "[i] Сначала войдите в сессию!");
            }
        }

        return false;
    }

}
