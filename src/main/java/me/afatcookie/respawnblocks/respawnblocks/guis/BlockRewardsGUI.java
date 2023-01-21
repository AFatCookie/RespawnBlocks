package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.Reward;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
        // Get the list of rewards for the block
        ArrayList<Reward> rewards = block.getRewards();

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
                if (!((index + 1) > rewards.size())) {
                    super.page = page + 1;
                    player.openInventory(new BlockRewardsGUI(block, instance, super.page).getInventory());
                }
                // If this is the last page, do nothing
                else {
                    player.sendMessage("Last page, cannot go farther");
                    return;
                }
            }
        }

        // If the clicked item is a reward item
        for (Reward reward : rewards) {
            if (reward.getItem().getType() == clickedItem.getType()) {
                // Remove the reward from the list
                player.sendMessage(ChatColor.GREEN + "Removed item: " + reward.getItem().getType() + " From block rewards!");
                rewards.remove(reward);
                break;
            }
        }
        // Save the updated list of rewards
        block.setRewards(rewards);

        // Close the inventory
        player.closeInventory();
    }


    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public Inventory getInventory() {
        design("&3Rewards", 54, 6, 48,50);
        List<Reward> rewards = new ArrayList<>(block.getRewards());
        rewards.sort((a, b) -> a.getItem().getType().toString().compareToIgnoreCase(b.getItem().getType().toString()));
        if (!rewards.isEmpty()) {
            for (int i = 0; i < super.maxPageItems; i++) {
                index = super.maxPageItems * this.page + i;
                if (index >= rewards.size()) {
                    break;
                }
                if (rewards.get(index) != null) {
                    ItemStack inventoryItem =  new ItemCreator(rewards.get(index).getItem()).
                            setLore("&2Click this to remove it from this blocks rewards!",
                            "&bItem Weight: " + rewards.get(index).getWeight()).getItemStack();
                    inv.setItem(inv.firstEmpty(), inventoryItem);
                }
            }
        }
        return inv;
    }
}
