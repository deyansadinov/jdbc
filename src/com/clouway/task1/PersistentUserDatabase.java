package com.clouway.task1;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class PersistentUserDatabase implements PersistentUserRepository<User> {

  private final ConnectionProvider provider;


  public PersistentUserDatabase(ConnectionProvider provider) {
    this.provider = provider;
  }


  @Override
  public void register(User user) throws SQLException {
    Connection connection = provider.get();
    Statement statement = connection.createStatement();
    try {
      String sql = "insert into user_info(id,name,age,address,e_mail)  values (" + user.id + ",'" + user.name + "'," + user.age + ",'" +
              user.address + "','" + user.e_mail + "')";
      statement.executeUpdate(sql);
      System.out.println("\n" + sql);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        connection.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void update(User user, String name) {
    Connection connection = provider.get();

    try {
      PreparedStatement pr = connection.prepareStatement("update user_info set name=? where id=?");
      pr.setString(1, name);
      pr.setInt(2, user.id);
      pr.execute();
      pr.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void remove(User user) {
    Connection connection = provider.get();

    try {
      connection.createStatement().execute("delete from user_info where id=" + user.id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public void dropTable(String newTable) {
    Connection connection = provider.get();
    try {
      connection.createStatement().execute("drop table " + newTable);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<User> findByProperty(int age, String retrieveAge) {
    List<User> list = new ArrayList<User>();
    Connection connection = provider.get();
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from user_info where " + age + "::text like '" + retrieveAge + "'");
      manipulator(rs,list);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public List<User> retrieveUsersByAge(int age) {
    List<User> list = new ArrayList<User>();
    Connection connection = provider.get();
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from user_info where " + age + " >=25 ");
      manipulator(rs,list);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public List<User> getAll() {
    List<User> list = new ArrayList<User>();
    Connection connection = provider.get();
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from user_info");
      manipulator(rs,list);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  private void manipulator(ResultSet rs,List<User> list) {
    try {
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String address = rs.getString("address");
        String email = rs.getString("e_mail");
        list.add(new User(id, name, age, address, email));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


}
