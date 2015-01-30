import com.clauway.task2.task3.ConnectionProvider;
import com.clauway.task2.task3.Database;
import com.clauway.task2.task3.DateCalendar;
import com.clauway.task2.task3.Person;
import com.clauway.task2.task3.Trip;
import com.sun.deploy.perf.PerfRollup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class DatabaseTest {

  private Database database;
  private Connection connection;
  private Person person = new Person("dido",38746387,26,"sss@abv.bg");
//  private Trip trip = new Trip(38746387,"2015-01-20","2015-01-25","Amsterdam");
  private DateCalendar date;


  @Before
  public void setUp(){
    date = new DateCalendar();
    ConnectionProvider provider = new ConnectionProvider();
    database = new Database(provider);
    connection = provider.connect();


  }

  @After
  public void tearDown() throws SQLException {
    connection.createStatement().execute("truncate people,trip restart identity");
  }

  @Test
  public void addPerson() {
    database.addPerson(person);

    List<Person> list = database.findPeople();

    assertThat(list.size(),is(1));
    assertThat(list.get(0).name,is("dido"));
  }

  @Test
  public void updatePersonAge() {
    database.addPerson(person);
    database.updatePerson(person,27);

    List<Person> list = database.findPeople();

    assertThat(list.get(0).age,is(27));
  }

  @Test
  public void findPersonByLetters() {
    database.addPerson(person);

    List<Person> list = database.findByLetters("d");

    assertThat(list.size(),is(1));
  }

  @Test
  public void findMorePersonsByLetters() {
    database.addPerson(person);
    database.addPerson(new Person("danail",45389437,24,"dd@abv.bg"));

    List<Person> list = database.findByLetters("d");

    assertThat(list.size(),is(2));
  }


  @Test
  public void findPeopleAtSameCityAtSameDate() {
    database.addPerson(person);
    database.addPerson(new Person("gosho",1,24,"ggg@abv.bg"));
    database.addPerson(new Person("kalin",2,34,"kkk@abv.bg"));
    database.addPerson(new Person("petyr",3,30,"ppp@abv.bg"));

    Date dateArrive1 = date.getDate(2015, 1,20);
    Date dateArrive2 = date.getDate(2015, 1,12);
    Date dateArrive3 = date.getDate(2015, 1,10);
    Date dateArrive4 = date.getDate(2015, 1,10);

    Date dateLeaving1 = date.getDate(2015,1,25);
    Date dateLeaving2 = date.getDate(2015,1,20);
    Date dateLeaving3 = date.getDate(2015,1,20);
    Date dateLeaving4 = date.getDate(2015,1,25);

    database.addTrip(new Trip(38746387,dateArrive1,dateLeaving1,"Amsterdam"));
    database.addTrip(new Trip(1,dateArrive2,dateLeaving2,"Amsterdam"));
    database.addTrip(new Trip(2,dateArrive3,dateLeaving3,"Amsterdam"));
    database.addTrip(new Trip(3,dateArrive4,dateLeaving4,"Berlin"));

    List<Person> list = database.findPeopleAtSameCityAtSameDate("Amsterdam",dateArrive2);
   assertThat(list.size(), is(2));

  }
  
  @Test
  public void listCitiesOrderedByNumberOfTrips() {

    database.addPerson(new Person("gosho",1,24,"ggg@abv.bg"));
    database.addPerson(new Person("kalin",2,34,"kkk@abv.bg"));
    database.addPerson(new Person("petyr",3,30,"ppp@abv.bg"));

    Date dateArrive1 = date.getDate(2015, 1,20);
    Date dateArrive2 = date.getDate(2015, 1,12);
    Date dateArrive3 = date.getDate(2015, 1,10);
    Date dateArrive4 = date.getDate(2015, 1,10);

    Date dateLeaving1 = date.getDate(2015,1,25);
    Date dateLeaving2 = date.getDate(2015,1,20);
    Date dateLeaving3 = date.getDate(2015,1,20);
    Date dateLeaving4 = date.getDate(2015,1,25);

    database.addTrip(new Trip(1,dateArrive1,dateLeaving1,"Amsterdam"));
    database.addTrip(new Trip(2,dateArrive2,dateLeaving2,"Amsterdam"));
    database.addTrip(new Trip(3,dateArrive3,dateLeaving3,"Amsterdam"));
    database.addTrip(new Trip(3,dateArrive4,dateLeaving4,"Berlin"));


    List<String> result = database.listCitiesDescendingTripCount();

    assertThat(result.size(),is(2));
    assertThat(result.get(0),is("Amsterdam"));
    assertThat(result.get(1),is("Berlin"));

  }

  @Test
  public void findCitiesByDate() {
    database.addPerson(person);
    Date dateArrive = date.getDate(2015, 1,20);
    Date dateLeaving = date.getDate(2015,1,25);
    database.addTrip(new Trip(38746387,dateArrive,dateLeaving,"Amsterdam"));

    List<Person> result = database.findCitiesByDate("Amsterdam",dateArrive);

    assertThat(result.size(),is(1));
  }
  
  @Test
  public void addTripToPersonWhoDoNotExist() throws SQLException {
    Date dateArrive = date.getDate(2015, 1,20);
    Date dateLeaving = date.getDate(2015,1,25);
    database.addTrip(new Trip(38746387,dateArrive,dateLeaving,"Amsterdam"));

    List<Trip> result = database.findTrips();

    assertThat(result.size(),is(0));
  }

}

