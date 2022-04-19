package com.exitius.eautoclick;

import com.exitius.eautoclick.utils.FileStorage;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class Settings {

    public static boolean PLOTSQUARED = false, FACTIONS = false;

    public List<String> DISABLED_WORLDS;
    public boolean FACTIONS_ENABLED, PLOTS_ENABLED, PLOTS_MEMBER, PLOTS_TRUSTED, LITE_MODE,
            FIXEDDAMAGE_ENABLED, HAND_ANIMATION, STRENGTH_POTION, SHARPNESS, AUTODISABLE_ENABLED;
    public double FIXEDDAMAGE_DAMAGE;
    public int AUTODISABLE_TIME, LITE_MODE_MS, MSPERCLICK_DEFAULT, MOBSPERHIT_DEFAULT;
    public HashMap<Material, Double> CUSTOMDAMAGE;
    public HashMap<String, Integer> MSPERCLICK, MOBSPERHIT;

    public Settings(FileStorage config) {
        DISABLED_WORLDS = config.getStringList("disabled_worlds");
        FACTIONS_ENABLED = config.getBoolean("factions.enabled");
        PLOTS_ENABLED = config.getBoolean("plots.enabled");
        PLOTS_MEMBER = config.getBoolean("plots.member");
        PLOTS_TRUSTED = config.getBoolean("plots.trusted");
        LITE_MODE = config.getBoolean("lite_mode.enabled");
        LITE_MODE_MS = config.getInt("lite_mode.click_time");
        FIXEDDAMAGE_ENABLED = config.getBoolean("fixed_damage.enabled");
        FIXEDDAMAGE_DAMAGE = config.getDouble("fixed_damage.damage");
        HAND_ANIMATION = config.getBoolean("hand_animation");
        STRENGTH_POTION = config.getBoolean("strength_potion");
        SHARPNESS = config.getBoolean("sharpness");
        AUTODISABLE_ENABLED = config.getBoolean("auto_disable.enabled");
        AUTODISABLE_TIME = config.getInt("auto_disable.time");
        MSPERCLICK_DEFAULT = config.getInt("ms_per_click.default");
        MOBSPERHIT_DEFAULT = config.getInt("mobs_per_hit.default");
        CUSTOMDAMAGE = new HashMap<>();
        for (String material_name : config.getConfigurationSection("custom_damage").getKeys(false)) {
            Material material = null;
            try {
                material = Material.valueOf(material_name);
            } catch (Exception exception) {
                EAutoClick.log(Level.WARNING, "O material " + material_name + " inserido na config.yml não existe");
            }

            if (material != null)
                CUSTOMDAMAGE.put(material, config.getDouble("custom_damage." + material_name));
        }
        MSPERCLICK = new HashMap<>();
        for (String permission : config.getConfigurationSection("ms_per_click").getKeys(false)) {
            if (!permission.equalsIgnoreCase("default"))
                MSPERCLICK.put(permission, config.getInt("ms_per_click." + permission));
        }
        MOBSPERHIT = new HashMap<>();
        for (String permission : config.getConfigurationSection("mobs_per_hit").getKeys(false)) {
            if (!permission.equalsIgnoreCase("default"))
                MOBSPERHIT.put(permission, config.getInt("mobs_per_hit" + permission));
        }
    }

    public void reload() {
        EAutoClick.getPlugin().getFileStorage().reload();
        EAutoClick.getPlugin().setSettings(new Settings(EAutoClick.getPlugin().getFileStorage()));
    }

}
