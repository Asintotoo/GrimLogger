package com.asintoto.grimlogger.database.types;

import ac.grim.grimac.api.GrimUser;
import com.asintoto.grimlogger.GrimLogger;
import com.asintoto.grimlogger.database.Database;
import com.asintoto.grimlogger.utils.StrUtils;
import com.asintoto.grimlogger.utils.ViolationEntry;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import com.mongodb.client.*;
import org.bson.Document;

@SuppressWarnings("all")
public class MongoDBDatabase extends Database {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> violationsCollection;

    public MongoDBDatabase(GrimLogger plugin) {
        super(plugin);
        connect();
    }

    @Override
    public void connect() {
        String host = plugin.getConfig().getString("database.ip-address", "localhost");
        int port = plugin.getConfig().getInt("database.port", 27017);
        String dbName = plugin.getConfig().getString("database.database-name", "storage");
        String user = plugin.getConfig().getString("database.user", "root");
        String password = plugin.getConfig().getString("database.password", "");
        String collection = plugin.getConfig().getString("database.collection", "grimlog");

        String conn = "mongodb+srv://" + user + ":" + password + "@" + host + "/?retryWrites=true&w=majority";

        try {
            mongoClient = MongoClients.create(conn);
            database = mongoClient.getDatabase(dbName);
            violationsCollection = database.getCollection(collection);
            if(violationsCollection == null) {
                database.createCollection(collection);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Can't connect to MongoDB, disabling plugin...");
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    public void createTables() {
        // Do nothing
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public CompletableFuture<Void> insertViolation(GrimUser player, String checkname, int vl) {
        return CompletableFuture.runAsync(() -> {
            try {
                Document violation = new Document()
                        .append("uuid", player.getUniqueId().toString())
                        .append("username", player.getName())
                        .append("checkname", checkname)
                        .append("checkvl", vl)
                        .append("date", StrUtils.getDate())
                        .append("servername", plugin.getServerName());

                violationsCollection.insertOne(violation);
            } catch (Exception e) {
                printError(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<ViolationEntry>> getViolations(OfflinePlayer player, int start, int stop) {
        return CompletableFuture.supplyAsync(() -> {
            List<ViolationEntry> violations = new ArrayList<>();
            try {
                FindIterable<Document> result = violationsCollection.find(Filters.eq("uuid", player.getUniqueId().toString()))
                        .sort(Sorts.descending("date"))
                        .skip(start)
                        .limit(stop - start);

                for (Document doc : result) {
                    String uuid = doc.getString("uuid");
                    String username = doc.getString("username");
                    String checkname = doc.getString("checkname");
                    int vl = doc.getInteger("checkvl");
                    String date = doc.getString("date");
                    String servername = doc.getString("servername");

                    violations.add(new ViolationEntry(uuid, username, checkname, Integer.toString(vl), date, servername));
                }

            } catch (Exception e) {
                printError(e);
            }

            return violations;
        });
    }
}
