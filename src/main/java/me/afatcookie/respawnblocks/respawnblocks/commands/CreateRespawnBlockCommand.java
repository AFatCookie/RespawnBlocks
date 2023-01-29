package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Creates a respawnableBlock, when looking at it within 5 blocks
 */
public class CreateRespawnBlockCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates the block you are directly looking at, into a respawn block!";
    }

    @Override
    public String getSyntax() {
        return "/rb create";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        // Check if the command sender is a player
        if (!(commandSender instanceof Player)) {
            return;
        }
        Player player = (Player) commandSender;
        // Check if any arguments were passed to the command
        if (args.length > 0) {
            // Get the targeted block within a range of 5 blocks, ignoring fluid collision
            Block targetBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
            // check if the block is not null
            if (targetBlock == null) {
                player.sendMessage(ChatColor.RED + "Failed to create a respawn block.");
                return;
            }
            // check if the block is already a respawn block
            if (instance.getRBManager().getRespawnBlock(targetBlock) != null) {
                player.sendMessage(ChatColor.RED + "This block is already a respawn block!");
                return;
            }
            // check if the respawn block section is missing from the config
            if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSectionName()) == null) {
                player.sendMessage(ChatColor.RED + "please run the command /rb clearall to get back the section. Note: this will clear configuration for all blocks!");
                player.sendMessage(ChatColor.RED + "or, try and create a configuration section named: " + " respawnblocks: " + " in the data config.");
                player.sendMessage(ChatColor.RED + "Or contact AFatCookie#4011 on Discord for futher assistance");
                return;
            }
            // check if the block type is not already in the config
            instance.getDataConfig().addCDInfoToConfig(targetBlock.getType());
            //create new respawn block
            RespawnBlock respawnBlock =
                    new RespawnBlock(
                            targetBlock.getLocation(),
                            instance,
                            targetBlock.getType().toString(),
                            targetBlock.getWorld().getName());
            instance.getRBManager().getRespawnBlocksList().add(respawnBlock);
            player.sendMessage(ChatColor.GREEN + "Successfully created a respawn block!");
        }
    }

}
