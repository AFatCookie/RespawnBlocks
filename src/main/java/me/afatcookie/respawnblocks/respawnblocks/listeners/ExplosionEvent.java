package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionEvent implements Listener {

    private final RespawnBlocks instance;

    public ExplosionEvent(RespawnBlocks instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> instance.getRBManager().getRespawnBlock(block) != null);
    }
}

