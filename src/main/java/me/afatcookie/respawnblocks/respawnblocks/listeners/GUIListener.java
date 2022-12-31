package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.guis.GUI;
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
        final GUI getGUI = (GUI) e.getInventory().getHolder();
        e.setCancelled(true);
        getGUI.onClick((Player) e.getWhoClicked(), e.getInventory(), e.getCurrentItem(), e.getClick(), e.getSlot());
    }

    @EventHandler
    public static  void onClose(InventoryCloseEvent e){
        if (!(e.getInventory().getHolder() instanceof GUI)) return;
        final GUI getGUI = (GUI) e.getInventory().getHolder();
        getGUI.onClose((Player) e.getPlayer(), e.getInventory());

    }
}
