package me.afatcookie.respawnblocks.respawnblocks.block;

import org.bukkit.inventory.ItemStack;

public class Reward {

    private  int blockID;


    private  ItemStack item;

    private int weight;


    public Reward(int blockID, ItemStack item, int weight) {
        this.blockID = blockID;
        this.item = item;
        this.weight = weight;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
