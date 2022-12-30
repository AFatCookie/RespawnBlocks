package me.afatcookie.respawnblocks.respawnblocks.commands;
import me.afatcookie.respawnblocks.respawnblocks.RespawnBlocks;
import org.bukkit.command.CommandSender;

public abstract class CommandBuilder {
    protected RespawnBlocks instance = RespawnBlocks.getInstance();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void execute(CommandSender commandSender, String[] args);
}
