package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.block.Reward;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;


public class BlockManagementGUI implements GUI {

    Inventory inventory;
    ItemStack blockClicked;
    RespawnBlocks instance;
    RespawnBlock block;

    public BlockManagementGUI(ItemStack clickedItem, RespawnBlocks instance, Player player) {
        this.instance = instance;
        PersistentDataContainer pdc = clickedItem.getItemMeta().getPersistentDataContainer();
        int x = pdc.get(new NamespacedKey(instance, "xcoord"), PersistentDataType.INTEGER);
        int y = pdc.get(new NamespacedKey(instance, "ycoord"), PersistentDataType.INTEGER);
        int z = pdc.get(new NamespacedKey(instance, "zcoord"), PersistentDataType.INTEGER);
        block = instance.getRBManager().getRespawnBlock(player.getWorld().getBlockAt(x, y, z));
        this.blockClicked = new ItemCreator(clickedItem)
                .setDisplayName("BlockID: " + block.getBlockID())
                .getItemStack();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot, ItemStack cursor) {
        if (clickedItem.getType() == Material.ENDER_PEARL) {
            findSafeTPSpace(block.getBlock(), player);
        }
        if (clickedItem.isSimilar(blockClicked) && clickedItem.hasItemMeta() && blockClicked.hasItemMeta()){
            if (!cursor.getType().isBlock() || cursor.getType() == Material.AIR){
                player.sendMessage("This item cannot be a block!");
      } else {
                for (Reward reward : block.getRewards()) {
                    if (reward.getItem().getType() == block.getBlock().getType()) {
                        reward.setItem(new ItemStack(cursor.getType(), 1));
                        break;
                    }
                }
                player.sendMessage(ChatColor.GOLD + "Changed this block type to:" + ChatColor.WHITE + " " + ChatColor.BOLD + cursor.getType());
                instance.getDataConfig().removeBlockFromConfig(clickedItem.getType());
                instance.getDataConfig().addCDInfoToConfig(cursor.getType());
                block.setInitialBlockMaterial(cursor.getType());
                block.getBlock().setType(cursor.getType());

        player.closeInventory();
            }
        }
        if (clickedItem.getType() == Material.BARRIER){
            instance.getRBManager().getRespawnBlocksList().remove(block);
            block.getBlock().setType(clickedItem.getType());
            player.closeInventory();
            player.sendMessage(ChatColor.GOLD + "Removed this Respawn Block!");
        }
        if (clickedItem.getType() == Material.BOOK){
            player.openInventory(new BlockRewardsGUI(block, instance, 0).getInventory());
        }
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public Inventory getInventory() {
        inventory = new InventoryBuilder(" ", 54, this).fillIn("WHITE").setSlot(13, this.blockClicked).setSlot(30,
                        new ItemCreator(blockClicked).setDisplayName("&6Change Block").setLore("&7Click to change the block type!").getItemStack())
                .setSlot(31, new ItemCreator(Material.ENDER_PEARL, 1).setDisplayName("&3Teleport to Block").setLore("&7Teleport to this Block!",
                        "&6NOTE",
                        "&7If there is no open space around the block", "&7where the block is visible",
                        "&7it will force teleport you to the exact block location!", "(Potential for Suffocation, Drowning etc...)").getItemStack()).setSlot(32, new ItemCreator(
                                Material.BARRIER, 1
                ).setDisplayName("&cRemove Block").setLore("&7Stop this block from respawning.").getItemStack()).setSlot(40,
                        new ItemCreator(Material.BOOK, 1).setDisplayName("&5Rewards").setLore("See the rewards this block drops").getItemStack()).build();
        return inventory;
    }





    private void findSafeTPSpace(Block start, Player player) {
        ArrayList<BlockFace> bf = new ArrayList<>();
        bf.add(BlockFace.UP);
        bf.add(BlockFace.DOWN);
        bf.add(BlockFace.NORTH);
        bf.add(BlockFace.EAST);
        bf.add(BlockFace.SOUTH);
        bf.add(BlockFace.WEST);
        for (BlockFace blockFaces : bf) {
            if (start.getRelative(blockFaces).getType() != Material.AIR) continue;
            Block block = start.getRelative(blockFaces);
            if (checkTopY(block)){
                player.teleport(new Location(player.getWorld(), block.getX() + 0.5D, block.getY() + 1, block.getZ() + 0.5D));
                return;
            }
            if (checkBottomY(block)){
                player.teleport(new Location(player.getWorld(), block.getX() + 0.5D, block.getY() - 1, block.getZ() + 0.5D));
                return;
            }
        }
        player.sendMessage(ChatColor.RED + "Unable to find safe teleport next to block.");
        player.sendMessage(ChatColor.RED + "Force teleporting you to the block!");
        player.teleport(start.getLocation());
        player.closeInventory();
    }

    private boolean checkTopY(Block block){
        return block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).getType() == Material.AIR;
    }

    private boolean checkBottomY(Block block){
        return block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == Material.AIR;
    }


}

