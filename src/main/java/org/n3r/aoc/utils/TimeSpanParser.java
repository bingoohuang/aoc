package org.n3r.aoc.utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a time span. Currently the units supported are from seconds, minutes, hours and days.
 */
public final class TimeSpanParser {

    /**
     * factors to calculate seconds from group index.
     */
    private static final List<TimeUnit> TIME_UNITS = Arrays.asList(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES,
            TimeUnit.SECONDS);

    /**
     * the pattern to recognize the different time span units and their values.
     */
    private static final Pattern PATTERN = Pattern.compile("^(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s?)?$");

    /**
     * no construction, please.
     */
    private TimeSpanParser() {
        ; // inhibit instantiation of utility class.
    }

    /**
     * Parses a time span string and returns the time span according to the given unit. If the given time span string is
     * empty or null, the default value will be returned. Default time unit is seconds.
     * <p/>
     * Example time spans (which are all equal in value) are:
     * <ul>
     * <li>86400</li>
     * <li>86400s</li>
     * <li>1440m</li>
     * <li>24h</li>
     * <li>1d</li>
     * </ul>
     * <p/>
     * Also mixed time spans can be used, but only in the correct order <b>d</b>, <b>h</b>, <b>m</b>, <b>s</b> e.g.:
     * <ul>
     * <li>1d12h10m45s</li>
     * <li>12h10s</li>
     * </ul>
     * <p/>
     * For each of the above strings the result of parsing it with the <code>TimeUnit.MINUTES</code> would result in the
     * value of <code>1440</code>.
     * <p/>
     * If the time span string could not be parsed or uses other suffixes than supported, an
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param timeSpan     the string representing the time span
     * @param timeUnit     the {@link java.util.concurrent.TimeUnit} to be used
     * @param defaultValue the default value to use if the time span string was null or empty.
     * @return the time span measured in the given {@link java.util.concurrent.TimeUnit}.
     */
    public static long parse(final String timeSpan, final TimeUnit timeUnit, final long defaultValue) {
        if (timeSpan == null || timeSpan.isEmpty()) {
            return defaultValue;
        }
        final Matcher m = PATTERN.matcher(timeSpan);
        long value = 0;
        if (m.matches()) {
            for (int i = 0; i < m.groupCount(); i++) {
                if (null != m.group(i + 1)) {
                    value += TIME_UNITS.get(i).toSeconds(Long.valueOf(m.group(i + 1)));
                }
            }
        } else {
            throw new IllegalArgumentException("'" + timeSpan + "' is not a valid timespan. It must match the pattern '"
                    + PATTERN + "'.");
        }
        return timeUnit.convert(value, TimeUnit.SECONDS);
    }

    /**
     * Calls {@link org.n3r.aoc.utils.TimeSpanParser#parse(String, java.util.concurrent.TimeUnit, long)} with default 0.
     */
    public static long parse(final String timeSpan, final TimeUnit timeUnit) {
        return parse(timeSpan, timeUnit, 0);
    }

}