package com.clauway.task2.task3;

import java.sql.Date;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Trip {

  public final int ucn;
  public final Date arrivalDate;
  public final Date departureDate;
  public final String city;

  public Trip(int ucn,Date arrivalDate,Date departureDate,String city) {
    this.ucn = ucn;
    this.arrivalDate = arrivalDate;
    this.departureDate = departureDate;
    this.city = city;
  }
}
