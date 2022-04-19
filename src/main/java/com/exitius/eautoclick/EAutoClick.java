package com.exitius.eautoclick;

import com.exitius.eautoclick.commands.AutoclickCommand;
import com.exitius.eautoclick.commands.EAutoclickCommand;
import com.exitius.eautoclick.listeners.PlayerListener;
import com.exitius.eautoclick.runnables.AutoClickRunnable;
import com.exitius.eautoclick.utils.FileStorage;
import com.exitius.eautoclick.utils.messages.DefaultMessages;
import com.exitius.eautoclick.utils.messages.Messages;
import com.intellectualcrafters.plot.api.PlotAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class EAutoClick extends JavaPlugin {

    private static EAutoClick instance;
    private static Messages messages;
    private static Settings settings;
    private FileStorage fileStorage;
    private PlotAPI plotAPI;

    public static EAutoClick getPlugin() {
        return instance;
    }

    public static Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        EAutoClick.settings = settings;
    }

    public FileStorage getFileStorage() {
        return fileStorage;
    }

    @Override
    public void onEnable() {
        instance = this;
        log("Iniciando plugin...");

        this.saveDefaultConfig();

        messages = new Messages(new File(this.getDataFolder(), "messages.yml"), new DefaultMessages(this));
        fileStorage = new FileStorage(this, "config.yml", "config.yml");
        settings = new Settings(fileStorage);
        log("Configuração carregada com sucesso");

        log("Procurando plugin PlotSquared");
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            Settings.PLOTSQUARED = true;
            plotAPI = new PlotAPI();
            log("PlotSquared encontrado, compatibilidade ativada");
        } else
            log("PlotSquared não encontrado");

        log("Procurando Plugin Factions");
        if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
            Settings.FACTIONS = true;
            log("Factions encontrado, compatibilidade ativada");
        }
        log("Factions não encontrado");

        /*if (settings.AUTODISABLE_ENABLED) {
            DisableAutoClickRunnable runnable = new DisableAutoClickRunnable();
            runnable.runTaskTimer(this, 0, 20 * 5);
        }*/

        if (settings.LITE_MODE) {
            AutoClickRunnable runnable = new AutoClickRunnable(null);
            runnable.runTaskTimer(this, 0, settings.LITE_MODE_MS);
        }

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("autoclick").setExecutor(new AutoclickCommand());
        getCommand("eautoclick").setExecutor(new EAutoclickCommand());
    }

    @Override
    public void onDisable() {
        log("Plugin desabilitando, desativando jogadores com AutoClicker");
        AutoclickCommand.autoclick.clear();
    }

    public static void log(String message) {
        instance.getLogger().log(Level.INFO, message);
    }

    public static void log(Level level, String message) {
        instance.getLogger().log(level, message);
    }

    public static Messages getMessages() {
        return messages;
    }

    public PlotAPI getPlotAPI() {
        return plotAPI;
    }

    public void reload(File file) {
        try {
            getConfig().load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
