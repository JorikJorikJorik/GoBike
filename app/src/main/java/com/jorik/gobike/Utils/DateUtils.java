package com.jorik.gobike.Utils;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

import com.jorik.gobike.Model.Enum.TypePeriod;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

  public static final String PARSE_DATE_CREATE = "yyyy-MM-dd";
  public static final String PARSE_DATE_GSON = "yyyy-MM-dd'T'HH:mm:ss";

  private static final int COUNT_MILLISECONDS_IN_SECONDS = 1000;
  private static final int FIRST_DAY_IN_MONTH = 1;
  private static final String PARSE_INTERVAL = "HH:mm";
  private static final String UTC = "UTC";
  private static final String EMPTY_RESULT = "";
  private static final String HOUR_INDEX = "h";
  private static final String MINUTE_INDEX = "min";
  private static final String PARSE_DATE_YEAR = "yyyy";
  private static final String PARSE_DATE_MONTH = "MMMM";
  private static final String PARSE_DATE_WEEK = "dd.MM";

  public static Date parseDate(String date, String format) {
    Date parseDate = new Date();
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
      parseDate = simpleDateFormat.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return parseDate;
  }

  public static String formatDate(Date date, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    return simpleDateFormat.format(date);
  }

  public static String parseInterval(long seconds) {
    Date date = new Date(seconds * COUNT_MILLISECONDS_IN_SECONDS);
    return formatDate(date, PARSE_INTERVAL);
  }

  public static String dateToRangTime(long secondsInterval) {
    Date dateInterval = parseDate(parseInterval(secondsInterval), PARSE_INTERVAL);
    Calendar calendar = getInstance();
    calendar.setTime(dateInterval);
    String result = EMPTY_RESULT;

    int hours = calendar.get(HOUR_OF_DAY);
    int minutes = calendar.get(MINUTE);

    if (hours > 0) {
      result += formatTimeRang(hours, HOUR_INDEX);
    }
    if (minutes > 0 || result.isEmpty()) {
      result += formatTimeRang(minutes, MINUTE_INDEX);
    }
    return result;
  }

  private static String formatTimeRang(int value, String index) {
    return String.format("%d %s ", value, index);
  }

  public static String parseDateByTypePeriod(String startDate, int type) {
    Calendar calendar = getInstance();
    TypePeriod typePeriod = TypePeriod.fromValue(type);
    switch (typePeriod) {
      case YEAR:
        convertToValidDate(calendar, startDate, PARSE_DATE_YEAR, MONTH, JANUARY);
        break;
      case MONTH:
        convertToValidDate(calendar, startDate, PARSE_DATE_MONTH, YEAR, getInstance().get(YEAR));
        break;
      case WEEK:
        convertToValidDate(calendar, startDate, PARSE_DATE_WEEK, YEAR, getInstance().get(YEAR));
        break;
    }
    return formatDate(calendar.getTime(), PARSE_DATE_CREATE);
  }

  private static void convertToValidDate(Calendar calendar, String startDate, String formatParse,
      int differentDateKey, int differentDateValue) {
    calendar.setTime(parseDate(startDate, formatParse));
    calendar.set(differentDateKey, differentDateValue);
    calendar.add(DAY_OF_MONTH, FIRST_DAY_IN_MONTH);
  }
}
