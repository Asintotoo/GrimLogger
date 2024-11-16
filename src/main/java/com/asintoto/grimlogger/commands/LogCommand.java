package com.asintoto.grimlogger.commands;

import com.asintoto.grimlogger.GrimLogger;
import com.asintoto.grimlogger.utils.StrUtils;
import com.asintoto.grimlogger.utils.ViolationEntry;
import org.bukkit.OfflinePlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command("grimlog")
@CommandPermission("grimlogger.command.log")
public class LogCommand {
    private final GrimLogger plugin;

    public LogCommand(GrimLogger plugin) {
        this.plugin = plugin;
    }

    @CommandPlaceholder
    public void onLog(BukkitCommandActor actor, OfflinePlayer player, @Range(min = 1) @Default("1") int page) {
        int offsetpage = page - 1;

        int logperpage = plugin.getConfig().getInt("log.entry-per-page", 10);
        int start = offsetpage * logperpage;
        int stop = offsetpage + logperpage;

        String fetch  = plugin.getMessages().getString("general.fetch", "&7Fetching data for %player%...");
        fetch = fetch.replace("%player%", player.getName());
        actor.sender().sendMessage(StrUtils.format(fetch));

        plugin.getDatabase().getViolations(player, start, stop).thenAccept(l -> {

            if(l.isEmpty()) {
                String empty = plugin.getMessages().getString("error.no-records", "&cNo records found for %player% at page %page%");
                empty = empty.replace("%player%", player.getName()).replace("%page%", Integer.toString(page));
                actor.sender().sendMessage(StrUtils.format(empty));
                return;
            }

            String header = plugin.getMessages().getString("general.log-header", "&bViolations for %player% at page %page%");
            header = header.replace("%player%", player.getName()).replace("%page%", Integer.toString(page));
            actor.sender().sendMessage(StrUtils.format(header));

            String msg = plugin.getMessages().getString("general.log-entry", "&3[%server%] &b%check% &f(x&c%vl%&f) &7%date%");
            for (ViolationEntry e : l) {

                String current = msg.replace("%player%", e.username())
                        .replace("%check%", e.checkname())
                        .replace("%server%", e.servername())
                        .replace("%vl%", e.vl())
                        .replace("%date%", StrUtils.timeAgo(e.date()));

                actor.sender().sendMessage(StrUtils.format(current));
            }
        });
    }
}
