package com.nishtahir.islackbot.util

/**
 * Utility class to help with manipulating timestamps.
 */
class TimestampUtils {

    /**
     * @return Unix timestamp of beginning of current day.
     */
    static long getStartOfCurrentDay() {
        return getStartOfDay(Calendar.instance)
    }

    /**
     *
     * @param calendar
     * @return
     */
    static long getStartOfDay(Calendar calendar) {
        resetDay(calendar)
        return getUnixTime(calendar.timeInMillis)
    }

    /**
     * @return Unix timestamp of beginning of current week.
     */
    static long getStartOfCurrentWeek() {
        return getStartOfWeek(Calendar.instance)
    }

    /**
     *
     * @param calendar
     * @return
     */
    static long getStartOfWeek(Calendar calendar) {
        resetWeek(calendar)
        return getUnixTime(calendar.timeInMillis)
    }

    /**
     * @return Unix timestamp of beginning of current week.
     */
    static long getStartOfCurrentMonth() {
        return getStartOfMonth(Calendar.instance)
    }

    /**
     *
     * @param calendar
     * @return
     */
    static long getStartOfMonth(Calendar calendar) {
        resetMonth(calendar)
        return getUnixTime(calendar.timeInMillis)
    }

    /**
     *
     * @param calendar
     * @return
     */
    static def resetDay(def calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    /**
     *
     * @param calendar
     * @return
     */
    static def resetWeek(Calendar calendar) {
        resetDay(calendar)
        calendar.setFirstDayOfWeek(Calendar.MONDAY)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    }

    /**
     *
     * @param calendar
     * @return
     */
    static def resetMonth(Calendar calendar) {
        resetWeek(calendar)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }

    /**
     *
     * @param timeInMillis
     * @return
     */
    static long getUnixTime(long timeInMillis) {
        return timeInMillis / 1000
    }
}
