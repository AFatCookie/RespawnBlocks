package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearAllBlocksCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "clearAll";
    }

    @Override
    public String getDescription() {
        return "Removes all RespawnBlocks";
    }

    @Override
    public String getSyntax() {
        return "/rb clearAll";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
      for (RespawnBlock rb : instance.getRBManager().getRespawnBlocksList()) {
        if (instance.getTm().isInCoolDown(rb)) {
          rb.getWorld().getBlockAt(rb.getxCoord(), rb.getyCoord(), rb.getzCoord()).setType(rb.getInitialBlockType());
        }
            }
      instance.getRBManager().clearBlocks();
      commandSender.sendMessage("Cleared all active RespawnBlocks!");
        }
    }
}
