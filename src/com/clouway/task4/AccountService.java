package com.clouway.task4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class AccountService {

  private final ConnectionProvider provider;

  public AccountService(ConnectionProvider provider) {

    this.provider = provider;
  }

  public void registerUser(User user) {
   Connection connection = provider.get();
    try {
      PreparedStatement pr = connection.prepareStatement("insert into users(id,name) values (" + user.id + ",'" + user.name + "')");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<User> findUsers() {
    Connection connection = provider.get();
    List<User> list = new ArrayList<User>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from users");
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        list.add(new User(id, name));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void registerAddress(Address address) {
    Connection connection = provider.get();
    try {
      PreparedStatement pr = connection.prepareStatement("insert into addresses(id,address,city) values (" + address.id + ",'" + address.address
              + "','" + address.city + "')");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Address> findAddress() {
    Connection connection = provider.get();
    List<Address> list = new ArrayList<Address>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from addresses");
      while (rs.next()) {
        int id = rs.getInt("id");
        String address = rs.getString("address");
        String city = rs.getString("city");
        list.add(new Address(id, address, city));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void registerContact(Contact contact) {
    Connection connection = provider.get();
    try {
      PreparedStatement pr = connection.prepareStatement("insert into contacts(user_id,contact_id) values (" + contact.userId + "," + contact.contactId + ")");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Contact> findContact() {
    Connection connection = provider.get();
    List<Contact> list = new ArrayList<Contact>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select users.id as user_id,addresses.id as contact_id from users inner join contacts on contacts.user_id=users.id " +
              "inner join addresses on contacts.contact_id=addresses.id");
      while (rs.next()) {
        int user_id = rs.getInt("user_id");
        int contact_id = rs.getInt("contact_id");
        list.add(new Contact(user_id, contact_id));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }
}
