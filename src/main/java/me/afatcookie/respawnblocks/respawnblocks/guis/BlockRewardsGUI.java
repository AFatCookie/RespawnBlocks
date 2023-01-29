package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.block.Reward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BlockRewardsGUI extends PaginatedMenu{

    RespawnBlock block;

    private final int page;

    RespawnBlocks instance;
    public BlockRewardsGUI(RespawnBlock block, RespawnBlocks instance, int page) {
        this.block = block;
        this.instance = instance;
        this.page = page;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot, ItemStack cursor) {

        // If the clicked item is a button
        if (clickedItem.getType().equals(Material.DARK_OAK_BUTTON)) {
            // Get the display name of the button
            String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            // If the button is the "Left" button
            if (itemName.equalsIgnoreCase("Left")) {
                // If the current page is the first page, do nothing
                if (page == 0) {
                    player.sendMessage("Cannot go backwards, already on first page");
                    return;
                }
                // Go to the previous page
                super.page = page - 1;
                player.openInventory(new BlockRewardsGUI(block, instance, super.page).getInventory());
            }
            // If the button is the "Right" button
            else if (itemName.equalsIgnoreCase("Right")) {
                // If there are more rewards to show on the next page, go to the next page
                if (!((index + 1) > block.getRewards().size())) {
                    super.page = page + 1;
                    player.openInventory(new BlockRewardsGUI(block, instance, super.page).getInventory());
                }
                // If this is the last page, do nothing
                else {
                    player.sendMessage("Last page, cannot go farther");
                    return;
                }
            }
        }else{
            for (Reward rew : block.getRewards()) {
                if (rew.getItem().getType() == clickedItem.getType()) {

                    // If the clicked item is a reward item
                    player.openInventory(new ConfirmMenu(clickedItem, block).getInventory());
                    return;
                }
            }
        }
        // Save the updated list of rewards

        // Close the inventory
        player.closeInventory();
    }


    @Override
    public void onClose(Player player, Inventory inventory) {
        inventory.clear();
    }

    @Override
    public Inventory getInventory() {
        design("&3Rewards", 54, 6, 48,50);
        block.getRewards().sort((a, b) -> a.getItem().getType().toString().compareToIgnoreCase(b.getItem().getType().toString()));
        if (!block.getRewards().isEmpty()) {
            for (int i = 0; i < super.maxPageItems; i++) {
                index = super.maxPageItems * this.page + i;
                if (index >= block.getRewards().size()) {
                    break;
                }
                if (block.getRewards().get(index) != null) {
                    inv.setItem(inv.firstEmpty(), block.getRewards().get(index).getItem());
                }
            }
        }
        return inv;
    }
}
