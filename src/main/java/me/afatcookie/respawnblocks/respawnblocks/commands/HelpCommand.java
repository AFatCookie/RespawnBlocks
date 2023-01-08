package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.listeners.CommandListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "send the command sender a list of all commands!";
    }

    @Override
    public String getSyntax() {
        return "/rb help";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
            commandSender.sendMessage(ChatColor.GOLD + "+-------------------+");
            for (CommandBuilder command : CommandListener.getCommands()) {
                commandSender.sendMessage(command.getSyntax() + "-" + command.getDescription());
            }
            commandSender.sendMessage(ChatColor.GOLD + "+-------------------+");
        }
    }
}
