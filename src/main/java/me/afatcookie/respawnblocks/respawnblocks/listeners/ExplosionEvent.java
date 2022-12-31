package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import java.util.List;

public class ExplosionEvent implements Listener {

    RespawnBlocks instance;

    public ExplosionEvent(RespawnBlocks instance){
        this.instance = instance;
    }

    @EventHandler
    public void onExplodeEvent(EntityExplodeEvent e){
        List<Block> destroyedBlocks = e.blockList();
        destroyedBlocks.removeIf(block -> instance.getRBManager().getRespawnBlock(block) != null);
    }
}
