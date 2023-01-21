package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.files.DataConfig;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;

/**
 * Reloads the Configuration files for RespawnableBlocks.
 */
public class ReloadConfigCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reloads the RespawnableBlocks plugin!";
    }

    @Override
    public String getSyntax() {
        return "/rb reload";
    }



    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length > 0) {
            try{
            instance.getDataConfig().reload();
                for (RespawnBlock rb : instance.getRBManager().getRespawnBlocksList()) {
                    if (instance.getTm().isInCoolDown(rb)) {
                        rb.getWorld().getBlockAt(rb.getX(), rb.getY(), rb.getZ()).setType(rb.getInitialBlockType());
                        instance.getTm().getCoolDownList().remove(rb);
                    }

                    initRBFields(rb, instance.getDataConfig());
                }

                if (instance.getDataConfig().getConfig().getConfigurationSection("respawnblocks") != null){
                    ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection("respawnblocks");
                    for (String key : section.getKeys(false)) {
                        if (!section.isConfigurationSection(key.trim())) {
                            instance.getDataConfig().getConfig().set("respawnblocks." + key, null);
                        }
                    }
                    instance.getDataConfig().save();
                }




                    commandSender.sendMessage(ChatColor.GREEN + "Successfully reloaded RespawnableBlocks!");
            }catch (IllegalArgumentException | NullPointerException exception) {
                commandSender.sendMessage("Failed to reload RespawnableBlocks!");
                commandSender.sendMessage("Exception is in console.");
                exception.printStackTrace();
            }
        }
    }
    /**
     * Initializes the fields of an RBObject using values from a configuration file.
     *
     * @param rb the RBObject to initialize
     * @param dataConfig the DataConfig object containing the configuration file
     */
    private void initRBFields(RespawnBlock rb, DataConfig dataConfig) {
        // Get the configuration path for the RBObject
        String configPath = dataConfig.getRBSection() + "." + rb.getInitialBlockType().toString();

        // Create a new configuration section if one does not already exist
        if (dataConfig.getConfig().getConfigurationSection(configPath) == null) {
            dataConfig.getConfig().createSection(configPath, new HashMap<>());
        }

        // Get the configuration section for the RBObject
        ConfigurationSection section = dataConfig.getConfig().getConfigurationSection(configPath);
        if (section != null) {
            // Set the cooldown time for the RBObject

            int cooldownTime = dataConfig.getConfig().getInt(configPath + "." + "cooldown-time", dataConfig.getDefaultCooldown());
            rb.setCooldownTime(cooldownTime);
            if (!dataConfig.getConfig().contains(configPath + "." + "cooldown-time")){
                MemorySection.createPath(section, "cooldown-time");
                section.set("cooldown-time", 10);
            }

            // Set the cooldown material for the RBObject
            Material cooldownMaterial = Material.getMaterial(dataConfig.getConfig().getString(configPath + "." + "cooldown-block-material", dataConfig.getDefaultCooldownMaterial().toString()));
            rb.setCooldownMaterial(cooldownMaterial);
            if (!dataConfig.getConfig().contains(configPath + "." + "cooldown-block-material")){
                MemorySection.createPath(section, "cooldown-block-material");
                section.set("cooldown-block-material", "COBBLESTONE");
            }
        }

        // Save the configuration file
        dataConfig.save();
    }

}
