package com.clouway.task1;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class User {

  public final int id;
  public final String name;
  public final int age;
  public final String address;
  public final String e_mail;


  public User(int id,String name,int age,String address,String e_mail) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
    this.e_mail = e_mail;
  }

//  public int getId() {
//    return id;
//  }
//
//  public String getName() {
//    return name;
//  }
//
//  public int getAge() {
//    return age;
//  }
//
//  public String getAddress() {
//    return address;
//  }
//
//  public String getE_mail() {
//    return e_mail;
//  }
}
