package com.asintoto.grimlogger.utils;

import com.asintoto.grimlogger.GrimLogger;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class CommandUtils {
    private static final GrimLogger plugin = GrimLogger.getInstance();

    public static boolean hasPermission(BukkitCommandActor actor, String permission) {
        return actor.sender().hasPermission(permission);
    }

    public static boolean checkPermission(BukkitCommandActor actor, String permission) {
        if(!hasPermission(actor, permission)) {
            String msg = plugin.getMessages().getString("error.no-permission", "&cYou do not have permission to execute this command!");
            actor.sender().sendMessage(StrUtils.format(msg));

            return false;
        }
        return true;
    }
}
