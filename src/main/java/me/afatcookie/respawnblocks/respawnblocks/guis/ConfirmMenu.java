package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.block.Reward;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConfirmMenu implements GUI{

    Inventory inv;

    ItemStack itemToRemove;

    RespawnBlock respawnBlock;

    public ConfirmMenu(ItemStack itemStack, RespawnBlock block){
        this.itemToRemove = itemStack;
        this.respawnBlock = block;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot, ItemStack cursor) {
        if (clickedItem.hasItemMeta()){
            if (inventory.getItem(3).isSimilar(clickedItem)){
                player.closeInventory();
                for (Reward reward : respawnBlock.getRewards()) {
                    if (reward.getItem().isSimilar(itemToRemove)) {
                        // Remove the reward from the list
                        player.sendMessage(ChatColor.GREEN + "Removed item: " + reward.getItem().getType() + " From block rewards!");
                        respawnBlock.getRewards().remove(reward);
                        break;
                    }
                }
            } else if (inventory.getItem(5).isSimilar(clickedItem)) {
                player.closeInventory();
            }
        }
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public Inventory getInventory() {
        inv = new InventoryBuilder("&cAre You Sure?", 9, this).fillIn("WHITE").setSlot(3, new ItemCreator(Material.GREEN_STAINED_GLASS, 1).
                setDisplayName("&aConfirm").getItemStack()).setSlot(5, new ItemCreator(Material.RED_STAINED_GLASS, 1).setDisplayName("&cDeny").getItemStack()).build();
        return inv ;
    }
}
