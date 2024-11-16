package com.asintoto.grimlogger.utils;

import com.asintoto.colorlib.ColorLib;
import com.asintoto.grimlogger.GrimLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class StrUtils {
    private static final GrimLogger plugin = GrimLogger.getInstance();

    public static String format(String msg) {
        return ColorLib.setColors(msg);
    }

    public static String getDate() {
        String format = getDateFormat();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String getDateFormat() {
        return plugin.getConfig().getString("date.format", "yyyy-MM-dd HH:mm:ss");
    }

    public static void consoleLog(String msg) {
        plugin.getServer().getConsoleSender().sendMessage(format(msg));
    }

    public static String timeAgo(String dateTimeString) {
        String format = getDateFormat();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime dataTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime now = LocalDateTime.now();


        long secondsInMinute = 60;
        long secondsInHour = 60 * secondsInMinute;
        long secondsInDay = 24 * secondsInHour;
        long secondsInMonth = 30 * secondsInDay;
        long secondsInYear = 12 * secondsInMonth;

        long totalSeconds = ChronoUnit.SECONDS.between(dataTime, now);

        long years = totalSeconds / secondsInYear;
        totalSeconds %= secondsInYear;

        long months = totalSeconds / secondsInMonth;
        totalSeconds %= secondsInMonth;

        long days = totalSeconds / secondsInDay;
        totalSeconds %= secondsInDay;

        long hours = totalSeconds / secondsInHour;
        totalSeconds %= secondsInHour;

        long minutes = totalSeconds / secondsInMinute;
        totalSeconds %= secondsInMinute;

        long seconds = totalSeconds;

        StringBuilder timeAgo = new StringBuilder();

        if (years > 0) {
            timeAgo.append(years).append("y ");
        }
        if (months > 0) {
            timeAgo.append(months).append("m ");
        }
        if (days > 0) {
            timeAgo.append(days).append("d ");
        }
        if (hours > 0) {
            timeAgo.append(hours).append("h ");
        }
        if (minutes > 0) {
            timeAgo.append(minutes).append("m ");
        }
        if (seconds > 0) {
            timeAgo.append(seconds).append("s ");
        }

        if (timeAgo.isEmpty()) {
            String justnow = plugin.getMessages().getString("general.just-now", "Just Now");
            timeAgo.append(justnow);
        } else {
            String ago = plugin.getMessages().getString("general.ago", "ago");
            timeAgo.append(ago);
        }


        return timeAgo.toString().trim();
    }
}
