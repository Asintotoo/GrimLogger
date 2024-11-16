package com.asintoto.grimlogger.listeners;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.events.CommandExecuteEvent;
import com.asintoto.grimlogger.GrimLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlagListener implements Listener {
    private final GrimLogger plugin;

    public FlagListener(GrimLogger plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFlag(CommandExecuteEvent e) {
        GrimUser user = e.getPlayer();
        AbstractCheck check = e.getCheck();
        String cmd = e.getCommand();

        System.out.println(cmd);

        int vl = (int) e.getViolations();
        String checkname = check.getCheckName();

        plugin.getDatabase().insertViolation(user, checkname, vl);
    }
}
