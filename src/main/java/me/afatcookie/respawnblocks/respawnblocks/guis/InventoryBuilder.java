package me.afatcookie.respawnblocks.respawnblocks.guis;
import dev.dbassett.skullcreator.SkullCreator;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This is an inventory builder which can be used for ease of access to preset, settings and more!
 * @author AFatCookie
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class InventoryBuilder {

    private Inventory inv;

    /**
     * default constructor; can be used when creating inventories, but usually will only work with config inventories
     */
    private InventoryBuilder() {
    }

    /**
     * creates a default inventory with only a name
     *
     * @param name inventories name
     */
    public InventoryBuilder(String name) {
        this.inv = Bukkit.createInventory(null, 9, colorizeMessage(name));
    }

    /**
     * creates a default inventory with only a name, and a size
     *
     * @param name inventories name
     * @param size inventories size
     */
    public InventoryBuilder(String name, int size) {
        this.inv = Bukkit.createInventory(null, size, colorizeMessage(name));
    }

    /**
     * creates a default inventory with a name, size, and inventoryHolder
     *
     * @param name   inventories name
     * @param size   inventories size
     * @param player inventories holder
     */
    public InventoryBuilder(String name, int size, InventoryHolder player) {
        this.inv = Bukkit.createInventory(player, size, colorizeMessage(name));
    }

    /**
     * creates a default inventory with a name, and an inventoryType, replacing size
     *
     * @param name inventories name
     * @param type type of inventory to be created
     */
    public InventoryBuilder(String name, InventoryType type) {
        this.inv = Bukkit.createInventory(null, type, colorizeMessage(name));
    }

    /**
     * creates a default inventory with a name, an inventoryType, and an inventoryHolder
     *
     * @param name   inventories name
     * @param type   type of inventory to be created
     * @param player inventories holder
     */
    public InventoryBuilder(String name, InventoryType type, InventoryHolder player) {
        this.inv = Bukkit.createInventory(player, type, colorizeMessage(name));
    }

    /**
     * replace this instance's inventory with a specified inventory
     *
     * @param inventory inventory to replace this instance's inventory
     */
    public InventoryBuilder(Inventory inventory) {
        this.inv = inventory;
    }

    /**
     * recreate this instance's inventory
     *
     * @return this instance's new inventory
     */
    public InventoryBuilder reCreate() {
        return new InventoryBuilder(this.inv);
    }

    /**
     * set the specified itemStack at the specified slot in this instance's inventory
     *
     * @param index     slot to place itemStack in this instance's inventory
     * @param itemStack itemStack to place in the slot of this instance's inventory
     * @return set item to this instance's inventory
     */
    public InventoryBuilder setSlot(int index, ItemStack itemStack) {
        this.inv.setItem(index, itemStack);
        return this;
    }

    /**
     * fill the top of the inventory with a glass color. the parameter only needs the color.
     * ex:
     * PROPER USAGE:
     * fillTop(WHITE)
     * INVALID USAGE:
     * fillTop(MATERIAL.WHITE_STAINED_GLASS_PANE)
     *
     * @param glassColor glass color to use
     * @return applied filling to this instance's inventory
     */
    public InventoryBuilder fillTop(String glassColor) {
        for (int i = 0; i < 9; i++) {
            this.inv.setItem(i, new ItemStack(Material.valueOf(glassColor + "_STAINED_GLASS_PANE")));
        }
        return this;
    }

    /**
     * fill the top of the inventory with an itemStack.
     *
     * @param is itemStack to use
     * @return applied filling to this instance's inventory
     */
    public InventoryBuilder fillTop(ItemStack is) {
        for (int i = 0; i < 9; i++) {
            this.inv.setItem(i, is);
        }
        return this;
    }

    /**
     * fill the bottom part of the inventory with a specified glass color. This method is similar to fillTop so please read
     * that's documentation
     *
     * @param glassColor color of glass to use
     * @param rows       rows of inventory
     * @return Applied filling to this instance's inventory
     */
    public InventoryBuilder fillBottom(String glassColor, int rows) {
        ItemStack filler;
        Material material = Material.getMaterial(glassColor + "_STAINED_GLASS_PANE");
        if (material == null) {
            filler = new ItemStack(Material.BEDROCK);
        } else {
            filler = new ItemStack(Objects.requireNonNull(Material.getMaterial(glassColor + "_STAINED_GLASS_PANE")));
        }
        for (int i = (rows - 1) * 9; i < this.inv.getSize(); i++) {
            this.inv.setItem(i, filler);
        }
        return this;
    }

    /**
     * fills the bottom part of the inventory with a specified itemStack.
     *
     * @param is   itemStack to be used
     * @param rows rows of inventory
     * @return applied filling to this instance's inventory
     */
    public InventoryBuilder fillBottom(ItemStack is, int rows) {
        for (int i = (rows - 1) * 9; i < this.inv.getSize(); i++) {
            this.inv.setItem(i, is);
        }
        return this;
    }

    /**
     * fills in the sides of the inventory with the specified glass color. This method handles the parameters similar to
     * the fillTop method, so please read its documentation.
     *
     * @param glassColor color of glass to use
     * @param size       size of inventory
     * @return applied filling of this instance's inventory
     */
    public InventoryBuilder fillSides(String glassColor, int size) {
        ItemStack filler;
        Material material = Material.getMaterial(glassColor + "_STAINED_GLASS_PANE");
        if (material == null) {
            filler = new ItemStack(Material.BEDROCK);
        } else {
            filler = new ItemStack(Objects.requireNonNull(Material.getMaterial(glassColor + "_STAINED_GLASS_PANE")));
        }
        switch (size) {
            case 9:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                break;
            case 18:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                break;
            case 27:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                break;
            case 36:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                this.inv.setItem(27, filler);
                this.inv.setItem(35, filler);
                break;
            case 45:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                this.inv.setItem(27, filler);
                this.inv.setItem(35, filler);
                this.inv.setItem(36, filler);
                this.inv.setItem(44, filler);
                break;
            case 54:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                this.inv.setItem(27, filler);
                this.inv.setItem(35, filler);
                this.inv.setItem(36, filler);
                this.inv.setItem(44, filler);
                this.inv.setItem(45, filler);
                this.inv.setItem(53, filler);
                break;
        }
        return this;
    }

    public InventoryBuilder fillSides(ItemStack filler, int size){
        switch (size) {
            case 9:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                break;
            case 18:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                break;
            case 27:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                break;
            case 36:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                this.inv.setItem(27, filler);
                this.inv.setItem(35, filler);
                break;
            case 45:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                this.inv.setItem(27, filler);
                this.inv.setItem(35, filler);
                this.inv.setItem(36, filler);
                this.inv.setItem(44, filler);
                break;
            case 54:
                this.inv.setItem(0, filler);
                this.inv.setItem(8, filler);
                this.inv.setItem(9, filler);
                this.inv.setItem(17, filler);
                this.inv.setItem(18, filler);
                this.inv.setItem(26, filler);
                this.inv.setItem(27, filler);
                this.inv.setItem(35, filler);
                this.inv.setItem(36, filler);
                this.inv.setItem(44, filler);
                this.inv.setItem(45, filler);
                this.inv.setItem(53, filler);
                break;
        }
        return this;
    }

    /**
     * fills in the remaining spaces of the inventory that are either Material. AIR, or null. This method handles the parameters
     * similar to the fillTop method, so please read its documentation.
     *
     * @param glassColor color of glass to use
     * @return applied filling of this instance's inventory
     */
    public InventoryBuilder fillIn(String glassColor) {
        ItemStack filler;
        Material material = Material.getMaterial(glassColor + "_STAINED_GLASS_PANE");
        if (material == null) {
            filler = new ItemStack(Material.BEDROCK);
        } else {
            filler = new ItemStack(Objects.requireNonNull(Material.getMaterial(glassColor + "_STAINED_GLASS_PANE")));
        }
        for (int i = 0; i < this.inv.getSize(); i++) {
            this.inv.setItem(i, filler);
        }
        return this;
    }

    /**
     * fills in the remaining spaces of the inventory that are either Material. AIR, or null with the parameterized itemStack.
     *
     * @param is itemStack to use
     * @return applied filling of this instance's inventory
     */
    public InventoryBuilder fillIn(ItemStack is) {
        for (int i = 0; i < this.inv.getSize(); i++) {
            this.inv.setItem(i, is);
        }
        return this;
    }

    /**
     * replaces this instance's inventory contents with the specified array of itemStacks.
     *
     * @param itemStacks itemStack array to replace this instance's inventory with
     * @return new items of this instance's inventory
     */
    public InventoryBuilder replaceInventory(ItemStack[] itemStacks) {
        if (itemStacks.length > this.inv.getSize()) return null;
        this.inv.setStorageContents(itemStacks);
        return this;
    }

    /**
     * creates a gui from configuration files. This method can throw a lot of errors, so its best you understand how it works.
     * In order for this method to work, there must be:
     * gui.name: STRING_VALUE
     * gui.rows: INTEGER_VALUE
     * gui.filler: STRING_VALUE that turns into an itemStack when working with ItemCreator
     * gui.fillstyle: STRING_VALUE
     * and then the configItemSection which will read what will be put into the config
     * This method will ONLY work when used with ItemCreator.
     *
     * @param file              the file to look at for the gui
     * @param configItemSection the section that the gui's items' information is in
     * @param holder            the holder of the inventory.
     * @return a gui-made inventory for this instance's inventory
     */
    public InventoryBuilder createGUIFromConfig(FileConfiguration file, String configItemSection, InventoryHolder holder, String menuSection) {
        HashMap<Integer, ItemCreator> guiItems = new HashMap<>();
        String inventoryName;
        int inventorySize;
        ItemStack filler;
        List<String> fillStyle;
        inventoryName = colorizeMessage(file.getString(menuSection + ".name"));
        inventorySize = file.getInt(menuSection + ".rows") * 9;
        filler = new ItemCreator(true, file.getString(menuSection +".filler")).setDisplayName(file.getString(menuSection + ".fillername")).getItemStack();
        fillStyle = file.getStringList(menuSection + ".fillstyle");
        if (file.getConfigurationSection(configItemSection) != null) {
            for (String itemAccessor : file.getConfigurationSection(configItemSection).getKeys(false)) {
                String path = configItemSection + "." + itemAccessor;
                int slot = file.getInt(path + ".slot");
                ItemStack material = new ItemStack(Material.DIRT, 1);
                if (file.getString(path + ".material") == null || !file.contains(path + ".material")) {
                    if (file.getString(path + ".url") != null && file.contains(path + ".url")) {
                        material = SkullCreator.itemFromUrl(file.getString(path + ".url"));
                    } else if (file.getString(path + ".base64") != null && file.contains(path + ".base64")) {
                        material = SkullCreator.itemFromBase64(file.getString(path + ".base64"));
                    }
                } else {
                    material = new ItemStack(Material.getMaterial(file.getString(path + ".material")));
                }
                if (slot < 0 || slot >= inventorySize) {
                    Bukkit.getLogger().warning("the slot provided for one of your items is out of bounds of inventory space!");
                    continue;
                }
                ItemCreator is = new ItemCreator(material).setDisplayName
                        (file.getString(path + ".name")).setUnbreakable
                        (file.getBoolean(path + ".unbreakable")).hideUnbreakable().addGlow
                        (file.getBoolean(path + ".glow")).setLore(file.getStringList(path + ".lore"));
                guiItems.put(slot, is);
            }
        }

        this.inv = Bukkit.createInventory(holder, inventorySize, inventoryName);
        if (!fillStyle.isEmpty()) {
            for (String fillStyleChoice : fillStyle) {
                switch (fillStyleChoice.toLowerCase()) {
                    case "fillall":
                        fillIn(filler);
                        break;
                    case "filltop":
                        fillTop(filler);
                        break;
                    case "fillbottom":
                        fillBottom(filler, inventorySize / 9);
                        break;
                    case "fillsides":
                        fillSides(filler, inventorySize / 9);
                        break;
                }
            }
        } else {
            for (int i = 0; i < inventorySize; i++) {
                this.inv.setItem(i, filler);
            }
        }
        if (guiItems.isEmpty()) return this;
        for (int additions : guiItems.keySet()) {
            this.inv.setItem(additions, guiItems.get(additions).getItemStack());
        }
        return this;
    }

    /**
     * gets this instance's inventory
     *
     * @return this instance's inventory
     */
    public Inventory build() {
        return this.inv;
    }

    /**
     * gets this instance's inventory items
     *
     * @return the contents of this instance's inventory
     */
    public ItemStack[] getInventoryItems() {
        return this.inv.getContents();
    }

    /**
     * gets the holder of this instance's inventory
     *
     * @return the holder of this instance's inventory
     */
    public InventoryHolder getHolder() {
        return this.inv.getHolder();
    }

    /**
     * gets the size of this instance's inventory
     *
     * @return the size of this instance's inventory
     */
    public int getSize() {
        return this.inv.getSize();
    }

    private String colorizeMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
