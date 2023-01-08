package me.afatcookie.respawnblocks.respawnblocks.listeners;

import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import me.afatcookie.respawnblocks.respawnblocks.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;


public class CommandListener implements CommandExecutor {
    private final List<CommandBuilder> commands;

    private static List<CommandBuilder> commandsToPass;

    public CommandListener() {
        commands = Arrays.asList(
                new CreateRespawnBlockCommand(),
                new OpenBlocksGUICommand(),
                new RespawnBlockRemoveCommand(),
                new GiveBlockChangerCommand(),
                new ReloadConfigCommand(),
                new ClearAllBlocksCommand(),
                new HelpCommand(),
                new CreateRegionRespawnBlocksCommand(),
                new AddRewardCommand(),
                new CheckBlockCommand()
        );
        commandsToPass = commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("All commands should be executed by a player!");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 0) return false;

        String subCommandName = args[0].toLowerCase();
        for (CommandBuilder subCommand : commands) {
            if (subCommandName.equals(subCommand.getName().toLowerCase())) {
                subCommand.execute(player, args);
                return true;
            }
        }

        return false;
    }

    public static void register(RespawnBlocks plugin, String commandName) {
        plugin.getCommand(commandName).setExecutor(new CommandListener());
    }

    public static List<CommandBuilder> getCommands(){
        return commandsToPass;
    }
}

/*
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
        commandsArrayList.add(new CreateRegionRespawnBlocksCommand());

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

 */
