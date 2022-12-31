package me.afatcookie.respawnblocks.respawnblocks.commands;

import me.afatcookie.respawnblocks.respawnblocks.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Gives the player a wand which can be used to create/stop blocks respawning.
 */
public class GiveBlockChangerCommand extends CommandBuilder{
    @Override
    public String getName() {
        return "wand";
    }

    @Override
    public String getDescription() {
        return "Gives the player a wand which can be used to create respawnable blocks.";
    }

    @Override
    public String getSyntax() {
        return "/rb wand";
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) return;
        Player player = (Player) commandSender;
        if (args.length > 0) {
            player.getInventory().addItem(new ItemCreator(Material.BLAZE_ROD, 1).setDisplayName("&6RB&r &6&nWand").addGlow(true).addLoreLine(
                    "&6This wand is used to set points to make respawn blocks in mass fashion!").addLoreLine("&4RB ADMIN WAND")
                    .setPDCString(new NamespacedKey(instance, "wand"), "rbwand").getItemStack());
        }
    }
}
