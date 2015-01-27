package com.clauway.task2.task3;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Person {

  private final String name;
  private final int ucn;
  private final int age;
  private final String email;

  public Person( String name, int ucn, int age, String email) {
    this.name = name;
    this.ucn = ucn;
    this.age = age;
    this.email = email;
  }


  public String getName() {
    return name;
  }

  public int getUcn() {
    return ucn;
  }

  public int getAge() {
    return age;
  }

  public String getEmail() {
    return email;
  }
}
