package me.picro.myauth.commands;

import me.picro.myauth.managers.PlayerAuthManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoginCommand implements CommandExecutor {

    private final PlayerAuthManager authManager;

    public LoginCommand(PlayerAuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (args != null) {
                if (args.length > 0) {
                    String code = authManager.comparePassword(p, args[0]);

                    switch (code) {
                        case "success":
                            authManager.authenticated(p);
                            p.sendMessage(ChatColor.GREEN + "[i] Вы успешно вошли на сервер. Приятной игры " + ChatColor.RED + "<3" + ChatColor.GREEN + "!");
                            break;
                        case "not right":
                            p.sendMessage(ChatColor.RED + "[i] Неверный пароль!");
                            break;
                        case "logged":
                            p.sendMessage(ChatColor.YELLOW + "[i] Вы уже вошли");
                            break;
                        case "error":
                            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                            kickPlayerError("Login error. Admins, check logs. Timestamp: " + timeStamp, p);
                            break;
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Где аргументы, " + p.getName() + "?" + " Usage: /login <password>");
                }
            }
        }

        return false;
    }

    // private methods
    // kick player if an error occurred
    private void kickPlayerError(String error, Player player) {
        player.kickPlayer(
                ChatColor.RED + "Ошибка: " + ChatColor.GRAY + error +
                        ChatColor.AQUA + "\n\nОбратиться за помощью можно в тг группе GCIssues"
        );
    }

}
