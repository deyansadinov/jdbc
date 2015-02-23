package task1;

import com.clouway.task1.ConnectionProvider;
import com.clouway.task1.DataStore;
import com.clouway.task1.PersistentUserDatabase;
import com.clouway.task1.User;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PersistentUserDatabaseTest {


  private PersistentUserDatabase userDatabase;
  private Connection connection;
  private User user = new User(1, "dido", 26, "Bulgaria", "sss@abv.bg");

  @Rule
  public DataStoreRule dataStoreRule = new DataStoreRule();

  @org.junit.Before
  public void setUp() throws Exception {
    ConnectionProvider connectionProvider = new ConnectionProvider();
    DataStore dataStore = new DataStore(connectionProvider);
    userDatabase = new PersistentUserDatabase(dataStore);
    connection = dataStoreRule.getConnection();
  }


  @Test
  public void registerUser() throws SQLException {
    userDatabase.register(user);

    List<User> lists = userDatabase.getAll();
    assertThat(lists.size(), is(1));
    assertThat(lists.get(0).name, is("dido"));
  }

  @Test
  public void removeUser() throws SQLException {

    userDatabase.register(user);
    userDatabase.remove(user);

    List<User> list = userDatabase.getAll();
    assertThat(list.size(), is(0));
  }

  @Test
  public void updateUserName()throws SQLException {
    userDatabase.register(user);
    userDatabase.updateUserName(user, "Ivan");

    List<User> list = userDatabase.getAll();
    assertThat(list.get(0).name, is("Ivan"));
  }

  @Test
  public void dropTable() {
    String newTable = "newTable";
    createTable(newTable);
    userDatabase.dropTable(newTable);

    boolean exists = table(newTable);

    assertThat(exists, is(false));

  }

  @Test
  public void findUserAge() throws SQLException{
    userDatabase.register(user);
    userDatabase.register(new User(2,"Kalin",24,"Bulgaria","kkk@abv.bg"));

    List<User> list = userDatabase.findUserAge("2%");

    assertThat(list.size(),is(2));
    assertThat(list.get(0).age,is(26));
  }

  @Test
  public void retrieveUsersAge() throws SQLException{
    userDatabase.register(user);
    userDatabase.register(new User(2,"Kalin",24,"Bulgaria","kkk@abv.bg"));

    List<User> list = userDatabase.retrieveUsersAge(20);

    assertThat(list.size(),is(2));
    assertThat(list.get(0).age,is(26));
    assertThat(list.get(1).age,is(24));
  }



  private boolean table(String newTable) {
    try {
      connection.createStatement().execute("select * from " + newTable);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  private void createTable(String newTable) {
    try {
      connection.createStatement().execute("create table " + newTable + "(id integer primary key)");
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}