package me.afatcookie.respawnblocks.respawnblocks.DB;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;

import java.util.logging.Level;

public class Error {
    public static void execute(RespawnBlocks plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(RespawnBlocks plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
