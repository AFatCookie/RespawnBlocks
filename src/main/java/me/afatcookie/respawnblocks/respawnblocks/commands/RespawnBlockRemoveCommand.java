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
    if (!(commandSender instanceof Player)) return;
    Player player = (Player) commandSender;
    if (args.length > 0) {
      Block targetBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
      RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(targetBlock);
      if (respawnBlock == null){
          player.sendMessage(ChatColor.RED + "Block is null, or not currently a respawnable block.");
          return;
      }
         if (instance.getTm().isInCoolDown(respawnBlock) && targetBlock != null){
           targetBlock.setType(respawnBlock.getInitialBlockType());
         }
      instance.getRBManager().getRespawnBlocksList().remove(instance.getRBManager().getRespawnBlock(targetBlock));
      if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString()) != null){
        instance.getDataConfig().getConfig().set(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString(), null);
        instance.getDataConfig().save();
      }
           player.sendMessage(ChatColor.GREEN + "Successfully removed from respawnable blocks!");

      }
    }
  }
