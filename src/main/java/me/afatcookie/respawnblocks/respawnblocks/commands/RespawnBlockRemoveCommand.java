package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
Command that will stop the block from respawning continuously.
 */
public class RespawnBlockRemoveCommand extends CommandBuilder {
  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String getDescription() {
    return "stops the block from respawning";
  }

  @Override
  public String getSyntax() {
    return "/rb remove";
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
      // Get the respawn block associated with the targeted block
      RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(targetBlock);
      // check if the block is not a valid respawn block
      if (respawnBlock == null) {
        player.sendMessage(ChatColor.RED + "Block is null, or not currently a respawnable block.");
        return;
      }
      // check if the block is in cool down
      if (instance.getTm().isInCoolDown(respawnBlock) && targetBlock != null) {
        targetBlock.setType(respawnBlock.getInitialBlockType());
      }
      // remove the block from the respawn block list
      instance.getRBManager().getRespawnBlocksList().remove(instance.getRBManager().getRespawnBlock(targetBlock));
      // check if the block type is in the config and remove it
      if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString()) != null) {
        instance.getDataConfig().getConfig().set(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString(), null);
        instance.getDataConfig().save();
      }
      player.sendMessage("&aSuccessfully removed RespawnBlock!");
    }
  }
}