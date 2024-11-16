package com.asintoto.grimlogger;

import ac.grim.grimac.api.GrimAbstractAPI;
import com.asintoto.grimlogger.commands.LogCommand;
import com.asintoto.grimlogger.commands.MainCommand;
import com.asintoto.grimlogger.commands.exceptions.ExceptionHandler;
import com.asintoto.grimlogger.config.Config;
import com.asintoto.grimlogger.database.Database;
import com.asintoto.grimlogger.database.types.MongoDBDatabase;
import com.asintoto.grimlogger.database.types.SQLDatabase;
import com.asintoto.grimlogger.database.types.SQLiteDatabase;
import com.asintoto.grimlogger.listeners.FlagListener;
import com.asintoto.grimlogger.utils.StrUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

import java.util.logging.Level;

public final class GrimLogger extends JavaPlugin {

    private Config messages;
    @Getter
    private GrimAbstractAPI grimApi;
    @Getter
    private Database database;

    @Override
    public void onEnable() {
        getLogger().info("Started GrimLogger Initialization...");

        getLogger().info("Loading Files...");
        saveDefaultConfig();
        this.messages = new Config(this,"messages.yml");
        getLogger().info("Files Loaded!");

        getLogger().info("Hooking with GrimAC...");
        RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);
        if (provider != null) {
            this.grimApi = provider.getProvider();
        } else {
            getLogger().log(Level.SEVERE, "GrimAC not found, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Hooked with GrimAC!");

        getLogger().info("Connecting to Database...");
        setupDatabase();
        getLogger().info("Database Connected!");

        getLogger().info("Loading Commands...");
        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
                .exceptionHandler(new ExceptionHandler(this))
                .build();
        lamp.register(new MainCommand(this));
        lamp.register(new LogCommand(this));
        getLogger().info("Commands Loaded!");

        getLogger().info("Loading Event Listeners...");
        getServer().getPluginManager().registerEvents(new FlagListener(this), this);
        getLogger().info("Event Listeners Loaded!");

        getLogger().info("Successfully finished GrimLogger Initialization!");
        systemMessage(true);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling GrimLogger...");
        database.close();
        getLogger().info("GrimLogger Disabled!");
        systemMessage(false);
    }

    public static GrimLogger getInstance() {
        return getPlugin(GrimLogger.class);
    }

    private void systemMessage(boolean enable) {
        String msg;
        if(enable) {
            msg = getMessages().getString("system.enable");
        } else {
            msg = getMessages().getString("system.disable");
        }

        getServer().getConsoleSender().sendMessage(StrUtils.format(msg));
    }

    public YamlConfiguration getMessages() {
        return this.messages.getConfig();
    }

    public void reload() {
        getLogger().info("Reloading GrimLogger...");
        reloadConfig();
        this.messages.reload();
        getLogger().info("GrimLogger Reloaded!");
    }

    private void setupDatabase() {
        String type = getConfig().getString("database.type", "sqlite");
        switch (type) {
            case "sqlite":
                this.database = new SQLiteDatabase(this);
                break;
            case "sql":
                this.database = new SQLDatabase(this);
                break;
            case "mongodb":
                this.database = new MongoDBDatabase(this);
                break;
            default:
                getLogger().log(Level.SEVERE, "Database type non valid, disabling plugin...");
                getServer().getPluginManager().disablePlugin(this);
        }
    }

    public String getServerName() {
        return getConfig().getString("server-name", "Global");
    }
}
