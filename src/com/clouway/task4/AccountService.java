package com.clouway.task4;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class AccountService {


  private final DataStore dataStore;

  public AccountService(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  public void registerUser(User user) {
    dataStore.execute("insert into users(id,name) values (" + user.id + ",'" + user.name + "')");
  }

  public List<User> findUsers() {
    return dataStore.findAll("users", new RowFetcher<User>() {
      int id = 0;
      String name;
      @Override
      public User fetchRow(ResultSet rs) {
        try {
          id = rs.getInt(1);
          name = rs.getString(2);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return new User(id, name);
      }
    });
  }

  public void registerAddress(Address address) {
    dataStore.execute("insert into addresses(id,address,city) values (" + address.id + ",'" + address.address
            + "','" + address.city + "')");
  }

  public List<Address> findAddress() {
    return dataStore.findAll("addresses", new RowFetcher<Address>() {
      int id = 0;
      String address;
      String city;
      @Override
      public Address fetchRow(ResultSet rs) {
        try {
          id = rs.getInt(1);
          address = rs.getString(2);
          city = rs.getString(3);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return new Address(id, address, city);
      }
    });
  }

  public List<Address> findAddress(String street, String city) {
    return dataStore.fetchRow("select * from addresses where address='" + street + "' and city='" + city + "'", new RowFetcher<Address>() {
      int id = 0;
      String address;
      String city;
      @Override
      public Address fetchRow(ResultSet rs) {
        try {
          id = rs.getInt(1);
          address = rs.getString(2);
          city = rs.getString(3);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return new Address(id, address, city);
      }
    });
  }

  public void registerContact(Contact contact) {
    dataStore.execute("insert into contacts(user_id,address_id) values (" + contact.user.id + "," + contact.address.id + ")");
  }

  public List<Contact> findContact() {
    return dataStore.fetchRow("select users.id, users.name, addresses.id, addresses.address, addresses.city " +
            "from users, addresses, contacts where contacts.user_id=users.id and contacts.address_id=addresses.id", new RowFetcher<Contact>() {
      int userID = 0;
      String userName;
      int addressID = 0;
      String address;
      String city;
      @Override
      public Contact fetchRow(ResultSet rs) {
        try {
          userID = rs.getInt(1);
          userName = rs.getString(2);
          addressID = rs.getInt(3);
          address = rs.getString(4);
          city = rs.getString(5);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return new Contact(new User(userID, userName), new Address(addressID, address, city));
      }
    });
  }

  public List<User> findUsersByAddress(String street, String city) {
    return dataStore.fetchRow("select users.id,users.name from users,addresses,contacts where contacts.user_id=users.id and contacts.address_id=addresses.id " +
            "and addresses.address='" + street + "' and addresses.city='" + city + "'", new RowFetcher<User>() {
      int id = 0;
      String name;
      @Override
      public User fetchRow(ResultSet rs) {
        try {
          id = rs.getInt(1);
          name = rs.getString(2);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return new User(id,name);
      }
    });
  }
}
