package com.clouway.task1;


//import com.clouway.task1.DataStore.RowFetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class PersistentUserDatabase implements PersistentUserRepository {

  private final DataStore dataStore;

  public PersistentUserDatabase(DataStore dataStore) {
    this.dataStore = dataStore;

  }


  @Override
  public void register(User user) throws SQLException {
      dataStore.execute("insert into user_info(id,name,age,address,e_mail)  values (" + user.id + ",'" + user.name + "'," + user.age + ",'" +
              user.address + "','" + user.e_mail + "')");

//    try {
//      String sql = "insert into user_info(id,name,age,address,e_mail)  values (" + user.id + ",'" + user.name + "'," + user.age + ",'" +
//              user.address + "','" + user.e_mail + "')";
//      statement.executeUpdate(sql);
//      System.out.println("\n" + sql);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } finally {
//      try {
//        connection.close();
//        statement.close();
//      } catch (SQLException e) {
//        e.printStackTrace();
//      }
//    }
  }

  @Override
  public void update(User user, String name) {
    dataStore.execute("update user_info set name='" + name + "' where name='" + user.name + "'");
//    Connection connection = provider.get();
//
//    try {
//      PreparedStatement pr = connection.prepareStatement("update user_info set name=? where id=?");
//      pr.setString(1, name);
//      pr.setInt(2, user.id);
//      pr.execute();
//      pr.close();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
  }


  @Override
  public void remove(User user) {
    dataStore.execute("delete from user_info where id=" + user.id );

//    try {
//      connection.createStatement().execute("delete from user_info where id=" + user.id);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
  }


  public void dropTable(String newTable) {
//    Connection connection = provider.get();
//    try {
//      connection.createStatement().execute("drop table " + newTable);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
    dataStore.execute("drop table " + newTable);
  }

  @Override
  public List<User> findByProperty(int age, String retrieveAge) {

//    Connection connection = provider.get();
//    try {
//      Statement statement = connection.createStatement();
//      ResultSet rs = statement.executeQuery("select * from user_info where " + age + "::text like '" + retrieveAge + "'");
//      manipulator(rs,list);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return list;
   return dataStore.fetchRow("select * from user_info where " + age + "::text like '" + retrieveAge + "'",new RowFetcher<User>() {
     @Override
     public User fetchRow(ResultSet rs) throws SQLException {
       int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String address = rs.getString("address");
        String email = rs.getString("e_mail");
        return new User(id,name,age,address,email);
     }
   });

  }

  @Override
  public List<User> retrieveUsersByAge(int age) {
//    List<User> list = new ArrayList<User>();
//    Connection connection = provider.get();
//    try {
//      Statement statement = connection.createStatement();
//      ResultSet rs = statement.executeQuery("select * from user_info where " + age + " >=25 ");
//      manipulator(rs,list);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return list;
    return dataStore.fetchRow("select * from user_info where " + age + " >=25 ",new RowFetcher<User>() {
      @Override
      public User fetchRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String address = rs.getString("address");
        String email = rs.getString("e_mail");
        return new User(id,name,age,address,email);
      }
    });
  }

  @Override
  public List<User> getAll() {
//    List<User> list = new ArrayList<User>();
//    Connection connection = provider.get();
//    try {
//      Statement statement = connection.createStatement();
//      ResultSet rs = statement.executeQuery("select * from user_info");
//      manipulator(rs,list);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return list;
//    return fetchRow("select * from user_info", new RowFetcher<User>(){
//      @Override
//      public User fetchRow(ResultSet rs) throws SQLException{
//        int id = rs.getInt("id");
//        String name = rs.getString("name");
//        int age = rs.getInt("age");
//        String address = rs.getString("address");
//        String email = rs.getString("e_mail");
//        return new User(id,name,age,address,email);
//      }
//    });
    return dataStore.findAll("user_info", new RowFetcher<User>() {
      @Override
      public User fetchRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int age = rs.getInt("age");
        String address = rs.getString("address");
        String email = rs.getString("e_mail");
        return new User(id,name,age,address,email);
      }
    });
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

//  private <T> List<T> fetchRow(String sql, RowFetcher<T> rowFetcher) {
//    Connection connection = provider.get();
//    List<T> list = new ArrayList<T>();
//    try {
//      Statement stmt = connection.createStatement();
//      ResultSet rs = stmt.executeQuery(sql);
//      while (rs.next()){
//        T item = rowFetcher.fetchRow(rs);
//        list.add(item);
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//    return list;
//  }




}
