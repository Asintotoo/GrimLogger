package com.asintoto.grimlogger.config;

import com.asintoto.grimlogger.GrimLogger;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {
    @Getter
    private final File file;
    @Getter
    private YamlConfiguration config;
    @Getter
    private final String fileName;

    protected final GrimLogger plugin;

    public Config(GrimLogger plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        file = new File(plugin.getDataFolder(), fileName);

        if(!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
