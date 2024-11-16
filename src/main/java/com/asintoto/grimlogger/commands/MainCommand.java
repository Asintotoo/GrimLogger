package com.asintoto.grimlogger.commands;

import com.asintoto.grimlogger.GrimLogger;
import com.asintoto.grimlogger.utils.CommandUtils;
import com.asintoto.grimlogger.utils.StrUtils;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;

@Command("grimlogger")
@CommandPermission("grimlogger.command.use")
public class MainCommand {
    private final GrimLogger plugin;

    public MainCommand(GrimLogger plugin) {
        this.plugin = plugin;
    }

    @CommandPlaceholder
    public void onDefault(BukkitCommandActor actor) {
        String msg = plugin.getMessages().getString("general.credit", "&b• &fRunning &bGrimLogger v%version% &fby &b%authors%");
        msg = msg.replace("%version%", plugin.getDescription().getVersion())
                .replace("%authors%", String.join(", ", plugin.getDescription().getAuthors()));

        actor.sender().sendMessage(StrUtils.format(msg));
    }

    @Subcommand("reload")
    public void onReload(BukkitCommandActor actor) {
        if(!CommandUtils.checkPermission(actor, "grimlogger.command.reload")) return;

        plugin.reload();
        String msg = plugin.getMessages().getString("general.reload", "&bPlugin Reloaded");
        actor.sender().sendMessage(StrUtils.format(msg));
    }

    @Subcommand("help")
    public void onHelp(BukkitCommandActor actor) {
        if(!CommandUtils.checkPermission(actor, "grimlogger.command.help")) return;

        List<String> msgs = plugin.getMessages().getStringList("general.help");
        for(String msg : msgs) {
            actor.sender().sendMessage(StrUtils.format(msg));
        }
    }
}
