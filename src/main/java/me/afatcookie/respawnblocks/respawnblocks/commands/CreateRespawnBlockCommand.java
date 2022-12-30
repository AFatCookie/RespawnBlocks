package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
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
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
          Block targetBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
          if (targetBlock == null){
              player.sendMessage(ChatColor.RED + "Failed to create a respawn block.");
              return;
          }
          if (instance.getRBManager().getRespawnBlock(targetBlock) != null){
              player.sendMessage(ChatColor.RED + "This block is already a respawn block!");
              return;
          }
      RespawnBlock respawnBlock =
          new RespawnBlock(
              targetBlock.getX(),
              targetBlock.getY(),
              targetBlock.getZ(),
              instance, targetBlock.getType().toString(),
              targetBlock.getWorld().getName());
          instance.getRBManager().getRespawnBlocksList().add(respawnBlock);
          player.sendMessage(ChatColor.GREEN + "Successfully created a respawn block!");
        }
    }
}
