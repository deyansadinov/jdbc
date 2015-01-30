import com.clouway.task6.Car;
import com.clouway.task6.PersistentCar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class PersistentCarTest {

  private PersistentCar persistentCar;
  private Connection connection;

  @Before
  public void setUp() {
    persistentCar = new PersistentCar("car");
    connection = persistentCar.connection("postgres", "123456");
  }

  @After
  public void tearDown() throws SQLException {
    connection.createStatement().execute("truncate table car restart identity");
  }

  @Test
  public void register() {
    persistentCar.register(new Car(1, 118, 1999));

    List<Car> result = persistentCar.findAll();

    assertThat(result.size(), is(1));
  }

  @Test
  public void register1MillionRows() throws SQLException {

    persistentCar.register1MillionRows(new Car(randomInt(1000000), randomInt(232353), randomInt(34255)));

//    List<Car> result = persistentCar.findAll();

//    assertThat(result.size(), is(1000000));

  }

  private int randomInt(int value) {
    Random random = new Random();
    return random.nextInt(value);
  }

}
