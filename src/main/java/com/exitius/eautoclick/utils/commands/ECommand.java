package com.exitius.eautoclick.utils.commands;

import com.exitius.eautoclick.utils.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class ECommand extends Command implements CommandExecutor {

    public ECommand(String name) {
        super(name);
    }

    public ECommand(String name, String description) {
        super(name);
        this.setDescription(description);
    }

    public ECommand(String name, String description, List<String> aliases) {
        super(name);
        this.setDescription(description);
        this.setAliases(aliases);
    }

    public abstract String getUsage();

    public abstract String getPermission();

    public abstract String getPermissionMessage();

    public abstract int getMinArgs();

    public abstract String getNotEnoughArgsMessage();

    public abstract boolean isPlayerOnly();

    public abstract ESubCommand[] getSubCommands();

    public abstract void run(CommandSender commandSender, String label, String[] args);

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (isPlayerOnly() && !(commandSender instanceof Player)) {
            Messages.sendMessage(commandSender, getUsage() + " so pode ser executado por jogadores.");
            return true;
        }

        if (!commandSender.hasPermission(getPermission())) {
            Messages.sendMessage(commandSender, getPermissionMessage());
            return true;
        }

        if (args.length < getMinArgs()) {
            Messages.sendMessage(commandSender, getNotEnoughArgsMessage());
            return true;
        }

        if (args.length != 0 && getSubCommands().length != 0) {
            for (ESubCommand subCommand : getSubCommands()) {
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    subCommand.onCommand(commandSender, command, label, args);
                    return true;
                } else {
                    for (String alias : subCommand.getAliases()) {
                        if (args[0].equalsIgnoreCase(alias)) {
                            subCommand.onCommand(commandSender, command, label, args);
                            return true;
                        }
                    }
                }
            }
        }

        run(commandSender, label, args);

        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        onCommand(sender, this, commandLabel, args);
        return true;
    }
}
