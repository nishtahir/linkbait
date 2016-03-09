package com.nishtahir.linkbait.util

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
     * @param calendar
     * @return calendar with time set to beginning of current day
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
     * @return Unix timestamp of beginning of previous week.
     */
    static long getStartOfPreviousWeek(){
        Calendar calendar = Calendar.instance
        calendar.add(Calendar.DATE, -7)
        return getStartOfWeek(calendar)
    }

    /**
     * @param calendar
     * @return calendar with time set to first day of current week at 00:00:00 hh:mm:ss
     */
    static long getStartOfWeek(Calendar calendar) {
        resetWeek(calendar)
        return getUnixTime(calendar.timeInMillis)
    }

    /**
     * @return Unix timestamp of beginning of current month.
     */
    static long getStartOfCurrentMonth() {
        return getStartOfMonth(Calendar.instance)
    }

    /**
     * @param calendar
     * @return calendar with time set to first day of current month at 00:00:00 hh:mm:ss.
     */
    static long getStartOfMonth(Calendar calendar) {
        resetMonth(calendar)
        return getUnixTime(calendar.timeInMillis)
    }

    /**
     * Sets given calendar to 00:00:00 hh:mm:ss.
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
     * Sets given calendar to first day of current week.
     * @param calendar
     * @return
     */
    static def resetWeek(Calendar calendar) {
        resetDay(calendar)
        calendar.setFirstDayOfWeek(Calendar.MONDAY)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    }

    /**
     * Sets given calendar to first day of current month at  00:00:00 hh:mm:ss.
     * @param calendar
     * @return
     */
    static def resetMonth(Calendar calendar) {
        resetDay(calendar)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }

    /**
     * @param timeInMillis time in milliseconds.
     * @return Unix time representation
     */
    static long getUnixTime(long timeInMillis) {
        return timeInMillis / 1000
    }
}
