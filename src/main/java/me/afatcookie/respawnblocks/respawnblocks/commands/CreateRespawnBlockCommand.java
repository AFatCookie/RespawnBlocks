package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Creates a respawnableBlock, when looking at it within 5 blocks
 */
public class CreateRespawnBlockCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Creates the block you are directly looking at, into a respawn block!";
    }

    @Override
    public String getSyntax() {
        return "/rb create";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof  Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
          Block targetBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
          if (targetBlock == null){
              player.sendMessage(ChatColor.RED + "Failed to create a respawn block.");
              return;
          }
          if (instance.getRBManager().getRespawnBlock(targetBlock) != null){
              player.sendMessage(ChatColor.RED + "This block is already a respawn block!");
              return;
          }
          if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection()) == null){
              player.sendMessage(ChatColor.RED + "a section is missing from the config, please delete the data.yml file, and then reload the plugin fully.");
              player.sendMessage(ChatColor.RED + "or, try and create a configuration section named: " + " respawnblocks ");
              return;
          }
            if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString()) == null){
                instance.getDataConfig().getConfig().createSection(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString(), new HashMap<>());
                ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection() + "." + targetBlock.getType().toString());
                if (section != null) {
                    MemorySection.createPath(section, "cooldown-time");
                    MemorySection.createPath(section, "cooldown-block-material");
                    section.set("cooldown-time", 10);
                    section.set("cooldown-block-material", "COBBLESTONE");
                }
                instance.getDataConfig().save();
            }
      RespawnBlock respawnBlock =
          new RespawnBlock(
                  targetBlock.getLocation(),
              instance, targetBlock.getType().toString(),
              targetBlock.getWorld().getName());
          instance.getRBManager().getRespawnBlocksList().add(respawnBlock);
          player.sendMessage(ChatColor.GREEN + "Successfully created a respawn block!");
        }
    }
}
