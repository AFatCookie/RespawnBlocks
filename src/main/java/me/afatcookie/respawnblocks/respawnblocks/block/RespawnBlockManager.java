package me.afatcookie.respawnblocks.respawnblocks.block;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.utils.ItemStackSerializer;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/*
Manager for respawn blocks
 */
public class RespawnBlockManager {

    //Arraylist of all respawnable blocks
    private final ArrayList<RespawnBlock> respawnBlocks;

    private final RespawnBlocks instance;

    private int initalBlockID;

    private Block block1;

    private Block block2;

    public RespawnBlockManager(RespawnBlocks instance){
        respawnBlocks = new ArrayList<>();
        this.instance = instance;
        initalBlockID = 1000;

        }

    public ArrayList<RespawnBlock> getRespawnBlocksList() {
        return respawnBlocks;
    }


    public void clearBlocks(){
        instance.getDataConfig().getConfig().set("respawnblocks", new HashMap<>());
        instance.getDataConfig().save();
        instance.getDataConfig().reload();
        respawnBlocks.clear();
    }

    /**
     * Checks to see if the block parameterized matches any blocks within the respawnBlocks List.
     * @param block The block to check
     * @return returns the respawnBlock object if any match; Null otherwise
     */
    public RespawnBlock getRespawnBlock(Block block){
        for (RespawnBlock respawnBlock : respawnBlocks) {
      if (respawnBlock.getBlock().getType() != block.getType()) {
        continue;
                }
            if (respawnBlock.getX() != block.getLocation().getX()){
                continue;
            }
            if (respawnBlock.getY() != block.getLocation().getY()) {
                continue;
            }
            if (respawnBlock.getZ() != block.getZ()) {
                continue;
            }
            return respawnBlock;
        }
        return null;
    }

    /**
     * Initially clears the previous blocks in the table, and then will save the current respawn blocks. It doesn't matter
     * if the server crashes or ids change or whatever, these blocks have their ids, and their ids will always be accessible.
     */
    public void saveRespawnBlocks(){
        instance.getDatabase().clearTable("activeblocks");
        instance.getDatabase().clearTable("idtable");
        instance.getDatabase().clearTable("rewards");
        if (respawnBlocks.isEmpty()) return;
        for (RespawnBlock respawnBlock : respawnBlocks){
            instance.getDatabase().saveActiveBlocksToTable(respawnBlock);
            instance.getDatabase().saveIDSToTable(respawnBlock);
            saveRewards(respawnBlock);
        }

    }

    private void saveRewards(RespawnBlock respawnBlock){
        for (Reward reward : respawnBlock.getRewards()){
            byte[] serializedItem = ItemStackSerializer.serialize(reward.getItem());
            if (serializedItem != null){
                instance.getDatabase().saveRewardsToTable(respawnBlock, ItemStackSerializer.serialize(reward.getItem()));
            }
        }
    }

    /**
     * First tries to get all saved ids from when saving before closing of server. If the ArrayList of ids is empty, meaning
     * nothing was saved, then it won't look for any blocks; Otherwise it will get the respawn block corresponding to the
     * blockID.
     */
    public void retrieveRespawnBlocks(){
        ArrayList<Integer> allIDS = instance.getDatabase().getBlockIDS();
        if (allIDS.isEmpty()) return;
        initalBlockID = Collections.max(allIDS) + 1;
        for (int id : allIDS) {
            respawnBlocks.add(instance.getDatabase().getActiveBlocks(id));
        }
        for (RespawnBlock respawnBlock : respawnBlocks){
            respawnBlock.setRewards(instance.getDatabase().getRewardsFromDB(respawnBlock.getBlockID()));
        }
    }

    public int getInitalBlockID() {
        return initalBlockID;
    }

    public void setInitalBlockID(int newBlockID){
        initalBlockID = newBlockID;
    }

    public Block getBlock1() {
        return block1;
    }

    public void setBlock1(Block block1) {
        this.block1 = block1;
    }

    public Block getBlock2() {
        return block2;
    }

    public void setBlock2(Block block2) {
        this.block2 = block2;
    }
}
