package com.example.speingai.tools.datetime;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DatTimeTools {

    @Tool(description = "Get comprehensive date and time information including current date, " +
            "current day of week, tomorrow's date, and tomorrow's day of week")
    public String getDateTimeInfo() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDateTime now = LocalDateTime.now();

        StringBuilder info = new StringBuilder();
        info.append("Current date and time: ")
                .append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n");
        info.append("Today is: ")
                .append(today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .append(" (")
                .append(today).append(")\n");
        info.append("Tomorrow is: ")
                .append(tomorrow.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .append(" (")
                .append(tomorrow).append(")\n");

        return info.toString();
    }

    @Tool(description = "Get current date and time")
    public String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss EEEE");
        return "Current: " + now.format(formatter);
    }

    @Tool(description = "Get tomorrow's day of week")
    public String getTomorrowDayOfWeek() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return "Tomorrow is " +
                tomorrow.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) +
                " (" + tomorrow + ")";
    }

}
