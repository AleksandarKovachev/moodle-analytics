package com.moodle.analytics.constant;

import org.springframework.context.MessageSource;

import java.util.Locale;

public enum DayOfWeek {

    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int dayOfWeek;

    DayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public static DayOfWeek fromDayOfWeek(int day) {
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.getDayOfWeek() == day) {
                return dayOfWeek;
            }
        }
        return null;
    }

    public String getDayName(MessageSource messageSource) {
        return messageSource.getMessage(name().toLowerCase(), null, Locale.getDefault());
    }

}
