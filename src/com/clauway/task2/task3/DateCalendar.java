package com.clauway.task2.task3;

import java.sql.Date;
import java.util.Calendar;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class DateCalendar implements DateUtil {
  @Override
  public Date getDate(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR,year);
    calendar.set(Calendar.MONTH,month-1);
    calendar.set(Calendar.DAY_OF_MONTH,day);


    return new Date(calendar.getTime().getTime());
  }
}
