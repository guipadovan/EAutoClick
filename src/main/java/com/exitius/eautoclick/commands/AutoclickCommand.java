package com.exitius.eautoclick.commands;

import com.exitius.eautoclick.EAutoClick;
import com.exitius.eautoclick.runnables.AutoClickRunnable;
import com.exitius.eautoclick.utils.commands.ECommand;
import com.exitius.eautoclick.utils.commands.ESubCommand;
import com.exitius.eautoclick.utils.messages.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AutoclickCommand extends ECommand {

    public static HashMap<String, AutoClickRunnable> autoclick = new HashMap<>();

    public AutoclickCommand() {
        super("autoclick");
    }

    @Override
    public String getUsage() {
        return "/autoclick";
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
        return true;
    }

    @Override
    public ESubCommand[] getSubCommands() {
        return new ESubCommand[0];
    }

    @Override
    public void run(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (autoclick.containsKey(player.getName())) {
            if (!EAutoClick.getSettings().LITE_MODE)
                autoclick.get(player.getName()).cancel();
            autoclick.remove(player.getName());
            Messages.sendMessage(player, EAutoClick.getMessages().getMessage("disabled"));
        } else {
            for (String disabled_world : EAutoClick.getSettings().DISABLED_WORLDS) {
                if (player.getLocation().getWorld().getName().equalsIgnoreCase(disabled_world)) {
                    Messages.sendMessage(player, EAutoClick.getMessages().getMessage("error.disabled_world"));
                    return;
                }
            }
            int time = EAutoClick.getSettings().MSPERCLICK_DEFAULT;
            for (String permission : EAutoClick.getSettings().MSPERCLICK.keySet()) {
                if (player.hasPermission("eautoclick.ms." + permission))
                    time = EAutoClick.getSettings().MSPERCLICK.get(permission);
            }
            AutoClickRunnable runnable = new AutoClickRunnable(player);
            if (!EAutoClick.getSettings().LITE_MODE)
                runnable.runTaskTimer(EAutoClick.getPlugin(), 0, time);
            else
                runnable = null;
            autoclick.put(player.getName(), runnable);
            Messages.sendMessage(player, EAutoClick.getMessages().getMessage("enabled"));
        }
    }
}
