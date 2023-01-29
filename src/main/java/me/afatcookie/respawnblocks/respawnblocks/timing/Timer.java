package me.afatcookie.respawnblocks.respawnblocks.timing;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlockManager;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.Reward;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;


public class Timer extends BukkitRunnable {
    private final RespawnBlockManager respawnBlockManager;
    private final Block respawnableBlock;
    private final RespawnBlock respawnBlock;
    private final RespawnBlocks instance;
    private int time;

    public Timer(Block respawnableBlock, RespawnBlock respawnBlock, RespawnBlocks instance) {
        this.respawnableBlock = respawnableBlock;
        this.respawnBlock = respawnBlock;
        this.instance = instance;
        this.respawnBlockManager = instance.getRBManager();
        this.time = respawnBlock.getCooldownTime();
    }

    @Override
    public void run() {
        time--;
        for (Reward rwe : respawnBlock.getRewards()) {
            System.out.println(rwe.getItem().getType().toString()  + ":" + rwe.getItem().getAmount() + "  1");

        }
        if (time <= 0) {
            if (respawnBlockManager.getRespawnBlocksList().contains(respawnBlock)) {
                respawnableBlock.setType(respawnBlock.getInitialBlockType());
                instance.getTm().getCoolDownList().remove(respawnBlock);
            }
            this.cancel();
        }
    }
}