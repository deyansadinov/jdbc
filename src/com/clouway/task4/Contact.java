package com.clouway.task4;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Contact {
  public final User user;
  public final Address address;

  public Contact(User user, Address address) {
    this.user = user;
    this.address = address;
  }

}
