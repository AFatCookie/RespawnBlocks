package me.afatcookie.respawnblocks.respawnblocks.guis;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface GUI extends InventoryHolder {

    void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot, ItemStack cursor);
    void onClose(Player player, Inventory inventory);
}
