package com.exitius.eautoclick.listeners;

import com.exitius.eautoclick.EAutoClick;
import com.exitius.eautoclick.commands.AutoclickCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (AutoclickCommand.autoclick.containsKey(player.getName())) {
            AutoclickCommand.autoclick.get(player.getName()).cancel();
            AutoclickCommand.autoclick.remove(player.getName());
        }
    }

    @EventHandler
    public void playerChangeWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (EAutoClick.getSettings().DISABLED_WORLDS.contains(player.getLocation().getWorld().getName())) {
            if (AutoclickCommand.autoclick.containsKey(player.getName())) {
                AutoclickCommand.autoclick.get(player.getName()).cancel();
                AutoclickCommand.autoclick.remove(player.getName());
            }
        }
    }

}
