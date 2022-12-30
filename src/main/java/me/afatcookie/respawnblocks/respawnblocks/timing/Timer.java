package me.afatcookie.respawnblocks.respawnblocks.timing;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlockManager;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {
    private final RespawnBlockManager rbManager;

    private final Block respawnableBlock;

    private final RespawnBlock respawnBlock;

    private final RespawnBlocks instance;
    private int time;

    public Timer(Block respawnBlock, RespawnBlock respawnBlockConstructor, RespawnBlocks respawnBlocksInstance){
        this.respawnableBlock = respawnBlock;
        this.respawnBlock = respawnBlockConstructor;
        this.instance = respawnBlocksInstance;
        rbManager = instance.getRBManager();
        if (instance.getDataConfig().getConfig().getInt("block-cooldown-time") <= 0){
            time = 5;
        }
        time = instance.getDataConfig().getConfig().getInt("block-cooldown-time");
    }

    //Runs every second, checking if It's time to set the block back to its original form. Will remove it from cooldown automatically.
    @Override
    public void run() {
        time--;
        if (time <= 0){
      if (rbManager.getRespawnBlocksList().contains(respawnBlock)) {
        respawnableBlock.setType(respawnBlock.getInitialBlockType());
        instance.getTm().getCoolDownList().remove(respawnBlock);
        this.cancel();
            }
      this.cancel();
        }
    }
}
