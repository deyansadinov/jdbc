package com.clouway.task4;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Contact {

  private final int user_id;
  private final int contact_id;

  public Contact(int user_id,int contact_id) {
    this.user_id = user_id;
    this.contact_id = contact_id;
  }

  public int getUser_id() {
    return user_id;
  }

  public int getContact_id() {
    return contact_id;
  }
}
