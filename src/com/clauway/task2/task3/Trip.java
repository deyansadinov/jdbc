package com.clauway.task2.task3;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Trip {

  private final int ucn;
  private final String arrivalDate;
  private final String departureDate;
  private final String city;

  public Trip(int ucn,String arrivalDate,String departureDate,String city) {
    this.ucn = ucn;
    this.arrivalDate = arrivalDate;
    this.departureDate = departureDate;
    this.city = city;
  }

  public int getUcn() {
    return ucn;
  }

  public String getArrivalDate() {
    return arrivalDate;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public String getCity() {
    return city;
  }
}
