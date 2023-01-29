package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.block.RespawnBlock;
import me.afatcookie.respawnblocks.respawnblocks.files.DataConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
                instance.getDataConfig().removeDangling();
                for (RespawnBlock rb : instance.getRBManager().getRespawnBlocksList()) {
                    if (instance.getTm().isInCoolDown(rb)) {
                        rb.getWorld().getBlockAt(rb.getX(), rb.getY(), rb.getZ()).setType(rb.getInitialBlockType());
                        instance.getTm().getCoolDownList().remove(rb);
                    }

                    initRBFields(rb, instance.getDataConfig());
                }





                    commandSender.sendMessage(ChatColor.GREEN + "Successfully reloaded RespawnableBlocks!");
            }catch (IllegalArgumentException | NullPointerException exception) {
                commandSender.sendMessage(ChatColor.RED + "Failed to reload RespawnableBlocks!");
                commandSender.sendMessage( ChatColor.RED + "Exception is in console.");
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
        rb.setCooldownTime(dataConfig.getBlockTime(rb.getInitialBlockType()));
        rb.setCooldownMaterial(dataConfig.getBlockCDMaterial(rb.getInitialBlockType()));
        // Save the configuration file
        dataConfig.save();
    }

}
