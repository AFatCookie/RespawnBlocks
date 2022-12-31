package me.afatcookie.respawnblocks.respawnblocks.guis;

import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public abstract class PaginatedMenu implements GUI {

    protected Inventory inv;

    protected int page = 0;

    protected int maxPageItems = 28;

    protected int index = 0;

    public void design(){
       inv = new InventoryBuilder("&2Blocks", 54, this).fillBottom("WHITE", 6).fillSides("WHITE", 54).fillTop("WHITE")
                .setSlot(48, new ItemCreator(Material.DARK_OAK_BUTTON, 1).setDisplayName("Left").getItemStack()).setSlot(50,
                        new ItemCreator(Material.DARK_OAK_BUTTON, 1).setDisplayName("Right").getItemStack()).build();
    }

}
