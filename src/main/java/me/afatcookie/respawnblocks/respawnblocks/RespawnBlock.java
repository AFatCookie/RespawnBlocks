package me.afatcookie.respawnblocks.respawnblocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;

/*
This class just creates a respawnable Block, so I can manage them through the code
 */
public class RespawnBlock {

    RespawnBlocks instance;
    private final int xCoord;

    private final int yCoord;

    private final int zCoord;
    //the block it should turn in to, when its "regenerating"
    private Material coolDownBlock;

    private final int blockID;
    private final Block block;

    private int cooldownTime;

    private final World world;
    //the block's initial material before changing
    private  Material initialBlockMaterial;


    public RespawnBlock(int xCoord, int yCoord, int zCoord, RespawnBlocks respawnBlocksInstance, String initalMaterial, String world){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.instance = respawnBlocksInstance;
        this.world = Bukkit.getWorld(world);
        block = this.world.getBlockAt(xCoord,yCoord,zCoord);
        initialBlockMaterial = Material.getMaterial(initalMaterial);
        if (!block.getType().equals(initialBlockMaterial)){
            block.setType(initialBlockMaterial);
        }
        restoreRBSettingsViaConfig();
        this.blockID = respawnBlocksInstance.getRBManager().getInitalBlockID();
        instance.getRBManager().setInitalBlockID(instance.getRBManager().getInitalBlockID() + 1);
    }

    public RespawnBlock(int xCoord, int yCoord, int zCoord, RespawnBlocks instance, int blockID, String world, String initialBlockMaterial) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.instance = instance;
        this.blockID = blockID;
        this.world = Bukkit.getWorld(world);
        this.block = this.world.getBlockAt(xCoord, yCoord, zCoord);
        this.initialBlockMaterial = Material.getMaterial(initialBlockMaterial);
        if (!block.getType().equals(this.initialBlockMaterial)) {
            block.setType(this.initialBlockMaterial);
        }
        restoreRBSettingsViaConfig();
        instance.getRBManager().setInitalBlockID(instance.getRBManager().getInitalBlockID() + 1);
    }

    public int getxCoord() {
        return xCoord;
    }
    public int getyCoord() {
        return yCoord;
    }

    public int getzCoord() {
        return zCoord;
    }

    public Material getCoolDownBlock() {
        return coolDownBlock;
    }

    public void setCoolDownBlock(Material material){
        coolDownBlock = material;
    }

    public Material getInitialBlockType(){
        return initialBlockMaterial;
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
        this.initialBlockMaterial = initialBlockMaterial;
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    private void restoreRBSettingsViaConfig(){
        String configPath = instance.getDataConfig().getRBSection() + "." + this.initialBlockMaterial.toString();
        if (instance.getDataConfig().getConfig().getConfigurationSection(configPath) == null) {
            System.out.println("Creating section");
            instance.getDataConfig().getConfig().createSection(configPath, new HashMap<>());
        }
        ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(configPath);
        if (section != null) {
            if (instance.getDataConfig().getConfig().get(configPath + "." + "cooldown-time") == null) {
                MemorySection.createPath(section, "cooldown-time");
                section.set("cooldown-time", instance.getDataConfig().getDefaultCooldown());
                this.cooldownTime = instance.getDataConfig().getDefaultCooldown();
            } else {
                this.cooldownTime = instance.getDataConfig().getConfig().getInt(configPath + "." + "cooldown-time");
            }
            if (instance.getDataConfig().getConfig().getString(configPath + "." + "cooldown-block-material") == null) {
                MemorySection.createPath(section, "cooldown-block-material");
                section.set("cooldown-block-material", instance.getDataConfig().getDefaultCooldownMaterial().toString());
                this.coolDownBlock = instance.getDataConfig().getDefaultCooldownMaterial();
            } else {
                this.coolDownBlock = Material.getMaterial(instance.getDataConfig().getConfig().getString(configPath+ "." + "cooldown-block-material"));
            }
            instance.getDataConfig().save();
        }
    }
}
