package me.picro.myauth.commands;

import me.picro.myauth.Database;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class AddPlayerCommand implements CommandExecutor {

    private Database database;

    public AddPlayerCommand(Database database) {
        this.database = database;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args != null) {
                try {
                    PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO players (ID,NICK,PASSWORD) VALUES (?,?,?);");
                    String uuid = UUID.randomUUID().toString();
                    String password = StringUtils.left(uuid, 8);

                    ps.setString(1, uuid);
                    ps.setString(2, args[0]);
                    ps.setString(3, password);
                    ps.executeUpdate();

                    Bukkit.getLogger().info(
                            "\n[i] ID: " + uuid +
                                    "\n[i] Nickname: " + args[0] +
                                    "\n[i] Password: " + password
                    );
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

}
