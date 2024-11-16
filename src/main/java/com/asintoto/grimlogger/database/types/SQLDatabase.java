package com.asintoto.grimlogger.database.types;

import ac.grim.grimac.api.GrimUser;
import com.asintoto.grimlogger.GrimLogger;
import com.asintoto.grimlogger.database.Database;
import com.asintoto.grimlogger.utils.StrUtils;
import com.asintoto.grimlogger.utils.ViolationEntry;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class SQLDatabase extends Database {
    protected Connection connection;

    public SQLDatabase(GrimLogger plugin) {
        super(plugin);
        connect();
        createTables();
    }

    @Override
    public void connect() {
        String databaseName = plugin.getConfig().getString("database.database-name", "storage");
        String ipAddress = plugin.getConfig().getString("database.ip-address", "localhost");
        String port = Integer.toString(plugin.getConfig().getInt("database.port", 3306));
        String user = plugin.getConfig().getString("database.user", "root");
        String password = plugin.getConfig().getString("database.password", "");

        String url = "jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName;

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Can't connect to database, disabling plugin...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public void createTables() {
        CompletableFuture.runAsync(() -> {
            try {
                Statement statement = connection.createStatement();
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS violations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    uuid TEXT NOT NULL,
                    username TEXT NOT NULL,
                    checkname TEXT,
                    checkvl INTEGER DEFAULT 1,
                    servername TEXT,
                    date TEXT)
                    """);
            } catch (SQLException e) {
                printError(e);
            }
        });
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            printError(e);
        }
    }

    @Override
    public CompletableFuture<Void> insertViolation(GrimUser player, String checkname, int vl) {
        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement preparedStatement =
                         connection.prepareStatement("INSERT INTO violations (uuid, username, checkname, checkvl, date, servername) VALUES (?, ?, ?, ?, ?, ?)")) {

                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setString(3, checkname);
                preparedStatement.setString(4, Integer.toString(vl));
                preparedStatement.setString(5, StrUtils.getDate());
                preparedStatement.setString(6, plugin.getServerName());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                printError(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<ViolationEntry>> getViolations(OfflinePlayer player, int start, int stop) {
        return CompletableFuture.supplyAsync(() -> {
            List<ViolationEntry> violations = new ArrayList<>();
            String query = """
                SELECT uuid, username, checkname, checkvl, date, servername
                FROM violations
                WHERE uuid = ?
                ORDER BY date DESC
                LIMIT ? OFFSET ?;
            """;

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setInt(2, stop - start);
                statement.setInt(3, start);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String uuidResult = resultSet.getString("uuid");
                    String username = resultSet.getString("username");
                    String checkname = resultSet.getString("checkname");
                    String vl = resultSet.getString("checkvl");
                    String date = resultSet.getString("date");
                    String servername = resultSet.getString("servername");

                    violations.add(new ViolationEntry(uuidResult, username, checkname, vl, date, servername));
                }

                return violations;
            } catch (SQLException e) {
                printError(e);
                return violations;
            }
        });
    }
}
