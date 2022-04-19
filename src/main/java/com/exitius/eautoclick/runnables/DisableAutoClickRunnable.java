package com.exitius.eautoclick.runnables;

import com.exitius.eautoclick.EAutoClick;
import com.exitius.eautoclick.commands.AutoclickCommand;
import com.exitius.eautoclick.utils.TimeAPI;
import com.exitius.eautoclick.utils.messages.Messages;
import org.bukkit.scheduler.BukkitRunnable;

public class DisableAutoClickRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (AutoClickRunnable value : AutoclickCommand.autoclick.values()) {
            if (TimeAPI.toTime(EAutoClick.getSettings().AUTODISABLE_TIME + "s") + value.getLastDamage() <= System.currentTimeMillis()) {
                Messages.sendMessage(value.getPlayer(), EAutoClick.getMessages().getMessage("auto_disable"));
                value.cancel();
                AutoclickCommand.autoclick.remove(value.getPlayer().getName());
            }
        }
    }

}
