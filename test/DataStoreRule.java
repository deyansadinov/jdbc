import com.clouway.task4.AccountService;
import com.clouway.task4.ConnectionProvider;
import org.junit.rules.ExternalResource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class DataStoreRule extends ExternalResource {

  private Connection connection;

  @Override
  protected void before() throws Throwable {
    connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/didodb", "postgres", "123456");
  }

  @Override
  protected void after() {
    try {
      connection.createStatement().execute("truncate table addresses,contacts,users restart identity");
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  public Connection getConnection() {
    return connection;
  }
}
