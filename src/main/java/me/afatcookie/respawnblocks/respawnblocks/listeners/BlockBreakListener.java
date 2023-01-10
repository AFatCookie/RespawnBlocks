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
    Player player = e.getPlayer();
    Block block = e.getBlock();
    RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(block);
    if (respawnBlock == null) return;

    switch (player.getGameMode()) {
      case SURVIVAL:
        e.setCancelled(true);
        if (respawnBlock.getCooldownTime() <= 0) {
          dropAndDamageItem(player, respawnBlock, e);
          return;
        }
        if (!instance.getTm().isInCoolDown(respawnBlock)) {
          dropAndDamageItem(player, respawnBlock, e);
          block.setType(respawnBlock.getCooldownMaterial());
          new Timer(block, respawnBlock, instance).runTaskTimer(instance, 0, 20L);
          instance.getTm().getCoolDownList().add(respawnBlock);
          return;
        }
        break;
      case CREATIVE:
        if (player.hasPermission("respawnblocks.admin")) {
          instance.getRBManager().getRespawnBlocksList().remove(respawnBlock);
          player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 10);
          player.sendMessage(ChatColor.GOLD + "You have broken a respawn block.");
        }
        break;
    }
  }

    private void dropItems(RespawnBlock respawnBlock, Player player){
      if (!respawnBlock.getRewards().isEmpty() && respawnBlock.getRewards() != null){
        respawnBlock.dropRewards(player);
      }
    }

    private void dropAndDamageItem (Player player, RespawnBlock block, BlockBreakEvent e){
      e.setDropItems(false);
      dropItems(block, player);
      player.getInventory().setItemInMainHand(new ItemCreator(player.getInventory().getItemInMainHand()).setDurability(1).getItemStack());
    }
  }
