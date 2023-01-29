package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        // Check if the command sender is a player
        if (!(commandSender instanceof Player)) {
            return;
        }
        Player player = (Player) commandSender;
        // Check if any arguments were passed to the command
        if (args.length > 0) {
            // check if the block1 and block2 are not null
            if (instance.getRBManager().getBlock1() == null || instance.getRBManager().getBlock2() == null) {
                commandSender.sendMessage(ChatColor.RED + "Please reset your positions, one of them are null!");
                return;
            }
            //get all blocks between two points
            List<Block> blocks = getBlocks(instance.getRBManager().getBlock1().getLocation(), instance.getRBManager().getBlock2().getLocation());
            if (blocks == null || blocks.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Failed to create RespawnBlocks within the two points provided.");
                return;
            }
            for (Block block : blocks) {
                // check if the block is not already a respawn block
                if (instance.getRBManager().getRespawnBlock(block) == null) {
                    // check if the block is already in the config
                    instance.getDataConfig().addCDInfoToConfig(block.getType());
                    //create new respawn block
                    RespawnBlock respawnBlock =
                            new RespawnBlock(
                                    block.getLocation(),
                                    instance,
                                    block.getType().toString(),
                                    block.getWorld().getName());
                    instance.getRBManager().getRespawnBlocksList().add(respawnBlock);
                }
            }
        }
    }



    /**
     * Returns a list of blocks within a specified bounding box.
     *
     * @param pos1 the first corner of the bounding box
     * @param pos2 the second corner of the bounding box
     * @return a list of blocks within the bounding box, or null if the positions are in different worlds
     */
    private List<Block> getBlocks(Location pos1, Location pos2) {
        // Return null if the positions are in different worlds
        if (pos1.getWorld() != pos2.getWorld()) return null;

        World world = pos1.getWorld();
        List<Block> blocks = new ArrayList<>();

        // Get the coordinates of the positions
        int x1 = pos1.getBlockX();
        int y1 = pos1.getBlockY();
        int z1 = pos1.getBlockZ();
        int x2 = pos2.getBlockX();
        int y2 = pos2.getBlockY();
        int z2 = pos2.getBlockZ();

        // Find the lowest and highest coordinates
        int lowestX = Math.min(x1, x2);
        int lowestY = Math.min(y1,y2);
        int lowestZ = Math.min(z1, z2);
        int highestX = lowestX == x1 ? x2 : x1;
        int highestY = lowestY == y1 ? y2 : y1;
        int highestZ = lowestZ == z1 ? z2 : z1;

        // Iterate through all blocks within the bounding box
        for (int x = lowestX; x <= highestX; x++) {
            for (int y = lowestY; y <= highestY; y++) {
                for (int z = lowestZ; z <= highestZ; z++) {
                    blocks.add(world.getBlockAt(x,y,z));
                }
            }
        }
        return blocks;
    }

}
