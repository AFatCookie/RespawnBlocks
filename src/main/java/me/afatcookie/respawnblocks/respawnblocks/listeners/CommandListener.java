package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.commands.*;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandListener implements CommandExecutor {

    private final static ArrayList<CommandBuilder> commandsArrayList = new ArrayList<>();

    public CommandListener(){
        commandsArrayList.add(new CreateRespawnBlockCommand());
        commandsArrayList.add(new OpenBlocksGUICommand());
        commandsArrayList.add(new RespawnBlockRemoveCommand());
        commandsArrayList.add(new GiveBlockChangerCommand());
        commandsArrayList.add(new ReloadConfigCommand());
        commandsArrayList.add(new ClearAllBlocksCommand());
        commandsArrayList.add(new HelpCommand());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("All commands should be executed by a player!");
        } else {
            Player player = (Player) sender;
            if (args.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        getSubCommands().get(i).execute(player, args);
                    }
                }
            }
        }
        return false;
    }


    public static void commandList(RespawnBlocks plugin){
        addCommand(plugin, "rb");
    }

    private static  void addCommand(RespawnBlocks plugin, String commandName){
        plugin.getCommand(commandName).setExecutor(new CommandListener());
    }


    public static ArrayList<CommandBuilder> getSubCommands(){
        return commandsArrayList;
    }
}
