package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import org.bukkit.ChatColor;
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
        if (args.length > 0) {
      for (RespawnBlock rb : instance.getRBManager().getRespawnBlocksList()) {
        if (instance.getTm().isInCoolDown(rb)) {
          rb.getWorld().getBlockAt(rb.getX(), rb.getY(), rb.getZ()).setType(rb.getInitialBlockType());
          instance.getTm().getCoolDownList().remove(rb);
        }
            }
      instance.getRBManager().clearBlocks();
      commandSender.sendMessage(ChatColor.GREEN + "Cleared all active RespawnBlocks!");
        }
    }
}
