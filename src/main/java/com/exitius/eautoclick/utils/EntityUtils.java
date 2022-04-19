package com.exitius.eautoclick.utils;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityUtils {

    public static List<Entity> getNearbyEntities(Player player, int radius) {
        final ArrayList<Entity> list = new ArrayList<>();
        final Iterator<Entity> iterator2;
        Iterator<Entity> iterator = iterator2 = player.getNearbyEntities(radius, radius, radius).iterator();
        while (iterator.hasNext()) {
            final Entity entity;
            if ((entity = iterator2.next()) instanceof Creature) {
                list.add(entity);
            }
        }
        return list;
    }

}
