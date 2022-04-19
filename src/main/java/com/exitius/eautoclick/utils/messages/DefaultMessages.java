package com.exitius.eautoclick.utils.messages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Criado por Guilherme Padovam em 4/30/2017.
 * Todos os direitos reservados.
 */
public class DefaultMessages {

    private File file;
    private FileConfiguration config;

    public DefaultMessages(File file) {
        this.file = file;
    }

    public DefaultMessages(JavaPlugin plugin) {
        this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("messages.yml")));
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

}
