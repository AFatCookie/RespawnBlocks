package me.afatcookie.respawnblocks.respawnblocks.guis;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
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
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class BlockDisplayGUI implements GUI {
    Inventory inv;

    RespawnBlocks instance;

    public BlockDisplayGUI(RespawnBlocks instance){
        this.instance = instance;
    }
    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, ClickType clickType, int slot) {
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
        int x =
                clickedItem
                        .getItemMeta()
                        .getPersistentDataContainer()
                        .get(new NamespacedKey(instance, "xcoord"), PersistentDataType.INTEGER);
        int y =
                clickedItem
                        .getItemMeta()
                        .getPersistentDataContainer()
                        .get(new NamespacedKey(instance, "ycoord"), PersistentDataType.INTEGER);
        int z =
                clickedItem
                        .getItemMeta()
                        .getPersistentDataContainer()
                        .get(new NamespacedKey(instance, "zcoord"), PersistentDataType.INTEGER);
        findSafeTPSpace(player.getWorld().getBlockAt(x,y,z), player);
    }

    @Override
    public void onClose(Player player, Inventory inventory) {

    }

    @Override
    public Inventory getInventory() {
        inv = new InventoryBuilder("&a&nBlocks", 54, this).build();
        for (int i = 0; i < instance.getRBManager().getRespawnBlocksList().size(); i++) {
            fillInv(inv, i);
        }
        return inv;
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
        player.closeInventory();
    }

    private boolean checkTopY(Block block){
        return block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).getType() == Material.AIR;
    }

    private boolean checkBottomY(Block block){
        return block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == Material.AIR;
    }

    public void fillInv(Inventory inventory, int i){
        inventory.setItem( i, new ItemCreator(
                instance.getRBManager().getRespawnBlocksList().get(i).getInitialBlockType(), 1) .addGlow(true)
                .addLoreLine("&cClick here to teleport to this Block!") .addLoreLine( "&6X-Coordinate: " +
                        instance.getRBManager().getRespawnBlocksList().get(i).getxCoord()) .addLoreLine(
                        "&6Y-Coordinate: " + instance.getRBManager().getRespawnBlocksList().get(i).getyCoord())
                .addLoreLine( "&6Z-Coordinate: " +
                        instance.getRBManager().getRespawnBlocksList().get(i).getzCoord()) .setPDCInteger( new
                                NamespacedKey(instance, "xcoord"),
                        instance.getRBManager().getRespawnBlocksList().get(i).getxCoord()) .setPDCInteger( new
                                NamespacedKey(instance, "ycoord"),
                        instance.getRBManager().getRespawnBlocksList().get(i).getyCoord()) .setPDCInteger( new
                                NamespacedKey(instance, "zcoord"),
                        instance.getRBManager().getRespawnBlocksList().get(i).getzCoord()) .getItemStack());
    }
}
