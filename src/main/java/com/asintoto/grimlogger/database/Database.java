package com.asintoto.grimlogger.database;

import ac.grim.grimac.api.GrimUser;
import com.asintoto.grimlogger.GrimLogger;
import com.asintoto.grimlogger.utils.StrUtils;
import com.asintoto.grimlogger.utils.ViolationEntry;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class Database {
    protected final GrimLogger plugin;

    public Database(GrimLogger plugin) {
        this.plugin = plugin;
    }

    public abstract void connect();
    public abstract void createTables();
    public abstract void close();

    public abstract CompletableFuture<Void> insertViolation(GrimUser player, String checkname, int vl);
    public abstract CompletableFuture<List<ViolationEntry>> getViolations(OfflinePlayer player, int start, int stop);

    @SuppressWarnings("all")
    protected void printError(Exception e) {
        String msg = plugin.getMessages().getString("error.database-error", "&cError during database access!");
        StrUtils.consoleLog(msg);
        e.printStackTrace();
    }
}
