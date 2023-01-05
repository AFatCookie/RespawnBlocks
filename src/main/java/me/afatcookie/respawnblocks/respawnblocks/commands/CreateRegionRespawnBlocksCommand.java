package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateRegionRespawnBlocksCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "createRegion";
    }

    @Override
    public String getDescription() {
        return "Creates a the region selected into RespawnBlocks";
    }

    @Override
    public String getSyntax() {
        return "/rb createRegion";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
            if (instance.getRBManager().getBlock1() == null || instance.getRBManager().getBlock2() == null){
                commandSender.sendMessage(ChatColor.RED + "Please reset your positions, one of them are null!");
                return;
            }
            List<Block> blocks = getBlocks(instance.getRBManager().getBlock1().getLocation(), instance.getRBManager().getBlock2().getLocation());
            if (blocks == null || blocks.isEmpty()){
                player.sendMessage(ChatColor.RED + "Failed to create RespawnBlocks within the two points provided.");
                return;
            }
      for (Block block : blocks) {
        if (instance.getRBManager().getRespawnBlock(block) == null) {
            if (instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection() + "." + block.getType().toString()) == null){
                instance.getDataConfig().getConfig().createSection(instance.getDataConfig().getRBSection() + "." + block.getType().toString(), new HashMap<>());
                ConfigurationSection section = instance.getDataConfig().getConfig().getConfigurationSection(instance.getDataConfig().getRBSection() + "." + block.getType().toString());
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
                  block.getX(),
                  block.getY(),
                  block.getZ(),
                  instance,
                  block.getType().toString(),
                  block.getWorld().getName());
          instance.getRBManager().getRespawnBlocksList().add(respawnBlock);
        }
}
        }
    }

    private List<Block> getBlocks(Location pos1, Location pos2){
        if(pos1.getWorld() != pos2.getWorld()) return null;
        World world = pos1.getWorld();
        List<Block> blocks = new ArrayList<>();
        int x1 = pos1.getBlockX();
        int y1 = pos1.getBlockY();
        int z1 = pos1.getBlockZ();

        int x2 = pos2.getBlockX();
        int y2 = pos2.getBlockY();
        int z2 = pos2.getBlockZ();

        int lowestX = Math.min(x1, x2);
        int lowestY = Math.min(y1,y2);
        int lowestZ = Math.min(z1, z2);

        int highestX = lowestX == x1 ? x2 : x1;
        int highestY = lowestY == y1 ? y2 : y1;
        int highestZ = lowestZ == z1 ? z2 : z1;

        for (int x = lowestX; x <= highestX; x++){
            for (int y= lowestY; y <= highestY; y++){
                for (int z = lowestZ; z <= highestZ; z++){
                    blocks.add(world.getBlockAt(x,y,z));
                }
            }
        }
        return blocks;
    }
}
