package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
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

    public ClickListener(RespawnBlocks respawnBlocks){
        instance = respawnBlocks;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Action action = e.getAction();
        //Checking if player is holding wand + has permission to use wand
        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(instance, "wand"), PersistentDataType.STRING)) return;
        if (!player.hasPermission("respawnblocks.admin")){
            player.sendMessage(ChatColor.RED + "Insufficient permission to use this item.");
            return;
        }
        //Checking if a block was left-clicked, and if so, checks to see if it's already a RespawnBlock; Makes it one if not, removes it if so.
        if (e.getClickedBlock() == null) return;
        Block targetBlock = e.getClickedBlock();
        if (!action.equals(Action.LEFT_CLICK_BLOCK)) return;
        if (instance.getRBManager().getRespawnBlock(targetBlock) == null){
      RespawnBlock respawnBlock =
          new RespawnBlock(
              targetBlock.getX(),
              targetBlock.getY(),
              targetBlock.getZ(),
              instance, targetBlock.getType().toString(),
              targetBlock.getWorld().getName());
            instance.getRBManager().getRespawnBlocksList().add(respawnBlock);
            player.sendMessage(ChatColor.GREEN + "Successfully created a respawn block!");
            return;
        }
        if (instance.getTm().isInCoolDown(instance.getRBManager().getRespawnBlock(targetBlock)) && targetBlock != null){
            targetBlock.setType(instance.getRBManager().getRespawnBlock(targetBlock).getInitialBlockType());
        }
        instance.getRBManager().getRespawnBlocksList().remove(instance.getRBManager().getRespawnBlock(targetBlock));
        player.sendMessage(ChatColor.GREEN + "Successfully removed from respawnable blocks!");
    }
}
