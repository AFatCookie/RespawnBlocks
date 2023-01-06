package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.guis.BlockManagementGUI;
import me.afatcookie.respawnblocks.respawnblocks.guis.GUI;
import me.afatcookie.respawnblocks.respawnblocks.guis.RbBlocksDisplayGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {

    @EventHandler
    public static void onClick(InventoryClickEvent e){
        if (!(e.getInventory().getHolder() instanceof GUI)){
            return;
        }
        if (e.getCurrentItem() == null){
            return;
        }
        if (e.getClickedInventory() == null) return;
        final GUI getGUI = (GUI) e.getInventory().getHolder();
    if (e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem() == null || e.getCurrentItem().getType().toString().contains("PANE")){
        e.setCancelled(true);
        }

    if (getGUI instanceof RbBlocksDisplayGUI){
        e.setCancelled(true);
    }
    if (getGUI instanceof BlockManagementGUI){
        if (e.getClickedInventory().toString().contains("Custom")){
        e.setCancelled(true);
            }
    }
        getGUI.onClick((Player) e.getWhoClicked(), e.getInventory(), e.getCurrentItem(), e.getClick(), e.getSlot(),
                e.getCursor());
    }

    @EventHandler
    public static  void onClose(InventoryCloseEvent e){
        if (!(e.getInventory().getHolder() instanceof GUI)) return;
        final GUI getGUI = (GUI) e.getInventory().getHolder();
        getGUI.onClose((Player) e.getPlayer(), e.getInventory());

    }
}
