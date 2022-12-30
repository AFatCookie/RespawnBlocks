package me.afatcookie.respawnblocks.respawnblocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

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

    private final World world;
    //the block's initial material before changing
    private final Material initialBlockMaterial;


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
        if (Material.getMaterial(instance.getDataConfig().getConfig().getString("cooldown-block")) == null){
            this.coolDownBlock = Material.STONE;
        }
        this.coolDownBlock = Material.getMaterial(instance.getDataConfig().getConfig().getString("cooldown-block"));
        this.blockID = respawnBlocksInstance.getRBManager().getInitalBlockID();
        respawnBlocksInstance.getRBManager().setInitalBlockID(respawnBlocksInstance.getRBManager().getInitalBlockID() + 1);
    }

    public RespawnBlock(int xCoord, int yCoord, int zCoord, RespawnBlocks instance, int blockID, String world, String initialBlockMaterial) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.instance = instance;
        this.blockID = blockID;
        this.world = Bukkit.getWorld(world);
        this.block = this.world.getBlockAt(xCoord,yCoord,zCoord);
        this.initialBlockMaterial = Material.getMaterial(initialBlockMaterial);
        if (!block.getType().equals(this.initialBlockMaterial)){
            block.setType(this.initialBlockMaterial);
        }
        if (Material.getMaterial(instance.getDataConfig().getConfig().getString("cooldown-block")) == null){
            this.coolDownBlock = Material.STONE;
        }
        this.coolDownBlock = Material.getMaterial(instance.getDataConfig().getConfig().getString("cooldown-block"));
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
}
