package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
import org.bukkit.ChatColor;
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
                        rb.getWorld().getBlockAt(rb.getxCoord(), rb.getyCoord(), rb.getzCoord()).setType(rb.getInitialBlockType());
                        instance.getTm().getCoolDownList().remove(rb);
                    }
                    String configPath = instance.getDataConfig().getRBSection() + "." + rb.getInitialBlockType().toString();
                        if (instance.getDataConfig().getConfig().getConfigurationSection(configPath) == null) {
                            instance.getDataConfig().getConfig().createSection(configPath, new HashMap<>());
                        }
                            ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(configPath);
                    if (section != null) {
                                if (instance.getDataConfig().getConfig().get(configPath + "." + "cooldown-time") == null) {
                                    MemorySection.createPath(section, "cooldown-time");
                                    section.set("cooldown-time", instance.getDataConfig().getDefaultCooldown());
                                }
                                if (instance.getDataConfig().getConfig().getString(configPath + "." + "cooldown-block-material") == null) {
                                    MemorySection.createPath(section, "cooldown-block-material");
                                    section.set("cooldown-block-material", instance.getDataConfig().getDefaultCooldownMaterial().toString());
                                }
                            }
                            instance.getDataConfig().save();
                }

                if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection()) != null){
                    ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection());
                    for (String itemAccessor : section.getKeys(false)) {
                        if (!section.isConfigurationSection(itemAccessor)) {
                            String newItem = instance.getDataConfig().getRBSection() + "." + itemAccessor;
                            System.out.println(newItem);
                            section.set(itemAccessor, null);
                            instance.saveConfig();
                        }
                    }
                }

            commandSender.sendMessage(ChatColor.GREEN + "Successfully reloaded RespawnableBlocks!");
            }catch (IllegalArgumentException | NullPointerException exception) {
                commandSender.sendMessage("Failed to reload RespawnableBlocks!");
                commandSender.sendMessage("Exception is in console.");
                exception.printStackTrace();
            }
        }
    }
}
