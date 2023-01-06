package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class RbBlocksDisplayGUI extends PaginatedMenu{

    RespawnBlocks instance;

    int page;
    public RbBlocksDisplayGUI(RespawnBlocks instance, int page){
        this.instance = instance;
        this.page = page;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot, ItemStack cursor) {

        ArrayList<RespawnBlock> blocks = new ArrayList<>(instance.getRBManager().getRespawnBlocksList());
        if (clickedItem.getType().equals(Material.DARK_OAK_BUTTON)){
            if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase("Left")){
                if (page == 0){
                    player.sendMessage("Cannot go backwards, already on first page");
                    return;
                }
                super.page = page - 1;
                player.openInventory(new RbBlocksDisplayGUI(instance, super.page).getInventory());
            }else
            if (ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase("Right")){
                if (!((index + 1) > blocks.size())){
                    super.page = page + 1;
                    player.openInventory(new RbBlocksDisplayGUI(instance, super.page).getInventory());
        } else {
          player.sendMessage("Last Page, cannot go farther");
                }
            }
        }

        if (!clickedItem.hasItemMeta()) return;
        if (clickedItem.getItemMeta().getPersistentDataContainer().isEmpty()) return;
        if (!clickedItem
                .getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(instance, "xcoord"), PersistentDataType.INTEGER)) return;
        if (!clickedItem
                .getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(instance, "ycoord"), PersistentDataType.INTEGER)) return;
        if (!clickedItem
                .getItemMeta()
                .getPersistentDataContainer()
                .has(new NamespacedKey(instance, "zcoord"), PersistentDataType.INTEGER)) return;
        player.openInventory(new BlockManagementGUI(clickedItem, instance, player).getInventory());
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public Inventory getInventory() {
        design();
        ArrayList<RespawnBlock> blocks = new ArrayList<>(instance.getRBManager().getRespawnBlocksList());
        blocks.sort((a,b) -> a.getInitialBlockType().toString().compareToIgnoreCase(b.getInitialBlockType().toString()));
        if (!blocks.isEmpty()){
      for (int i = 0; i < super.maxPageItems; i++) {
        index = super.maxPageItems * this.page + i;
        if (index >= blocks.size()) break;
        if (blocks.get(index) != null){
            inv.setItem(inv.firstEmpty(), new ItemCreator(
                    blocks.get(index).getInitialBlockType(), 1) .addGlow(true)
                    .addLoreLine("&cClick here to teleport to this Block!") .addLoreLine( "&6X-Coordinate: " +
                            blocks.get(index).getX()) .addLoreLine(
                            "&6Y-Coordinate: " + instance.getRBManager().getRespawnBlocksList().get(index).getY())
                    .addLoreLine( "&6Z-Coordinate: " +
                            blocks.get(index).getZ()) .setPDCInteger( new
                                    NamespacedKey(instance, "xcoord"),
                            blocks.get(index).getX()) .setPDCInteger( new
                                    NamespacedKey(instance, "ycoord"),
                            blocks.get(index).getY()) .setPDCInteger( new
                                    NamespacedKey(instance, "zcoord"),
                            blocks.get(index).getZ()) .getItemStack());
        }
      }
        }

        return inv;
    }

}
