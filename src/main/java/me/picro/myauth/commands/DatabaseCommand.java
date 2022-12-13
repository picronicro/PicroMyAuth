package me.picro.myauth.commands;

import me.picro.myauth.Database;
import me.picro.myauth.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class DatabaseCommand implements CommandExecutor {

    private final Main main;
    private final Database database;

    public DatabaseCommand(Main main, Database database) {
        this.main = main;
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("status")) {
                String status = "Is connected: " + database.isConnected();

                if (sender instanceof Player) {
                    ((Player) sender).getPlayer().sendMessage("[i] " + status);
                } else if (sender instanceof ConsoleCommandSender) {
                    Bukkit.getLogger().info("[i] " + status);
                }
            }

            if (args[0].equalsIgnoreCase("reconnect")) {
                database.disconnect();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            database.connect();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.runTaskLater(main, 60L);
            }
        }

        return false;
    }

}
