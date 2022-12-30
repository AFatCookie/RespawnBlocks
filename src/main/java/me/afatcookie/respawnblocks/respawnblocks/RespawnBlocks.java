package me.afatcookie.respawnblocks.respawnblocks;

import me.afatcookie.respawnblocks.respawnblocks.DB.Database;
import me.afatcookie.respawnblocks.respawnblocks.DB.SQLite;
import me.afatcookie.respawnblocks.respawnblocks.files.DataConfig;
import me.afatcookie.respawnblocks.respawnblocks.listeners.BlockBreakListener;
import me.afatcookie.respawnblocks.respawnblocks.listeners.ClickListener;
import me.afatcookie.respawnblocks.respawnblocks.listeners.CommandListener;
import me.afatcookie.respawnblocks.respawnblocks.listeners.GUIListener;
import me.afatcookie.respawnblocks.respawnblocks.timing.TimerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RespawnBlocks extends JavaPlugin {

    private static RespawnBlocks instance;

    private static DataConfig dataConfig;

    private RespawnBlockManager rbManager;

    private Database database;

    private TimerManager tm;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        database = new SQLite(instance);
        database.createBlockIDTable();
        database.createActiveBlocksTable();
        dataConfig = new DataConfig(instance);
        rbManager = new RespawnBlockManager(instance);
        tm = new TimerManager(instance);
        rbManager.retrieveRespawnBlocks();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        rbManager.saveRespawnBlocks();
    }

    public static RespawnBlocks getInstance(){
        return instance;
    }

    public RespawnBlockManager getRBManager(){
        return rbManager;
    }

    public TimerManager getTm(){
        return tm;
    }

    /**
     * registers the listeners
     */
    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new ClickListener(this), this);
    }

    public  DataConfig getDataConfig() {
        return dataConfig;
    }

    public Database getDatabase() {
        return database;
    }

    private void registerCommands(){
        CommandListener.commandList(this);
    }
}
