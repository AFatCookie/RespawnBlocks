package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public abstract class PaginatedMenu implements GUI {

    protected Inventory inv;

    protected int page = 0;

    protected int maxPageItems = 28;

    protected int index = 0;

    public void design(String name, int size, int rows, int buttonOneSlot, int buttonTwoSlot){
       inv = new InventoryBuilder(name, size, this).fillBottom("WHITE", rows).fillSides("WHITE", size).fillTop("WHITE")
                .setSlot(buttonOneSlot, new ItemCreator(Material.DARK_OAK_BUTTON, 1).setDisplayName("Left").getItemStack()).setSlot(buttonTwoSlot,
                        new ItemCreator(Material.DARK_OAK_BUTTON, 1).setDisplayName("Right").getItemStack()).build();
    }

}
