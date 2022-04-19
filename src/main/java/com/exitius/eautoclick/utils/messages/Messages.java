package com.exitius.eautoclick.utils.messages;

import com.exitius.eautoclick.EAutoClick;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Messages {

    private final File file;
    private final FileConfiguration config;

    public Messages(File file, DefaultMessages defaultMessages) {
        this.file = file;

        this.file.getParentFile().mkdirs();

        try {
            this.file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);

        for (String key : this.config.getKeys(false)) {
            if (!(this.config.get(key) instanceof String)) {
                set(key, null);
            }
        }

        if (defaultMessages != null) {
            for (String key : defaultMessages.getConfig().getKeys(true)) {
                if (!this.config.contains(key)) {
                    set(key, defaultMessages.getConfig().getString(key));
                }
            }
        }

        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EAutoClick.log("Mensagens carregadas com sucesso");
    }

    public Messages(File file) {
        this(file, null);
    }

    public Messages(JavaPlugin plugin) {
        this(new File(plugin.getDataFolder(), "messages.yml"), null);
    }

    public Messages(JavaPlugin plugin, DefaultMessages defaultMessages) {
        this(new File(plugin.getDataFolder(), "messages.yml"), defaultMessages);
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static String compile(String seperator, List<?> list, int start) {
        StringBuilder builder = new StringBuilder();
        if (list == null || seperator == null) {
            return null;
        }
        for (int i = start; i < list.size(); ++i) {
            builder.append(seperator).append(list.get(i));
        }
        String out = builder.toString();
        if (out.startsWith(seperator)) {
            out = out.substring(seperator.length());
        }
        return out;
    }

    public static void sendMessage(CommandSender sendTo, String message) {
        String[] messages;

        if (message.contains("\n")) {
            messages = message.split("\n");
        } else {
            List<String> messagesList = new ArrayList<>();
            messagesList.add(message);

            messages = messagesList.toArray(new String[messagesList.size()]);
        }

        if (sendTo instanceof Player) {
            for (String toSend : messages) {
                sendTo.sendMessage(ChatColor.translateAlternateColorCodes('&', toSend));
            }
            return;
        }

        for (String toSend : messages) {
            sendTo.sendMessage(stripColors(toSend));
        }
    }

    public static void broadcastMessage(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMessage(player, message);
        }

        sendMessage(Bukkit.getConsoleSender(), message);
    }

    public static void broadcastMessage(String message, Permission permission) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                sendMessage(player, message);
            }
        }

        sendMessage(Bukkit.getConsoleSender(), message);
    }

    public static void broadcastMessage(String message, String permission) {
        broadcastMessage(message, new Permission(permission));
    }

    public static String stripColors(String message) {
        return ChatColor.stripColor(Messages.format(message));
    }

    public static String setLength(String in, int len) {
        return in.length() > len ? in.substring(0, len) : in;
    }

    public static String unescapeText(String text) {
        return (' ' + text).replaceAll("([^\\\\])_", "$1 ")
                .replaceAll("([^\\\\])\\|", "$1\n")
                .replaceAll("([^\\\\])\\\\([_\\|])", "$1$2")
                .replace("\\\\", "\\").substring(1);
    }

    public static String optimizeColorCodes(String in) {
        StringBuilder out = new StringBuilder();
        StringBuilder oldFormat = new StringBuilder("&r");
        StringBuilder newFormat = new StringBuilder("&r");
        StringBuilder formatChange = new StringBuilder();
        boolean color = false;
        char[] var7 = in.toCharArray();

        for (char aVar7 : var7) {
            char c = aVar7;
            if (!color) {
                if (c == 167) {
                    color = true;
                } else {
                    if (!newFormat.toString().equals(oldFormat.toString())) {
                        out.append(formatChange);
                        formatChange.setLength(0);
                        oldFormat.setLength(0);
                        oldFormat.append(newFormat);
                    }

                    out.append(c);
                }
            } else {
                color = false;
                if (c >= 107 && c <= 111) {
                    int max = newFormat.length();
                    boolean add = true;

                    for (int i = 1; i < max; i += 2) {
                        if (newFormat.charAt(i) == c) {
                            add = false;
                            break;
                        }
                    }

                    if (add) {
                        newFormat.append('&').append(c);
                        formatChange.append('&').append(c);
                    }
                } else {
                    if ((c < 48 || c > 57) && (c < 97 || c > 102)) {
                        c = 102;
                    }

                    newFormat.setLength(0);
                    newFormat.append('&').append(c);
                    formatChange.setLength(0);
                    formatChange.append('&').append(c);
                }
            }
        }

        return ChatColor.translateAlternateColorCodes('&', out.toString());
    }

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String chatColourToString(String message) {
        if (message != null)
            if (message.contains(ChatColor.COLOR_CHAR + ""))
                return message.replace(ChatColor.COLOR_CHAR, '&');
        return message;
    }

    public void set(String key, String message) {
        this.config.set(key, message);
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public String getMessage(String key) {
        return Messages.format(this.config.getString(key, ""));
    }

    public String getFlatMessage(String key) {
        return stripColors(getMessage(key));
    }

}
