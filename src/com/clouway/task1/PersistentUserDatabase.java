package com.clouway.task1;



import java.sql.Connection;
import java.sql.DriverManager;
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
  private final String tableName;
  private Statement statement;
  private List<User> list = new ArrayList<User>();


  public PersistentUserDatabase(ConnectionProvider provider,String tableName) {
    this.provider = provider;
    this.tableName = tableName;
  }


  @Override
  public void register(User user) {
      Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
      String sql = "insert into " + tableName + "(id,name,age,address,e_mail)  values (" + user.id + ",'" + user.name + "'," + user.age + ",'" +
              user.address + "','" + user.e_mail + "')";
      statement.executeUpdate(sql);
      System.out.println("\n" + sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void update(User user, String name) {
    Connection connection = provider.connect();

    try {
      statement = connection.createStatement();
      PreparedStatement pr = connection.prepareStatement("update " + tableName + " set name=? where id=?");
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
    Connection connection = provider.connect();

    try {
      connection.createStatement().execute("delete from " + tableName + " where id=" + user.id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public void dropTable(String newTable) {
    Connection connection = provider.connect();
    try {
      connection.createStatement().execute("drop table " + newTable);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<User> findByProperty(int age, String retrieveAge) {
    Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from " + tableName + " where " + age + "::text like '" + retrieveAge + "'" );
      manipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public List<User> retrieveUsersByAge(int age) {
    Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from " + tableName + " where " + age + " >=25 ");
      manipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  @Override
  public List<User> getAll() {
    Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("select * from " + tableName);
      manipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  private void manipulator(ResultSet rs){
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
