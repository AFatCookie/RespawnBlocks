package me.afatcookie.respawnblocks.respawnblocks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
import java.util.Objects;

/*
This class just creates a respawnable Block, so I can manage them through the code
 */
public class RespawnBlock {

    RespawnBlocks instance;
    private final int x;

    private final int y;

    private final int z;
    //the block it should turn in to, when its "regenerating"
    private Material cooldownMaterial;

    private final int blockID;
    private final Block block;

    private int cooldownTime;

    private final World world;
    //the block's initial material before changing
    private  String initialBlockMaterial;


    /**
     * Creates a Respawnable Block using the parameterized data.
     * @param blockLocation the location of the block
     * @param respawnBlocksInstance the instance of this plugin
     * @param initalMaterial Inital material of the block
     * @param world the world that the current block is in
     */
    public RespawnBlock(Location blockLocation, RespawnBlocks respawnBlocksInstance, String initalMaterial, String world){
        this.x = blockLocation.getBlockX();
        this.y = blockLocation.getBlockY();
        this.z = blockLocation.getBlockZ();
        this.instance = respawnBlocksInstance;
        this.world = Bukkit.getWorld(world);
        block = this.world.getBlockAt(x,y,z);
        initialBlockMaterial = initalMaterial;
        loadSettingsFromConfig(instance.getDataConfig().getRBSection() + "." + this.initialBlockMaterial);
        this.blockID = respawnBlocksInstance.getRBManager().getInitalBlockID();
        instance.getRBManager().setInitalBlockID(instance.getRBManager().getInitalBlockID() + 1);
    }

    /**
     * This constructor is used when getting the data from the database.
     * @param xCoord the x Coordinate of the block
     * @param yCoord the y coordinate of the block
     * @param zCoord the z coordinate of the block
     * @param instance the instance of this plugin
     * @param blockID the block id, its unique identifier
     * @param world the world that this block is currently in
     * @param initialBlockMaterial the inital material the block was made of.
     */
    public RespawnBlock(int xCoord, int yCoord, int zCoord, RespawnBlocks instance, int blockID, String world, String initialBlockMaterial) {
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
        this.instance = instance;
        this.blockID = blockID;
        this.world = Bukkit.getWorld(world);
        this.block = this.world.getBlockAt(xCoord, yCoord, zCoord);
        this.initialBlockMaterial = initialBlockMaterial;
        loadSettingsFromConfig(instance.getDataConfig().getRBSection() + "." + this.initialBlockMaterial);
        instance.getRBManager().setInitalBlockID(instance.getRBManager().getInitalBlockID() + 1);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Material getCooldownMaterial() {
        return cooldownMaterial;
    }

    public void setCooldownMaterial(Material material){
        cooldownMaterial = material;
    }

    public Material getInitialBlockType(){
        return Material.getMaterial(initialBlockMaterial);
    }


    public int getBlockID() {
        return blockID;
    }

    public Block getBlock() {
        return block;
    }

    public World getWorld() {
        return world;
    }

    public void setInitialBlockMaterial(Material initialBlockMaterial) {
        this.initialBlockMaterial = initialBlockMaterial.toString();
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    /**
     * for respawnblocks, it will get the cooldown time and cooldown material for the block from the data config.
     * @param path the path to check for the respawnBlock.
     */
    private void restoreRBSettingsViaConfig(String path){
        if (instance.getDataConfig().getConfig().getConfigurationSection(path) == null) {
            instance.getDataConfig().getConfig().createSection(path, new HashMap<>());
        }
        ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(path);
        if (section != null) {
            if (instance.getDataConfig().getConfig().get(path + "." + "cooldown-time") == null) {
                MemorySection.createPath(section, "cooldown-time");
                section.set("cooldown-time", instance.getDataConfig().getDefaultCooldown());
                this.cooldownTime = instance.getDataConfig().getDefaultCooldown();
            } else {
                this.cooldownTime = instance.getDataConfig().getConfig().getInt(path + "." + "cooldown-time");
            }
            if (instance.getDataConfig().getConfig().getString(path + "." + "cooldown-block-material") == null) {
                MemorySection.createPath(section, "cooldown-block-material");
                section.set("cooldown-block-material", instance.getDataConfig().getDefaultCooldownMaterial().toString());
                this.cooldownMaterial = instance.getDataConfig().getDefaultCooldownMaterial();
            } else {
                this.cooldownMaterial = Material.getMaterial(instance.getDataConfig().getConfig().getString(path+ "." + "cooldown-block-material"));
            }
            instance.getDataConfig().save();
        }
    }
    /**
     * Loads the block's settings from the configuration file.
     *
     * @param path the path to the block's configuration section in the file
     */
    private void loadSettingsFromConfig(String path) {
        ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(path);
        if (section == null) {
            section = instance.getDataConfig().getConfig().createSection(path, new HashMap<>());
        }

        this.cooldownTime = Objects.requireNonNullElse(
                section.getInt("cooldown-time"),
                instance.getDataConfig().getDefaultCooldown()
        );

        this.cooldownMaterial = Material.getMaterial(
                Objects.requireNonNullElse(
                        section.getString("cooldown-block-material"),
                        instance.getDataConfig().getDefaultCooldownMaterial().toString()
                )
        );

        section.addDefault("cooldown-time", instance.getDataConfig().getDefaultCooldown());
        section.addDefault("cooldown-block-material", instance.getDataConfig().getDefaultCooldownMaterial().toString());

        instance.getDataConfig().save();
    }

}
