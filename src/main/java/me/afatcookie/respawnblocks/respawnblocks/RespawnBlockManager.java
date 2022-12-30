package me.afatcookie.respawnblocks.respawnblocks;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/*
Manager for respawn blocks
 */
public class RespawnBlockManager {

    //Arraylist of all respawnable blocks
    private final ArrayList<RespawnBlock> respawnBlocks;

    private final RespawnBlocks instance;

    private int initalBlockID;

    public RespawnBlockManager(RespawnBlocks instance){
        respawnBlocks = new ArrayList<>();
        this.instance = instance;
        initalBlockID = 1000;

        }

    public ArrayList<RespawnBlock> getRespawnBlocksList() {
        return respawnBlocks;
    }


    public void clearBlocks(){
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
            if (respawnBlock.getxCoord() != block.getLocation().getX()){
                continue;
            }
            if (respawnBlock.getyCoord() != block.getLocation().getY()) {
                continue;
            }
            if (respawnBlock.getzCoord() != block.getZ()) {
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
        if (respawnBlocks.isEmpty()) return;
        for (RespawnBlock respawnBlock : respawnBlocks){
            instance.getDatabase().saveActiveBlocksToTable(respawnBlock);
            instance.getDatabase().saveIDSToTable(respawnBlock);
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
    }

    public int getInitalBlockID() {
        return initalBlockID;
    }

    public void setInitalBlockID(int newBlockID){
        initalBlockID = newBlockID;
    }
}