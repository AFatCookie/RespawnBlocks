package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.timing.Timer;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

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
    onBlockBreak2(e);
    /*
    Player player = e.getPlayer();
    Block block = e.getBlock();
    RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(block);
    if (respawnBlock != null) {
      if (player.getGameMode() == GameMode.SURVIVAL) {
        // Makes sure player is in survival, just to eliminate any accidental breakage of the block.
        e.setCancelled(true);
        if (respawnBlock.getCooldownTime() <= 0) {
          e.setDropItems(false);
          tryDropItems(block.getDrops(player.getInventory().getItemInMainHand()), player, block.getLocation());
          player.getInventory().setItemInMainHand(new ItemCreator(player.getInventory().getItemInMainHand()).setDurability(1).getItemStack());
          return;
        }
        if (!instance.getTm().isInCoolDown(respawnBlock)) {
          e.setDropItems(false);
          tryDropItems(
                  block.getDrops(player.getInventory().getItemInMainHand()), player, block.getLocation());
          block.setType(respawnBlock.getCooldownMaterial());
          new Timer(block, respawnBlock, instance).runTaskTimer(instance, 0, 20L);
          instance.getTm().getCoolDownList().add(respawnBlock);
          player
                  .getInventory()
                  .setItemInMainHand(
                          new ItemCreator(player.getInventory().getItemInMainHand())
                                  .setDurability(1)
                                  .getItemStack());
          // Above, it cancels the drops, makes sure block is not in cooldown, and will drop the item
          // and put it in cooldown. If a respawnBlock
          // is attempted to be broken while "cooling down", it won't do anything, the timer must
          // finish which started when it wasn't in cooldown.
          return;
        }
        return;
      }
    }
    // If player is in Creative, and has the correct permissions, it will allow them to break the
    // RespawnBlock from existence.
    if (player.hasPermission("respawnblocks.admin") && player.getGameMode() == GameMode.CREATIVE) {
      instance.getRBManager().getRespawnBlocksList().remove(respawnBlock);
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 10);
      player.sendMessage(ChatColor.GOLD + "You have broken a respawn block.");
    }
  }
    */
  }
    /**
     * Drops the list of items, to the specified location
     *
     * @param items    Collection of items to drop
     * @param player   the player to get the world of
     * @param location the location at which the items will be dropped at.
     */
    private void tryDropItems(Collection < ItemStack > items, Player player, Location location){
      if (items != null) {
        if (!items.isEmpty()) {
          for (ItemStack item : items) {
            player.getWorld().dropItemNaturally(location, item);
          }
        }
      }
    }

    private void onBlockBreak2 (BlockBreakEvent e){
      Player player = e.getPlayer();
      Block block = e.getBlock();
      RespawnBlock respawnBlock = instance.getRBManager().getRespawnBlock(block);
      if (respawnBlock == null) return;

      switch (player.getGameMode()) {
        case SURVIVAL:
          e.setCancelled(true);
          if (respawnBlock.getCooldownTime() <= 0) {
            dropAndDamageItem(player, block, e);
            return;
          }
          if (!instance.getTm().isInCoolDown(respawnBlock)) {
            dropAndDamageItem(player, block, e);
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

    private void dropAndDamageItem (Player player, Block block, BlockBreakEvent e){
      e.setDropItems(false);
      tryDropItems(block.getDrops(player.getInventory().getItemInMainHand()), player, block.getLocation());
      player.getInventory().setItemInMainHand(new ItemCreator(player.getInventory().getItemInMainHand()).setDurability(1).getItemStack());
    }
  }
