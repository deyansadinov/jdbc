package com.clouway.task4;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Address {

  private final int id;
  private final String address;
  private final String city;

  public Address(int id,String address,String city) {
    this.id = id;
    this.address = address;
    this.city = city;
  }

  public int getId() {
    return id;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }
}
