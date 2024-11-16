package com.asintoto.grimlogger.commands.exceptions;

import com.asintoto.grimlogger.GrimLogger;
import com.asintoto.grimlogger.utils.StrUtils;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.*;
import revxrsal.commands.exception.*;
import revxrsal.commands.node.ParameterNode;

public class ExceptionHandler extends BukkitExceptionHandler {
    private final GrimLogger plugin;

    public ExceptionHandler(GrimLogger plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onInvalidPlayer(InvalidPlayerException e, BukkitCommandActor actor) {
        String msg = plugin.getMessages().getString("error.invalid-player", "&cInvalid player: &e%player%&c.");
        msg = msg.replace("%player%", e.input());

        actor.error(StrUtils.format(msg));
    }

    @Override
    public void onInvalidDecimal(InvalidDecimalException e, BukkitCommandActor actor) {
        String msg = plugin.getMessages().getString("error.invalid-number", "&cInvalid number &e%number%&c.");
        msg = msg.replace("%number%", e.input());

        actor.error(StrUtils.format(msg));
    }

    @Override
    public void onInvalidInteger(InvalidIntegerException e, BukkitCommandActor actor) {
        String msg = plugin.getMessages().getString("error.invalid-integer", "&cInvalid integer &e%number%&c.");
        msg = msg.replace("%number%", e.input());

        actor.error(StrUtils.format(msg));
    }

    @Override
    public void onMissingArgument(MissingArgumentException e, BukkitCommandActor actor,  ParameterNode<BukkitCommandActor, ?> parameter) {
        String msg = plugin.getMessages().getString("error.missing-arg", "&cRequired parameter is missing: &e%param%&c. Usage: &e/%usage%&c.");
        msg = msg.replace("%param%", parameter.name())
                .replace("%usage%", parameter.command().usage());

        actor.error(StrUtils.format(msg));
    }

    @Override
    public void onNoPermission(NoPermissionException e, BukkitCommandActor actor) {
        String msg = plugin.getMessages().getString("error.no-permission", "&cYou do not have permission to execute this command!");

        actor.error(StrUtils.format(msg));
    }

    @Override
    public void onNumberNotInRange(NumberNotInRangeException e, BukkitCommandActor actor, ParameterNode<BukkitCommandActor, Number> parameter) {
        String msg = plugin.getMessages().getString("error.not-in-range", "&c%param% must be greater than %min%");
        msg = msg.replace("%param%", parameter.name())
                .replace("%min%", fmt(e.minimum()));

        actor.error(StrUtils.format(msg));

    }
}
