package me.afatcookie.respawnblocks.respawnblocks.files;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class DataConfig {

    private File file;

    private FileConfiguration fileConfiguration;

    private final RespawnBlocks instance;

    public DataConfig(RespawnBlocks instance) {
        this.instance = instance;
        setup();

        getConfig().addDefault("default-block-cooldown-time", 10);
        getConfig().addDefault("default-cooldown-block", "COBBLESTONE");
        if (getConfig().getConfigurationSection(getRBSectionName()) == null) {
            getConfig().createSection(getRBSectionName());
        }
        getConfig().options().copyDefaults(true);
        save();
    }

    public void setup() {
        file = new File(this.instance.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Something went wrong in " + DataConfig.class.getName());
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save data to Data Config", e);
        }
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }


    public int getDefaultCooldown() {

        return getConfig().getInt("default-block-cooldown-time");
    }

    public Material getDefaultCooldownMaterial() {
        return Material.getMaterial(Objects.requireNonNull(getConfig().getString("default-cooldown-block")));
    }

    public String getRBSectionName() {
        return "respawnblocks";
    }

    public ConfigurationSection getRBConfigSection(){
        return getConfig().getConfigurationSection(getRBSectionName());
    }

    public void clearSection(ConfigurationSection section) {
        if (section != null) {
            // Get the parent section
            ConfigurationSection parent = section.getParent();

            // Remove the section
            if (parent != null) {
                parent.set(section.getName(), null);
            } else {
                getConfig().set(section.getName(), null);
            }

            // Save the config
            save();
        }
    }

    public void addCDInfoToConfig(Material material){
        addCDBlockInfoToConfig(material);
        addCDTimeInfoToConfig(material, getDefaultCooldown());
        save();
    }

    public void addCDBlockInfoToConfig(Material material){
        if (!getRBConfigSection().contains(material + "-" + "cooldown" + "-" + "material")) {
            MemorySection.createPath(getRBConfigSection(), material + "-" + "cooldown" + "-" + "material");
            getRBConfigSection().set(material + "-" + "cooldown" + "-" + "material", getDefaultCooldownMaterial().toString());
        }
        save();
    }

    public void addCDTimeInfoToConfig(Material material, int time){
        if (!getRBConfigSection().contains(material.toString() + "-" + "cooldown" + "-" + "time")) {
            MemorySection.createPath(getRBConfigSection(), material + "-" + "cooldown" + "-" + "time");
            getRBConfigSection().set(material + "-" + "cooldown" + "-" + "time", time);
        }
        save();
    }

    public int getBlockTime(Material material){
        if (getRBConfigSection().getInt(material + "-" + "cooldown" + "-" + "time") <= 0){
            return 10;
        }
        try {

            return Objects.requireNonNullElse(
                    getRBConfigSection().getInt(material + "-" + "cooldown" + "-" + "time"),
                    instance.getDataConfig().getDefaultCooldown()
            );
        }catch (NullPointerException e){
            addCDTimeInfoToConfig(material, 10);
        }
        return 10;
    }

    public Material getBlockCDMaterial(Material material ){
        try {

            return Material.getMaterial(
                    Objects.requireNonNullElse(
                            getRBConfigSection().getString(material + "-" + "cooldown" + "-" + "material"),
                            instance.getDataConfig().getDefaultCooldownMaterial().toString()
                    )
            );
        }catch (NullPointerException e){
            addCDBlockInfoToConfig(material);
        }
        return material;
    }

    public void removeBlockFromConfig(Material material){
       getConfig().set(getRBSectionName() + "." + material + "-" + "cooldown" + "-" + "material", null);
       getConfig().set(getRBSectionName() + "." +material + "-" + "cooldown" + "-" + "time", null);
       save();
    }

    public void removeDangling(){
        for (String key : getRBConfigSection().getKeys(false)) {
            if (key.contains("-cooldown-material")) {
                String testMaterial = key.split("-cooldown-material")[0];
                try {
                    Material.valueOf(testMaterial);
                }catch (IllegalArgumentException | NullPointerException e){
                    getRBConfigSection().set(key, null);
                }
            }else if (key.contains("-cooldown-time")){
                String testMaterial = key.split("-cooldown-time")[0];
                try {
                    Material.valueOf(testMaterial);
                }catch (IllegalArgumentException | NullPointerException e){
                    getRBConfigSection().set(key, null);
                }
            }else{
                getRBConfigSection().set(key, null);
            }
        }
        save();
    }





}
