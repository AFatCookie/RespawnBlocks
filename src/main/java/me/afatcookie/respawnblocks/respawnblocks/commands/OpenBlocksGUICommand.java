package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.guis.blocksDisplayGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Opens the GUI which displays all active RespawnableBlocks, also allowing them to tp to it.
 */
public class OpenBlocksGUICommand extends CommandBuilder{
    @Override
    public String getName() {
        return "blocks";
    }

    @Override
    public String getDescription() {
        return "Opens a menu, displaying all RB blocks, and allowing you to teleport to them.";
    }

    @Override
    public String getSyntax() {
        return "/rb blocks";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
            player.openInventory(new blocksDisplayGUI(instance).getInventory());
        }
    }
}
