package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.guis.BlockManagementGUI;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckBlockCommand extends CommandBuilder {
    @Override
    public String getName() {
        return "checkblock";
    }

    @Override
    public String getDescription() {
        return "/rb checkblock";
    }

    @Override
    public String getSyntax() {
        return "if the block being looked at is a respawnBlock, then it'll display the menu of the block.";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Check if the command sender is a player
        if (!(commandSender instanceof Player)) {
            // If not, return and do not execute further code
            return;
        }
        Player player = (Player) commandSender;

        // Check if any arguments were passed to the command
        if (args.length > 0) {
            // Get the targeted block within a range of 10 blocks, ignoring fluid collision
            Block targetBlock = player.getTargetBlockExact(10, FluidCollisionMode.ALWAYS);

            // Check if the targeted block is not a valid respawn block
            if (targetBlock == null || instance.getRBManager().getRespawnBlock(targetBlock) == null || targetBlock.getType() == Material.AIR) {
                // If not, send a message to the player and return
                player.sendMessage(ChatColor.RED + "This is not a respawnBlock.");
                return;
            }

            // Get the respawn block associated with the targeted block
            RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(targetBlock);
            // Create a new inventory using the BlockManagementGUI class and open it for the player
            player.openInventory(new BlockManagementGUI(
                    new ItemCreator(respawnBlock.getInitialBlockType(), 1)
                            .addGlow(true)
                            .setPDCInteger(new NamespacedKey(instance, "xcoord"), respawnBlock.getX())
                            .setPDCInteger(new NamespacedKey(instance, "ycoord"), respawnBlock.getY())
                            .setPDCInteger(new NamespacedKey(instance, "zcoord"), respawnBlock.getZ())
                            .getItemStack(),
                    instance,
                    player).getInventory());
        }
    }

}
