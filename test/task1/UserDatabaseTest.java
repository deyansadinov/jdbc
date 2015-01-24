package task1;

import com.clouway.task1.User;
import com.clouway.task1.UserDatabase;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserDatabaseTest {


  private UserDatabase userDatabase;
  private Connection connection;
  private User user = new User(1, "dido", 26, "Bulgaria", "sss@abv.bg");


  @org.junit.Before
  public void setUp() throws Exception {
    userDatabase = new UserDatabase("task1");
    connection = userDatabase.connect("postgres", "123456");
  }

  @org.junit.After
  public void tearDown() throws Exception {
    connection.createStatement().execute("truncate task1");
    connection.close();
  }

  @Test
  public void register() {
    userDatabase.register(user);

    List<User> lists = userDatabase.getAll();
    assertThat(lists.size(), is(1));
    assertThat(lists.get(0).getName(), is("dido"));
  }

  @Test
  public void removeUser() {

    userDatabase.register(user);
    userDatabase.remove(user);

    List<User> list = userDatabase.getAll();
    assertThat(list.size(), is(0));
  }

  @Test
  public void update() {
    userDatabase.register(user);
    userDatabase.update(user, "Ivan");

    List<User> list = userDatabase.getAll();
    assertThat(list.get(0).getName(), is("Ivan"));
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
  public void like() {
    userDatabase.register(user);

    List<User> list = userDatabase.findByProperty(26, "2%");

    assertThat(list.size(),is(1));
  }

  @Test
  public void where() {
    userDatabase.register(user);

    List<User> list = userDatabase.retrieveAge(26);

    assertThat(list.get(0).getAge(),is(26));
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

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}