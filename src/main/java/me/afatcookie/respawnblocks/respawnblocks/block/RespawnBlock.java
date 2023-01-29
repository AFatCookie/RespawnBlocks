package me.afatcookie.respawnblocks.respawnblocks.block;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
This class just creates a respawnable Block, so I can manage them through the code
 */
public class RespawnBlock {

    RespawnBlocks instance;
    private final int x;
    private final int y;
    private final int z;
    private final int blockID;
    private int cooldownTime;
    //the block it should turn in to, when its "regenerating"
    private Material cooldownMaterial;

    private final Block block;
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
        loadSettingsFromConfig();
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
        loadSettingsFromConfig();
        loadRewards();
        instance.getRBManager().setInitalBlockID(instance.getRBManager().getInitalBlockID() + 1);
    }
    private void loadSettingsFromConfig() {
        this.cooldownTime = instance.getDataConfig().getBlockTime(getInitialBlockType());
        this.cooldownMaterial = instance.getDataConfig().getBlockCDMaterial(getInitialBlockType());

        // Save the configuration file
        instance.getDataConfig().save();
    }


    private void loadRewards(){
        if (rewards != null) return;
        rewards = new ArrayList<>();
        //Attempts to add a new reward based on the blocks first drop, and if the block drops nothing, it'll drop itself. both will have a weight of 50.
        rewards.add(new Reward(blockID, block.getDrops().stream().findFirst().orElse(new ItemStack(block.getType(), 1)), 50));

    }

    public void dropRewards(RespawnBlock block, Player player){
// Get the weighted item reward from the block
        ItemStack itemStack = new WeightManager(block).getWeightedReward().getItem();

// If the item is air, drop the initial block type instead
        if (itemStack.getType() == Material.AIR) {
            player.getWorld().dropItemNaturally(block.getBlock().getLocation(), new ItemStack(block.getInitialBlockType(),1));
            return;
        }
// Otherwise, drop the weighted reward item
        player.getWorld().dropItemNaturally(block.getBlock().getLocation(), itemStack);

    }

    public void addToRewards(ItemStack itemStack, int weight){
        rewards.add(new Reward(blockID, itemStack, weight));
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

    public int getCooldownTime() {
        return cooldownTime;
    }
    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public void setInitialBlockMaterial(Material initialBlockMaterial) {
        this.initialBlockMaterial = initialBlockMaterial.toString();
    }

    public void setCooldownMaterial(Material material){
        cooldownMaterial = material;
    }


    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

}
