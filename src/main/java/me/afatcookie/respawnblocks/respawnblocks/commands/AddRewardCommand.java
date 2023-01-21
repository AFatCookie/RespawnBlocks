package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddRewardCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "addReward";
    }

    @Override
    public String getDescription() {
        return "Add the item in your main hand to the block rewards.";
    }

    @Override
    public String getSyntax() {
        return "/rb addReward <blockID> <weight>";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length == 3){
            try {
                int blockID = Integer.parseInt(args[1]);
                int weight = Integer.parseInt(args[2]);
                for(RespawnBlock respawnBlock : instance.getRBManager().getRespawnBlocksList()){
                    if (respawnBlock.getBlockID() == blockID) {
                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                        if (heldItem.getType() != Material.AIR) {
                            respawnBlock.addToRewards(heldItem, weight);
                            player.sendMessage(ChatColor.GREEN + "Added held item to block reward!");
                            break;
                        }
                    }
                }
            }catch (NumberFormatException e){
                commandSender.sendMessage("Failed: A number is invalid!");
            }
        }
    }
}
