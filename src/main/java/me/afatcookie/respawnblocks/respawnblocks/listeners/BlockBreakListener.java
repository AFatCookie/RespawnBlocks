package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.timing.Timer;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/*
Controls what happens when a RespawnBlock is broken
 */
public class BlockBreakListener implements Listener {
  private final RespawnBlocks instance;

  public BlockBreakListener(RespawnBlocks respawnBlocks) {
    this.instance = respawnBlocks;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    Player player = e.getPlayer(); // get the player who broke the block
    Block block = e.getBlock(); // get the block that was broken
    RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(block); // get the respawn block associated with the broken block
    // check if the block is not a valid respawn block
    if (respawnBlock == null) {
      return;
    }
    // check the player's game mode
    switch (player.getGameMode()) {
      case SURVIVAL:
        e.setCancelled(true); // cancel the event
        if (respawnBlock.getCooldownTime() <= 0) {
          // if the cooldown time is less than or equal to 0, drop and damage the item
          dropAndDamageItem(player, respawnBlock, e);
          return;
        }
        if (!instance.getTm().isInCoolDown(respawnBlock)) {
          // if the block is not in cool down, drop and damage the item
          dropAndDamageItem(player, respawnBlock, e);
          block.setType(respawnBlock.getCooldownMaterial()); // change the block's material
          new Timer(block, respawnBlock, instance).runTaskTimer(instance, 0, 20L); // start the cool down timer
          instance.getTm().getCoolDownList().add(respawnBlock); // add the block to the cool down list
          return;
        }
        break;
      case CREATIVE:
        if (player.hasPermission("respawnblocks.admin")) {
          // remove the block from the respawn block list
          instance.getRBManager().getRespawnBlocksList().remove(respawnBlock);
          player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 10); // play sound effect
          player.sendMessage(ChatColor.GOLD + "You have broken a respawn block.");
        }
        break;
    }
  }


    private void dropItems(RespawnBlock respawnBlock, Player player){
      if (!respawnBlock.getRewards().isEmpty() && respawnBlock.getRewards() != null){
        respawnBlock.dropRewards(respawnBlock, player);
      }
    }

    private void dropAndDamageItem (Player player, RespawnBlock block, BlockBreakEvent e){
      e.setDropItems(false);
      dropItems(block, player);
      player.getInventory().setItemInMainHand(new ItemCreator(player.getInventory().getItemInMainHand()).setDurability(1).getItemStack());
    }
  }
