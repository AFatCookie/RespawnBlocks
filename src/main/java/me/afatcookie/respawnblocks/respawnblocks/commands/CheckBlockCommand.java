package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.guis.BlockManagementGUI;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
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
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
            Block targetBlock = player.getTargetBlockExact(10, FluidCollisionMode.ALWAYS);
            if (targetBlock == null || instance.getRBManager().getRespawnBlock(targetBlock) == null || targetBlock.getType() == Material.AIR) {
                player.sendMessage("This is not a respawnBlock.");
                return;
            }
            RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(targetBlock);
            player.openInventory(new BlockManagementGUI(new ItemCreator(respawnBlock.getInitialBlockType(), 1)
                    .addGlow(true)
                    .setPDCInteger(new NamespacedKey(instance, "xcoord"), respawnBlock.getX())
                    .setPDCInteger(new NamespacedKey(instance, "ycoord"), respawnBlock.getY())
                    .setPDCInteger(new NamespacedKey(instance, "zcoord"), respawnBlock.getZ())
                    .getItemStack(), instance, player).getInventory());
        }
    }
}
