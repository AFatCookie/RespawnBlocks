package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
/*
Listens for left clicks with admin wand
 */
public class ClickListener implements Listener {
  private final RespawnBlocks instance;

  public ClickListener(RespawnBlocks respawnBlocks) {
    instance = respawnBlocks;
  }

  @EventHandler
  public void onClick(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    if (!hasWandPermission(player)) return;

    Block targetBlock = e.getClickedBlock();
    if (targetBlock == null) return;

    if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
      instance.getRBManager().setBlock1(targetBlock);
      player.sendMessage(ChatColor.GOLD + "Set block 1!");
    } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      instance.getRBManager().setBlock2(targetBlock);
      player.sendMessage(ChatColor.GOLD + "Set block 2!");
    }
  }

  private boolean hasWandPermission(Player player) {
    if (!player.getInventory().getItemInMainHand().hasItemMeta()) return false;
    if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
            .has(new NamespacedKey(instance, "wand"), PersistentDataType.STRING)) return false;
    if (!player.hasPermission("respawnblocks.admin")) {
      player.sendMessage(ChatColor.RED + "Insufficient permission to use this item.");
      return false;
    }
    return true;
  }
}