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
  public void register(User user)  {
      dataStore.execute("insert into user_info(id,name,age,address,e_mail)  values (" + user.id + ",'" + user.name + "'," + user.age + ",'" +
              user.address + "','" + user.e_mail + "')");
  }

  @Override
  public void updateUserName(User user, String name) {
    dataStore.execute("update user_info set name='" + name + "' where name='" + user.name + "'");
  }

  @Override
  public void remove(User user) {
    dataStore.execute("delete from user_info where id=" + user.id );
  }


  public void dropTable(String newTable) {
    dataStore.execute("drop table " + newTable);
  }

  @Override
  public List<User> findUserAge(String retrieveAge) {
   return dataStore.fetchRow("select * from user_info where age::text like '" + retrieveAge + "'",new RowFetcher<User>() {
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
  public List<User> retrieveUsersAge(int age) {
    return dataStore.fetchRow("select * from user_info where age>=" + age,new RowFetcher<User>() {
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
}
