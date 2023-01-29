package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlockManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockFormChangeListener implements Listener {

    private final RespawnBlockManager rm;

     public BlockFormChangeListener(RespawnBlocks instance){
        rm = instance.getRBManager();
    }

    @EventHandler
    public void blockFormChangeEvent(BlockFormEvent e){
        Block block = e.getBlock();
        if (rm.getRespawnBlock(block) != null){
            RespawnBlock respawnBlock = rm.getRespawnBlock(block);
            if (respawnBlock.getInitialBlockType() == Material.DIRT){
                e.setCancelled(true);
            }
            if (respawnBlock.getInitialBlockType().toString().contains("POWDER")){
                e.setCancelled(true);
            }
        }
    }

}
