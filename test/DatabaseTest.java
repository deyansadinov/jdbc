import com.clauway.task2.task3.Database;
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
  private Trip trip = new Trip(38746387,"2015-01-20","2015-01-25","Amsterdam");

  @Before
  public void setUp(){
    database = new Database("people","trip");
    connection = database.connection("postgres","123456");
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
    assertThat(list.get(0).getName(),is("dido"));
  }

  @Test
  public void updatePersonAge() {
    database.addPerson(person);
    database.updatePerson(person,27);

    List<Person> list = database.findPeople();

    assertThat(list.get(0).getAge(),is(27));
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

    database.addTrip(trip);
    database.addTrip(new Trip(1,"2014-01-12","2014-01-20","Amsterdam"));
    database.addTrip(new Trip(2,"2014-01-10","2014-01-20","Amsterdam"));
    database.addTrip(new Trip(3,"2014-01-10","2014-01-25","Berlin"));

    List<Person> list = database.findPeopleAtSameCityAtSameDate("Amsterdam","2014-01-10");
   assertThat(list.size(), is(3));

  }
  
  @Test
  public void listCitiesOrderedByNumberOfTrips() {

    database.addPerson(new Person("gosho",1,24,"ggg@abv.bg"));
    database.addPerson(new Person("kalin",2,34,"kkk@abv.bg"));
    database.addPerson(new Person("petyr",3,30,"ppp@abv.bg"));


    database.addTrip(new Trip(1,"2014-01-12","2014-01-20","Amsterdam"));
    database.addTrip(new Trip(2,"2014-01-10","2014-01-20","Amsterdam"));
    database.addTrip(new Trip(3,"2014-01-10","2014-01-25","Berlin"));
    database.addTrip(new Trip(1,"2014-01-23","2014-01-30","Varna"));
    database.addTrip(new Trip(3,"2014-01-24","2014-01-30","Berlin"));
    database.addTrip(new Trip(2,"2014-01-25","2014-01-30","Amsterdam"));

    List<String> result = database.listCitiesDescendingTripCount();

    assertThat(result.size(),is(3));
    assertThat(result.get(0),is("Amsterdam"));
    assertThat(result.get(1),is("Berlin"));
    assertThat(result.get(2),is("Varna"));
  }

  @Test
  public void findCitiesByDate() {
    database.addPerson(person);
    database.addTrip(trip);

    List<Person> result = database.findCitiesByDate("Amsterdam","2015-01-20");

    assertThat(result.size(),is(1));
  }
  
  @Test
  public void addTripToPersonWhoDoNotExist() throws SQLException {
    database.addTrip(trip);

    List<Trip> result = database.findTrips();

    assertThat(result.size(),is(0));
  }

}

