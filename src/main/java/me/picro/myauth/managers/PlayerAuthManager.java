package me.picro.myauth.managers;

import me.picro.myauth.Database;
import me.picro.myauth.Main;
import me.picro.myauth.commands.ChangePasswordCommand;
import me.picro.myauth.commands.LoginCommand;
import me.picro.myauth.enums.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerAuthManager implements Listener {

    private final Main main;
    private final Database database;

    // commands
    private LoginCommand loginCommand;
    private ChangePasswordCommand changePasswordCommand;

    // auth
    // login
    private ArrayList<String> notAuthPlayer = new ArrayList<>();
    // player data
    private HashMap<Player, PlayerData> playerData = new HashMap<>();

    public PlayerAuthManager(Main main, Database database) {
        this.main = main;
        this.database = database;

        initCommands();
    }

    // private methods
    // init commands
    private void initCommands() {
        loginCommand = new LoginCommand(this);
        main.getCommand("login").setExecutor(loginCommand);

        changePasswordCommand = new ChangePasswordCommand(this, database);
        main.getCommand("changepwd").setExecutor(changePasswordCommand);
    }

    // player auth array
    public void authenticating(Player player, PlayerData data) {
        playerData.put(player, data);
        notAuthPlayer.add(player.getName());
    }

    public void authenticated(Player player) {
        notAuthPlayer.remove(player.getName());
    }

    // is authenticated
    public boolean isAuthenticated(Player player) {
        return notAuthPlayer.contains(player.getName());
    }

    // compare password from /login command
    public String comparePassword(Player player, String password) {
        if (player != null) {
            if (notAuthPlayer.contains(player.getName())) {
                if (playerData.get(player).getPassword().equals(password)) {
                    authenticated(player);
                    return "success"; // correct password
                } else {
                    return "not right"; // incorrect password
                }
            } else {
                return "logged"; // already logged in
            }
        }

        return "error"; // something went wrong
    }

    // events
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (notAuthPlayer.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (notAuthPlayer.contains(((Player) e.getEntity()).getPlayer().getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {
        if (notAuthPlayer.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    // remove player data on quit
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData data = playerData.get(p);
        if (data != null) {
            playerData.remove(p);
        }
        notAuthPlayer.remove(p.getName());
    }

}
