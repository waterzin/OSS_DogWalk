package com.example.oss;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {
    public static List<LocalDate> getThisWeekDates() {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DayOfWeek currentDay = today.getDayOfWeek();
        int daysFromMonday = currentDay.getValue() - DayOfWeek.MONDAY.getValue();
        LocalDate monday = today.minusDays(daysFromMonday);

        for (int i = 0; i < 7; i++) {
            dates.add(monday.plusDays(i));
        }

        return dates;
    }
}

