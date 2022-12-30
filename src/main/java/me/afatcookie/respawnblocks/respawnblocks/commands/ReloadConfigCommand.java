package me.afatcookie.respawnblocks.respawnblocks.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Reloads the Configuration files for RespawnableBlocks.
 */
public class ReloadConfigCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reloads the config files for RespawnableBlocks plugin";
    }

    @Override
    public String getSyntax() {
        return "/rb reload";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length > 0) {
            try{
            instance.getDataConfig().reload();
            commandSender.sendMessage(ChatColor.GREEN + "Successfully reloaded RespawnableBlocks!");
            }catch (IllegalArgumentException | NullPointerException exception) {
                commandSender.sendMessage("Failed to reload RespawnableBlocks!");
                commandSender.sendMessage("Exception is in console.");
                exception.printStackTrace();
            }
        }
    }
}
