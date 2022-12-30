package me.afatcookie.respawnblocks.respawnblocks.files;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataConfig {

    private File file;

    private FileConfiguration fileConfiguration;

    private final RespawnBlocks instance;

    public DataConfig(RespawnBlocks instance) {
        this.instance = instance;
        setup();
        getConfig().addDefault("block-cooldown-time", 10);
        getConfig().addDefault("cooldown-block", "COBBLESTONE");
        getConfig().options().copyDefaults(true);
        save();
    }

    public void setup(){
        file = new File(this.instance.getDataFolder(), "data.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong in " + DataConfig.class.getName());
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig(){
        return fileConfiguration;
    }

    public void save(){
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save data to Data Config", e);
        }
    }

    public void reload(){
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
