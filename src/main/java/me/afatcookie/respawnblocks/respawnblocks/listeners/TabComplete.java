package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.commands.CommandBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            for (CommandBuilder cmd : CommandListener.getCommands()) {
                suggestions.add(cmd.getName());
            }
        }
        return suggestions;
    }

    public static void register(RespawnBlocks instance, String commandName) {
        TabComplete tabComplete = new TabComplete();
        instance.getCommand(commandName).setTabCompleter(tabComplete);
    }
}
