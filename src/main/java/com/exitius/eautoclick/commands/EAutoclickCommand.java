package com.exitius.eautoclick.commands;

import com.exitius.eautoclick.EAutoClick;
import com.exitius.eautoclick.utils.commands.ECommand;
import com.exitius.eautoclick.utils.commands.ESubCommand;
import com.exitius.eautoclick.utils.messages.Messages;
import org.bukkit.command.CommandSender;

public class EAutoclickCommand extends ECommand {

    public EAutoclickCommand() {
        super("eautoclick");
    }

    @Override
    public String getUsage() {
        return "/eautoclick";
    }

    @Override
    public String getPermission() {
        return "eautoclick.command." + getName();
    }

    @Override
    public String getPermissionMessage() {
        return EAutoClick.getMessages().getMessage("error.no_perm");
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public String getNotEnoughArgsMessage() {
        return null;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public ESubCommand[] getSubCommands() {
        return new ESubCommand[0];
    }

    @Override
    public void run(CommandSender commandSender, String s, String[] strings) {

        EAutoClick.getSettings().reload();
        EAutoClick.getMessages().save();
        Messages.sendMessage(commandSender, "&aConfiguração recarregada com sucesso");
    }
}
