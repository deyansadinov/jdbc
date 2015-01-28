package com.clouway.task4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class ContactsDatabase {

  private final String userTable;
  private final String addressTable;
  private final String contactTable;
  private Connection connection;
  private ResultSet rs;
  private PreparedStatement pr;

  public ContactsDatabase(String addressTable,String contactTable,String userTable) {
    this.userTable = userTable;
    this.addressTable = addressTable;
    this.contactTable = contactTable;
  }

  public Connection connection(String user,String pass){
    try {
      connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/didodb",user,pass);
      return  connection;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void registerUser(User user) {
    try {
      pr = connection.prepareStatement("insert into users(id,name) values (" + user.getId() + ",'" + user.getName() + "')");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public List<User> findUsers() {
    List<User> list = new ArrayList<User>();
    try {
      rs = connection.createStatement().executeQuery("select * from users");
      while (rs.next()){
        int id = rs.getInt("id");
        String name = rs.getString("name");
        list.add(new User(id,name));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void registerAddress(Address address) {
    try {
      pr = connection.prepareStatement("insert into addresses(id,address,city) values (" + address.getId() + ",'" + address.getAddress()
                                      + "','" + address.getCity() + "')" );
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Address> findAddress() {
    List<Address> list = new ArrayList<Address>();
    try {
      rs = connection.createStatement().executeQuery("select * from addresses");
      while (rs.next()){
        int id = rs.getInt("id");
        String address = rs.getString("address");
        String city = rs.getString("city");
        list.add(new Address(id,address,city));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void registerContact(Contact contact) {
    try {
      pr = connection.prepareStatement("insert into contacts(user_id,contact_id) values (" + contact.getUser_id() + "," + contact.getContact_id() + ")");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Contact> findContact() {
    List<Contact> list = new ArrayList<Contact>();
    try {
      rs = connection.createStatement().executeQuery("select users.id as user_id,addresses.id as contact_id from users inner join contacts on contacts.user_id=users.id " +
              "inner join addresses on contacts.contact_id=addresses.id");
      while (rs.next()){
        int user_id = rs.getInt("user_id");
        int contact_id = rs.getInt("contact_id");
        list.add(new Contact(user_id,contact_id));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }
}
