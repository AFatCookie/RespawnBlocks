package me.afatcookie.respawnblocks.respawnblocks.block;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

    private  ArrayList<Reward> rewards;


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
        loadRewards();
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
        loadRewards();
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

    private void loadRewards(){
        rewards = new ArrayList<>();
        if (block.getDrops().stream().findFirst().isPresent()){
            rewards.add(new Reward(blockID, block.getDrops().stream().findFirst().get()));
        }else{
            rewards.add(new Reward(blockID, new ItemStack(block.getType(), 1)));
        }
        }

    public void dropRewards(Player player){
        for (Reward reward: rewards) {
            player.getWorld().dropItemNaturally(block.getLocation(), reward.getItem());
        }
    }

    public void addToRewards(ItemStack itemStack){
        rewards.add(new Reward(blockID, itemStack));
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }
}
