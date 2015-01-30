package com.clouway.task6;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Car {

  private final int id;
  private final int power;
  private final int year;

  public Car(int id,int power,int year) {
    this.id = id;
    this.power = power;
    this.year = year;
  }

  public int getId() {
    return id;
  }

  public int getPower() {
    return power;
  }

  public int getYear() {
    return year;
  }
}
