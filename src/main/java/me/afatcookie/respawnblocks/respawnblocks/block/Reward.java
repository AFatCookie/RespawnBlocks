package me.afatcookie.respawnblocks.respawnblocks.block;

import org.bukkit.inventory.ItemStack;

public class Reward {

    private  int blockID;


    private  ItemStack item;


    public Reward(int blockID, ItemStack item) {
        this.blockID = blockID;
        this.item = item;
    }

    public int getBlockID() {
        return blockID;
    }

    public ItemStack getItem() {
        return item;
    }


    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

}
