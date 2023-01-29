package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;


public class RbBlocksDisplayGUI extends PaginatedMenu {
    private final RespawnBlocks instance;
    private final int page;

    public RbBlocksDisplayGUI(RespawnBlocks instance, int page) {
        this.instance = instance;
        this.page = page;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot, ItemStack cursor) {
        List<RespawnBlock> blocks = new ArrayList<>(instance.getRBManager().getRespawnBlocksList());
        if (clickedItem.getType().equals(Material.DARK_OAK_BUTTON)) {
            String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            if (itemName.equalsIgnoreCase("Left")) {
                if (page == 0) {
                    player.sendMessage("Cannot go backwards, already on first page");
                    return;
                }
                super.page = page - 1;
                player.openInventory(new RbBlocksDisplayGUI(instance, super.page).getInventory());
            } else if (itemName.equalsIgnoreCase("Right")) {
                if (!((index + 1) > blocks.size())) {
                    super.page = page + 1;
                    player.openInventory(new RbBlocksDisplayGUI(instance, super.page).getInventory());
                } else {
                    player.sendMessage("Last page, cannot go farther");
                }
            }
        }

        if (!clickedItem.hasItemMeta() || clickedItem.getItemMeta().getPersistentDataContainer().isEmpty()) {
            return;
        }
        PersistentDataContainer pdc = clickedItem.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey(instance, "xcoord"), PersistentDataType.INTEGER) ||
                !pdc.has(new NamespacedKey(instance, "ycoord"), PersistentDataType.INTEGER) ||
                !pdc.has(new NamespacedKey(instance, "zcoord"), PersistentDataType.INTEGER)) {
            return;
        }
        player.openInventory(new BlockManagementGUI(new ItemCreator(clickedItem).removeAllLore().getItemStack(), instance, player).getInventory());
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public Inventory getInventory() {
        design("&2Blocks", 54, 6, 48,50);
        List<RespawnBlock> blocks = new ArrayList<>(instance.getRBManager().getRespawnBlocksList());
        blocks.sort((a, b) -> a.getInitialBlockType().toString().compareToIgnoreCase(b.getInitialBlockType().toString()));
        if (!blocks.isEmpty()) {
            for (int i = 0; i < super.maxPageItems; i++) {
                index = super.maxPageItems * this.page + i;
                if (index >= blocks.size()) {
                    break;
                }
                if (blocks.get(index) != null) {
                    inv.setItem(inv.firstEmpty(), new ItemCreator(blocks.get(index).getInitialBlockType(), 1).setDisplayName("Block Id: " +
                                    blocks.get(index).getBlockID())
                            .addGlow(true).addLoreLine(ChatColor.GOLD + "Click to open this block's menu")
                            .setPDCInteger(new NamespacedKey(instance, "xcoord"), blocks.get(index).getX())
                            .setPDCInteger(new NamespacedKey(instance, "ycoord"), blocks.get(index).getY())
                            .setPDCInteger(new NamespacedKey(instance, "zcoord"), blocks.get(index).getZ())
                            .getItemStack());
                }
            }
        }
        return inv;
    }
}