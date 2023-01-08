package me.afatcookie.respawnblocks.respawnblocks.timing;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;

import java.util.ArrayList;
/*
Manages blocks in cooldown
 */
public class TimerManager {

    //Arraylist of blocks currently in cooldown
    private final ArrayList<RespawnBlock> coolDownList;

    public TimerManager(RespawnBlocks instance){
        coolDownList = new ArrayList<>();
    }

    public boolean isInCoolDown(RespawnBlock respawnBlock){
        return coolDownList.contains(respawnBlock);
    }

    public ArrayList<RespawnBlock> getCoolDownList() {
        return coolDownList;
    }
}
