package com.exitius.eautoclick.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Criado por Guilherme Padovam em 4/30/2017.
 * Todos os direitos reservados.
 */
public class FileStorage extends YamlConfiguration {

    private final File file;
    private final String defaults;
    private final JavaPlugin plugin;

    public FileStorage(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    public FileStorage(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    public void reload() {
        if (!file.exists()) {
            try {
                boolean mkdirs = file.getParentFile().mkdirs();
                boolean newFile = file.createNewFile();
                load(file);
                if (defaults != null) {
                    InputStreamReader reader = new InputStreamReader(plugin.getResource(defaults));
                    FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                    setDefaults(defaultsConfig);
                    options().copyDefaults(true);

                    reader.close();
                    save();
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else
            try {
                load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
