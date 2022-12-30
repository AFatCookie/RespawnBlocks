package me.afatcookie.respawnblocks.respawnblocks.utils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * This is an item creator class which allows for an easy and alternative way of creating ItemStacks.
 * All you do is create a new instance of this class, and you can create the item to your desire!
 * @author AFatCookie
 *
 */
@SuppressWarnings({"unused", "ConstantConditions", "UnusedReturnValue"})
public class ItemCreator {


    private ItemStack itemStack;


    private ItemCreator() { }

    /**
     * Create ItemCreator from scratch, using a material
     * @param m Material of itemStack
     */
    public ItemCreator(Material m){
        this.itemStack = new ItemStack(m, 1);
    }


    /**
     * Create ItemCreator from scratch, using a material and a specified amount
     * @param m material of usage
     * @param amount amount of itemStack
     */
    public ItemCreator(Material m, int amount){
        this.itemStack = new ItemStack(m, amount);
    }
    /**
     * Create ItemCreator from scratch, using a string for a material
     *
     * @param materialAsString material to use
     */
    public ItemCreator(String materialAsString) {
        if (Material.getMaterial(materialAsString.toUpperCase()) == null) {
            this.itemStack = new ItemStack(Material.BEDROCK, 1);
        } else {
            this.itemStack = new ItemStack(Material.getMaterial(materialAsString.toUpperCase()), 1);
        }
    }


    /**
     * Create ItemCreator from scratch, using a string for a material, and a specified amount
     * @param materialAsString material as string for usage
     * @param amount amount of itemStack
     */
    public ItemCreator(String materialAsString, int amount){
        if (Material.getMaterial(materialAsString.toUpperCase()) == null){
            this.itemStack = new ItemStack(Material.DIRT, 1);
        }
        this.itemStack = new ItemStack(Material.getMaterial(materialAsString.toUpperCase()), amount);
    }

    public ItemCreator(boolean isGlass, String glassColor){
        this.itemStack = new ItemStack(Material.getMaterial(glassColor + "_STAINED_GLASS_PANE"));
    }

    public ItemCreator(boolean isFiller, String material, boolean forGui){
        if (Material.getMaterial(material) ==null){
            this.itemStack = new ItemStack(Material.DIRT, 1);
        }
        this.itemStack = new ItemStack(Material.getMaterial(material));
    }

    /**
     * Overrides this itemStack with parameterized itemStack
     * @param itemStack itemStack to override this instances itemStack
     */
    public ItemCreator(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    /**
     * clone this instance's itemStack
     * @return new itemStack
     */
    public ItemCreator reCreate(){
        return new ItemCreator(this.itemStack);
    }

    /**
     * Set the display name for this instance's itemStack
     * @param displayName desired name for itemStack
     * @return applied name of itemStack
     */
    public ItemCreator setDisplayName(String displayName){
        ItemMeta im = getItemMeta();
        im.setDisplayName(colorizeMessage(displayName));
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * To avoid deprecation, this method sets the durability by subtracting the items max durability by the parametrized amount.
     * NOTE: this method will only work if the item *HAS* durability, ex: (Tools, Bows, Armor) are all things that have durability.
     * NOTE: if the item doesn't have durability, this method will do nothing
     * @param durability the amount of durability loss the item should have on creation.
     * @return ItemCreator
     */
    public ItemCreator setDurability(int durability) {
        ItemMeta im = getItemMeta();
        assert im != null;
        if (!(this.itemStack.getType().getMaxDurability() > 0)) return this;
        org.bukkit.inventory.meta.Damageable damageable = (org.bukkit.inventory.meta.Damageable) im;
        damageable.setDamage(damageable.getDamage() + durability);
        itemStack.setItemMeta(damageable);
        return this;
    }

    /**
     * If this instance's itemStack should be unbreakable
     * @param ifUnbreakable True if item should be unbreakable, false if item shouldn't be unbreakable
     * @return Applied status of this instance's unbreakability status
     */
    public ItemCreator setUnbreakable(boolean ifUnbreakable){
        ItemMeta im = getItemMeta();
        assert im != null;
        im.setUnbreakable(ifUnbreakable);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Adds itemFlags to this instance's itemStack
     * @param itemFlags ItemFlags to add
     * @return Applied ItemFlags status to this instance's itemStack
     */
    public ItemCreator addItemFlags(ItemFlag... itemFlags){
        ItemMeta im = getItemMeta();
        assert im != null;
        im.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Removes itemFlags from this instance's itemStack
     * @param itemFlags ItemFlags to remove
     * @return Applied ItemFlag status to this instance's itemStack
     */
    public ItemCreator removeItemFlags(ItemFlag... itemFlags){
        ItemMeta im = getItemMeta();
        assert im != null;
        if (im.getItemFlags().isEmpty()) return this;
        im.removeItemFlags(itemFlags);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Adds an enchantment to this instance's itemStack. Does ignore level restriction and can be an enchantment not
     * applicable to this item type.
     * @param enchantment Enchantment to add
     * @param enchantmentLevel Level of enchantment to be added
     * @return Applied enchants status of this instance's itemStack
     */
    public ItemCreator addEnchantment(Enchantment enchantment, int enchantmentLevel){
        ItemMeta im = getItemMeta();
        assert im != null;
        im.addEnchant(enchantment, enchantmentLevel, true);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * adds glow to this instance's itemStack, using the enchantment LURE. You can manually apply your glow however using
     * the enchanting and itemFlag methods
     * @param shouldGlow if The item should glow
     * @return the applied enchants and itemFlags to this instance's itemStack.
     */
    public ItemCreator addGlow(boolean shouldGlow){
        if (shouldGlow){
            ItemMeta im = getItemMeta();
            im.addEnchant(Enchantment.LURE, 3, false);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            this.itemStack.setItemMeta(im);
        }
        return this;
    }

    /**
     * Adds multiple enchantments to this instance's itemStack. Does ignore level restriction nd can be an enchantment
     * not applicable to this item type.
     * @param enchantments Map of enchantments to be added, and their level. Enchantment being the key, level being the value
     * @return Applied enchants status of this instance's itemStack
     */
    public ItemCreator addMultipleEnchantments(Map<Enchantment, Integer> enchantments){
        this.itemStack.addUnsafeEnchantments(enchantments);
        return this;
    }

    /**
     * hides all enchants on this instance's itemStack
     * @return Applied status to this instance's itemStack
     */
    public ItemCreator hideAllEnchants(){
        ItemMeta im = getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * hides unbreakable on this instance's itemStack
     * @return Applied status to this instance's itemStack
     */
    public ItemCreator hideUnbreakable(){
        ItemMeta im = getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * hides all attributes on this instance's itemStack
     * @return Applied status to this instance's itemStack
     */
    public ItemCreator hideAttributes(){
        ItemMeta im = getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * sets a minecraft skull skin of specified player. This will only work if item is a skull and will return null otherwise
     * It's best to check if this player exists before using this method
     * @param uuid desired player's uuid
     * @return Applied skin to the skull of this instance's itemStack
     */
    public ItemCreator setSkullPlayerSkin(UUID uuid){
        ItemMeta im = getItemMeta();
        if (!this.itemStack.getType().equals(Material.PLAYER_HEAD)) return null;
        SkullMeta meta = (SkullMeta) im;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        itemStack.setItemMeta(meta);
        return this;
    }


    /**
     * Adds an attribute Modifier to this item. This will throw a null if any parameterized values are invalid, and will
     * throw an IllegalArgumentException if Attribute already exists on said item
     * @param attribute Attribute to be added
     * @param modifier attributeModifier to be added
     * @return Applied attributeModifier status to this instance's itemStack
     */
    public ItemCreator addAttributeModifier(Attribute attribute, AttributeModifier modifier){
        try{
            ItemMeta im = getItemMeta();
            assert im != null;
            im.addAttributeModifier(attribute, modifier);
            this.itemStack.setItemMeta(im);
        }catch (NullPointerException nullPointerException){
            System.out.println("failed to add Attribute Modifier to an item.");
            return null;
        }catch (IllegalArgumentException exception){
            System.out.println("Attribute already exists on an item.");
            return null;
        }
        return this;
    }

    /**
     * Remove attributeModifier from item
     * @param attribute attribute to be removed
     * @param modifier modifier to be removed
     * @return Applied attributeModifier status of this instance's itemStack
     */
    public ItemCreator removeAttributeModifier(Attribute attribute, AttributeModifier modifier){
        ItemMeta im = getItemMeta();
        assert im != null;
        if (!im.hasAttributeModifiers()) return this;
        if (!im.getAttributeModifiers().containsKey(attribute)) return this;
        im.removeAttributeModifier(attribute, modifier);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Sets the lore of this instance's itemStack via ArrayList
     * @param Lore Lore to be added
     * @return Applied lore status of this instance's itemStack
     */
    public ItemCreator setLore(String... Lore){
        ItemMeta im = getItemMeta();
        assert im != null;
        List<String> lore = new ArrayList<>();
        for (String line : Lore){
            lore.add(colorizeMessage(line));
        }
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Sets the lore of this instance's itemStack via List
     * @param Lore Lore to be added
     * @return Applied lore status of this instance's itemStack
     */
    public ItemCreator setLore(List<String> Lore){
        ItemMeta im = getItemMeta();
        assert im != null;
        List<String> lore = new ArrayList<>();
        for (String line : Lore){
            lore.add(colorizeMessage(line));
        }
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * removes all lore from item
     * @return Applied lore status of this instance's itemStack
     */
    @SuppressWarnings("ConstantConditions")
    public ItemCreator removeAllLore(){
        ItemMeta im = getItemMeta();
        assert im != null;
        if (!im.hasLore()) return this;
        List<String> lore = new ArrayList<>(im.getLore());
        lore.clear();
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Removes specified piece of lore from this instance's itemStack
     * @param line string of lore to be removed
     * @return Applied lore status of this instance's itemStack
     */
    @SuppressWarnings("ConstantConditions")
    public ItemCreator removeLoreLine(String line){
        ItemMeta im = getItemMeta();
        assert im != null;
        if (!im.hasLore()) return this;
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Removes lore line based off the provided index
     * @param loreIndex line of lore to be removed, based of it's position in the lore
     * @return Applied lore status of this instance's itemStack
     */
    @SuppressWarnings("ConstantConditions")
    public ItemCreator removeLoreLine(int loreIndex){
        ItemMeta im = getItemMeta();
        if (!im.hasLore()) return this;
        if (loreIndex < 0 || loreIndex > im.getLore().size()) return this;
        List<String> lore = new ArrayList<>(im.getLore());
        lore.remove(loreIndex);
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Adds a specific string to the lore of this instance's itemStack
     * @param line string to be added
     * @return Applied lore status of this instance's itemStack
     */
    public ItemCreator addLoreLine(String line){
        ItemMeta im = getItemMeta();
        assert im != null;
        List<String> lore;
        if (im.hasLore()){
            lore = new ArrayList<>(Objects.requireNonNull(im.getLore()));
        }else{
            lore = new ArrayList<>();
        }
        lore.add(colorizeMessage(line));
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Adds the parameterized string at the specified index of the lore
     * @param line string to be added
     * @param index index of lore where string should be added
     * @return Applied lore status of this instance's itemStack
     */
    @SuppressWarnings("ConstantConditions")
    public ItemCreator addLoreLine(String line, int index){
        ItemMeta im = getItemMeta();
        assert im != null;
        List<String> lore;
        if (im.hasLore()){
            lore = new ArrayList<>(im.getLore());
        }else{
            lore = new ArrayList<>();
        }
        lore.add(index, colorizeMessage(line));
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Replaces the line desired from the lore, with the replacement string provided.
     * @param lineToReplace line to replace
     * @param replacement replacement for the removed line
     * @return updated ItemStack for this instance.
     */
    public ItemCreator replaceLore(String lineToReplace, String replacement){
        ItemMeta im = getItemMeta();
        assert im != null;
        List<String> lore;
        if (im.hasLore()){
            lore = new ArrayList<>(im.getLore());
        }else{
            lore = new ArrayList<>();
            lore.add(colorizeMessage(replacement));
            return this;
        }
        for (String line : lore){
            if (line.contains(lineToReplace)){
                lore.set(lore.indexOf(line), colorizeMessage(replacement));
            }
        }
        im.setLore(lore);
        this.itemStack.setItemMeta(im);
        return this;
    }


    /**
     * if this instance's itemStack is wool, sets the wool color to the desired color via strings
     * @param color Color to be added. MUST BE IN ALL CAPITAL AND EXCLUDE _WOOL
     * @return Applied wool status of this instance's itemStack
     */
    public ItemCreator setWoolColor(String color){
        if (!this.itemStack.getType().toString().contains("WOOL")) return null;
        Material woolMat = Material.valueOf(color.toUpperCase() + "_WOOL");
        this.itemStack.setType(woolMat);
        return this;
    }

    /**
     * If this instance's itemStack is leather armor, sets the color of the armor
     * @param color Color to be added
     * @return Applied leather armor color status of this instance's itemStack
     */
    public ItemCreator setLeatherArmorColor(Color color){
        if (!this.itemStack.getType().toString().contains("LEATHER")) return null;
        if (this.itemStack.getType().toString().equals("LEATHER")) return null;
        try{
            LeatherArmorMeta im = (LeatherArmorMeta) getItemMeta();
            im.setColor(color);
            this.itemStack.setItemMeta(im);
        }catch (ClassCastException ignored) {}
        return this;
    }

    /**
     * This will create an item from a configuration file, on the assumption that the file contains
     * the information. in order to create an item via config, you must create a section where all
     * items will be created. This method will check in this section, and then look for the items
     * "key" and get the information of that key. example:
     *
     * <p>itemforserver: dagger: name: material: unbreakable: glowing: lore: this section here would
     * be for lore make it as long as needed as the next item starts below this as shown here at
     * "bowman:" bowman:
     *
     * <p>NOTE: this method will only read the following configurationSection prompts above, so make
     * sure to follow this example for items when trying to use this method
     *
     * @param file the file to read from
     * @param configItemSection the section in the config where item creation information will be
     *     stored
     * @return Applies all data to this instance's itemStack
     */
    public ItemCreator createItemsFromConfig(FileConfiguration file, String configItemSection) {
        if (file.getConfigurationSection(configItemSection) != null) {
            for (String itemAccessor : file.getConfigurationSection(configItemSection).getKeys(false)) {
                ItemStack item;
                String path = configItemSection + "." + itemAccessor;
                item = createItem(
                        file.getString(path + ".name"),
                        file.getString(path + ".material"),
                        file.getBoolean(path + ".unbreakable"),
                        file.getBoolean(path + ".glowing"),
                        file.getStringList(path + ".lore")
                );
                this.itemStack = item;
            }
        }
        return this;
    }


    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcStringValue value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCString(NamespacedKey keyAccessor, String pdcStringValue){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.STRING, pdcStringValue);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcIntValue value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCInteger(NamespacedKey keyAccessor, int pdcIntValue){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.INTEGER, pdcIntValue);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcFloatValue value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCFloat(NamespacedKey keyAccessor, float pdcFloatValue){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.FLOAT, pdcFloatValue);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcDoubleValue value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCDouble(NamespacedKey keyAccessor, double pdcDoubleValue){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.DOUBLE, pdcDoubleValue);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcByteValue value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCByte(NamespacedKey keyAccessor, byte pdcByteValue){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.BYTE, pdcByteValue);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcLongValue value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCLong(NamespacedKey keyAccessor, long pdcLongValue){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.LONG , pdcLongValue);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcIntArray value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCIntegerArray(NamespacedKey keyAccessor, int[] pdcIntArray){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.INTEGER_ARRAY, pdcIntArray);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcByteArray value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCByteArray(NamespacedKey keyAccessor, byte[] pdcByteArray){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.BYTE_ARRAY, pdcByteArray);
        this.itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add persistent data container to this instance's itemStack
     * @param keyAccessor key to access this PDC
     * @param pdcLongArray value to set this PDC with
     * @return applies PDC to this instance's itemStack
     */
    public ItemCreator setPDCLongArray(NamespacedKey keyAccessor, long[] pdcLongArray){
        ItemMeta im = getItemMeta();
        assert im != null;
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        pdc.set(keyAccessor, PersistentDataType.LONG_ARRAY, pdcLongArray);
        this.itemStack.setItemMeta(im);
        return this;
    }


    /**
     * builds the itemStack to be used
     * @return the itemStack from this instance
     */
    public ItemStack getItemStack(){
        return this.itemStack;
    }
    //Util methods for this class
    private ItemMeta getItemMeta(){
        return this.itemStack.getItemMeta();
    }

    private String colorizeMessage(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private ItemStack createItem(String name, String material, Boolean unbreakable, Boolean Glowing, List<String> Lore){
        ItemStack item;
        Material material1 = Material.getMaterial(material);
        if (material1 == null){
            material1 = Material.BEDROCK;
            Bukkit.getLogger().warning("The material '" + material + "' is not a valid material!");
        }
        item = new ItemStack(material1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(colorizeMessage(name));
        meta.setUnbreakable(unbreakable);
        if (Glowing) {
            meta.addEnchant(Enchantment.LURE, 3, false);
        }
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> updatedLore = new ArrayList<>(Lore.size());
        for (String line : Lore){
            updatedLore.add(colorizeMessage(line));
        }
        meta.setLore(updatedLore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getItem(){
        return this.itemStack;
    }

    private String sendErrorMessage(String message){
        return "[ItemCreator]: " + message;
    }

}

