import com.clauway.task2.task3.ConnectionProvider;
import com.clauway.task2.task3.DataStore;
import com.clauway.task2.task3.TravelAgency;
import com.clauway.task2.task3.DateCalendar;
import com.clauway.task2.task3.Person;
import com.clauway.task2.task3.Trip;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
public class TravelAgencyTest {

  private TravelAgency travelAgency;
  private Connection connection;
  private Person person = new Person("dido", 38746387, 26, "sss@abv.bg");
  private DateCalendar date;


  @Before
  public void setUp() {
    date = new DateCalendar();
    ConnectionProvider provider = new ConnectionProvider();
    DataStore dataStore = new DataStore(provider);
    travelAgency = new TravelAgency(dataStore);
    connection = provider.get();


  }

  @After
  public void tearDown() throws SQLException {
    connection.createStatement().execute("truncate person,trip restart identity");
  }

  @Test
  public void addPerson() throws SQLException {
    travelAgency.addPerson(person);

    List<Person> list = travelAgency.findPerson();

    assertThat(list.size(), is(1));
    assertThat(list.get(0).name, is("dido"));
  }

  @Test
  public void addMultiplePersons() throws SQLException {
    travelAgency.addPerson(person);
    travelAgency.addPerson(new Person("kalin",234456676,20,"kkk@abv.bg"));

    List<Person> list = travelAgency.findPerson();

    assertThat(list.size(),is(2));
    assertThat(list.get(0).name,is("dido"));
    assertThat(list.get(1).name,is("kalin"));
  }

  @Test
  public void updatePersonAge() throws SQLException {
    travelAgency.addPerson(person);
    List<Person> list = travelAgency.findPerson();
    assertThat(list.get(0).age,is(26));

    travelAgency.updatePerson(person, 27);
    list = travelAgency.findPerson();

    assertThat(list.get(0).age, is(27));
  }

  @Test
  public void findPersonByLetters() throws SQLException {
    travelAgency.addPerson(person);

    List<Person> list = travelAgency.findPersonWhichNameStartsWith("d");

    assertThat(list.size(), is(1));
    assertThat(list.get(0).name,is("dido"));
  }

  @Test
  public void findMultiplePersonsByLetters() throws SQLException {
    travelAgency.addPerson(person);
    travelAgency.addPerson(new Person("danail", 45389437, 24, "dd@abv.bg"));

    List<Person> list = travelAgency.findPersonWhichNameStartsWith("d");

    assertThat(list.size(), is(2));
    assertThat(list.get(1).name,is("danail"));
  }

  @Test
  public void registerPersonForTrip() throws SQLException {
    travelAgency.addPerson(person);
    travelAgency.addTrip(new Trip(38746387,date.getDate(2015, 1, 12), date.getDate(2015, 1, 20),"tyrnovo"));

    List<Trip>list = travelAgency.findTrips();

    assertThat(list.size(),is(1));
    assertThat(list.get(0).city,is("tyrnovo"));
  }

  @Test
  public void registerMultiplePersonsForTrips() throws SQLException {
    travelAgency.addPerson(person);
    travelAgency.addPerson(new Person("danail", 45389437, 24, "dd@abv.bg"));

    travelAgency.addTrip(new Trip(38746387, date.getDate(2015, 1, 12), date.getDate(2015, 1, 20), "Plovdiv"));
    travelAgency.addTrip(new Trip(45389437, date.getDate(2015, 1, 10), date.getDate(2015, 1, 20), "Varna"));

    List<Trip>list = travelAgency.findTrips();

    assertThat(list.size(),is(2));
    assertThat(list.get(0).city,is("Plovdiv"));
    assertThat(list.get(1).city,is("Varna"));
  }

  @Test
  public void updateCityOfTheTrip() throws SQLException {
    travelAgency.addPerson(person);
    Trip plovdiv = new Trip(38746387, date.getDate(2015, 1, 12), date.getDate(2015, 1, 20), "Plovdiv");
    travelAgency.addTrip(plovdiv);

    List<Trip>list = travelAgency.findTrips();
    assertThat(list.get(0).city,is("Plovdiv"));

    travelAgency.updateCityFromTrip(plovdiv,"Varna");
    list = travelAgency.findTrips();
    assertThat(list.get(0).city,is("Varna"));
  }


  @Test
  public void findPeopleAtSameCityAtSameDate() throws SQLException {
    travelAgency.addPerson(person);
    travelAgency.addPerson(new Person("gosho", 1, 24, "ggg@abv.bg"));
    travelAgency.addPerson(new Person("kalin", 2, 34, "kkk@abv.bg"));
    travelAgency.addPerson(new Person("petyr", 3, 30, "ppp@abv.bg"));


    travelAgency.addTrip(new Trip(38746387, date.getDate(2015, 1, 13), date.getDate(2015, 1, 25), "Amsterdam"));
    travelAgency.addTrip(new Trip(1, date.getDate(2015, 1, 12), date.getDate(2015, 1, 20), "Amsterdam"));
    travelAgency.addTrip(new Trip(2, date.getDate(2015, 1, 10), date.getDate(2015, 1, 20), "Amsterdam"));
    travelAgency.addTrip(new Trip(3, date.getDate(2015, 1, 10), date.getDate(2015, 1, 25), "Berlin"));

    List<Person> list = travelAgency.findPeopleAtSameCityAtSameDate("Amsterdam", date.getDate(2015, 1, 15), date.getDate(2015, 1, 22));
    assertThat(list.size(), is(3));
    assertThat(list.get(1).name,is("gosho"));
  }

  @Test
  public void listCitiesOrderedByNumberOfTrips() throws SQLException {

    travelAgency.addPerson(new Person("gosho", 1, 24, "ggg@abv.bg"));
    travelAgency.addPerson(new Person("kalin", 2, 34, "kkk@abv.bg"));
    travelAgency.addPerson(new Person("petyr", 3, 30, "ppp@abv.bg"));

    travelAgency.addTrip(new Trip(1, date.getDate(2015, 1, 20), date.getDate(2015, 1, 25), "Amsterdam"));
    travelAgency.addTrip(new Trip(2, date.getDate(2015, 1, 12), date.getDate(2015, 1, 20), "Amsterdam"));
    travelAgency.addTrip(new Trip(3, date.getDate(2015, 1, 10), date.getDate(2015, 1, 25), "Berlin"));

    List<String> result = travelAgency.listCitiesDescendingTripCount();

    assertThat(result.size(), is(2));
    assertThat(result.get(0), is("Amsterdam"));
    assertThat(result.get(1), is("Berlin"));

  }

  @Test
  public void findPersonByCityAndDate() throws SQLException {
    travelAgency.addPerson(person);
    Date dateArrive = date.getDate(2015, 1, 20);
    Date dateLeaving = date.getDate(2015, 1, 25);
    travelAgency.addTrip(new Trip(38746387, dateArrive, dateLeaving, "Amsterdam"));

    List<Person> result = travelAgency.findPersonBy("Amsterdam", dateArrive);

    assertThat(result.size(), is(1));
    assertThat(result.get(0).name,is("dido"));
  }

  @Test
  public void addTripToPersonWhoDoNotExist() throws SQLException {
    Date dateArrive = date.getDate(2015, 1, 20);
    Date dateLeaving = date.getDate(2015, 1, 25);
    travelAgency.addTrip(new Trip(38746387, dateArrive, dateLeaving, "Amsterdam"));

    List<Trip> result = travelAgency.findTrips();

    assertThat(result.size(), is(0));
  }
}

