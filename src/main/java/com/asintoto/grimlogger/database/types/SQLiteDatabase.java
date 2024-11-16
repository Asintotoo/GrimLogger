package com.asintoto.grimlogger.database.types;

import com.asintoto.grimlogger.GrimLogger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLiteDatabase extends SQLDatabase{

    public SQLiteDatabase(GrimLogger plugin) {
        super(plugin);
    }

    @Override
    public void connect() {
        String path = plugin.getConfig().getString("database.database-name", "storage");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/" + path + ".db");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Can't connect to database, disabling plugin...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}
