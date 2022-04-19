package com.exitius.eautoclick.utils.commands;

import com.exitius.eautoclick.utils.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ESubCommand {

    public abstract String getName();

    public abstract String[] getAliases();

    public abstract String getUsage();

    public abstract String getDescription();

    public abstract String getPermission();

    public abstract String getPermissionMessage();

    public abstract int getMinArgs();

    public abstract String getNotEnoughArgsMessage();

    public abstract boolean isPlayerOnly();

    public abstract void run(CommandSender commandSender, String label, String[] args);

    public void onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (isPlayerOnly() && !(commandSender instanceof Player)) {
            Messages.sendMessage(commandSender, getName() + " so pode ser executado por jogadores.");
            return;
        }

        if (!commandSender.hasPermission(getPermission())) {
            Messages.sendMessage(commandSender, getPermissionMessage());
            return;
        }

        if (args.length - 1 < getMinArgs()) {
            Messages.sendMessage(commandSender, getNotEnoughArgsMessage());
            return;
        }

        run(commandSender, label, args);
    }

}
