import com.clauway.task2.task3.ConnectionProvider;
import com.clauway.task2.task3.PersistentRepository;
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
public class PersistentRepositoryTest {

  private PersistentRepository persistentRepository;
  private Connection connection;
  private Person person = new Person("dido",38746387,26,"sss@abv.bg");
  private DateCalendar date;


  @Before
  public void setUp(){
    date = new DateCalendar();
    ConnectionProvider provider = new ConnectionProvider();
    persistentRepository = new PersistentRepository(provider);
    connection = provider.get();


  }

  @After
  public void tearDown() throws SQLException {
    connection.createStatement().execute("truncate people,trip restart identity");
  }

  @Test
  public void addPerson() throws SQLException {
    persistentRepository.addPerson(person);

    List<Person> list = persistentRepository.findPeople();

    assertThat(list.size(),is(1));
    assertThat(list.get(0).name,is("dido"));
  }

  @Test
  public void updatePersonAge() throws SQLException{
    persistentRepository.addPerson(person);
    persistentRepository.updatePerson(person,27);

    List<Person> list = persistentRepository.findPeople();

    assertThat(list.get(0).age,is(27));
  }

  @Test
  public void findPersonByLetters() throws SQLException{
    persistentRepository.addPerson(person);

    List<Person> list = persistentRepository.findByLetters("d");

    assertThat(list.size(),is(1));
  }

  @Test
  public void findMorePersonsByLetters() throws SQLException{
    persistentRepository.addPerson(person);
    persistentRepository.addPerson(new Person("danail",45389437,24,"dd@abv.bg"));

    List<Person> list = persistentRepository.findByLetters("d");

    assertThat(list.size(),is(2));
  }


  @Test
  public void findPeopleAtSameCityAtSameDate() throws SQLException {
    persistentRepository.addPerson(person);
    persistentRepository.addPerson(new Person("gosho",1,24,"ggg@abv.bg"));
    persistentRepository.addPerson(new Person("kalin",2,34,"kkk@abv.bg"));
    persistentRepository.addPerson(new Person("petyr",3,30,"ppp@abv.bg"));


    persistentRepository.addTrip(new Trip(38746387,date.getDate(2015, 1,20),date.getDate(2015,1,25),"Amsterdam"));
    persistentRepository.addTrip(new Trip(1,date.getDate(2015, 1,12),date.getDate(2015,1,20),"Amsterdam"));
    persistentRepository.addTrip(new Trip(2,date.getDate(2015, 1,10),date.getDate(2015,1,20),"Amsterdam"));
    persistentRepository.addTrip(new Trip(3,date.getDate(2015, 1,10),date.getDate(2015,1,25),"Berlin"));

    List<Person> list = persistentRepository.findPeopleAtSameCityAtSameDate("Amsterdam",date.getDate(2015, 1,12));
   assertThat(list.size(), is(2));

  }
  
  @Test
  public void listCitiesOrderedByNumberOfTrips() throws SQLException{

    persistentRepository.addPerson(new Person("gosho",1,24,"ggg@abv.bg"));
    persistentRepository.addPerson(new Person("kalin",2,34,"kkk@abv.bg"));
    persistentRepository.addPerson(new Person("petyr",3,30,"ppp@abv.bg"));

    Date dateArrive = date.getDate(2015,1,20);
    Date dateLeaving = date.getDate(2015,1,25);

    //version 1
    persistentRepository.addTrip(new Trip(1,date.getDate(2015, 1,20),date.getDate(2015,1,25),"Amsterdam"));
    persistentRepository.addTrip(new Trip(2,date.getDate(2015, 1,12),date.getDate(2015,1,20),"Amsterdam"));
    persistentRepository.addTrip(new Trip(3,date.getDate(2015, 1,10),date.getDate(2015,1,20),"Amsterdam"));
    persistentRepository.addTrip(new Trip(3,date.getDate(2015, 1,10),date.getDate(2015,1,25),"Berlin"));

    //version 2
    persistentRepository.addTrip(new Trip(1,dateArrive,dateLeaving,"Amsterdam"));
    persistentRepository.addTrip(new Trip(2,dateArrive,dateLeaving,"Amsterdam"));
    persistentRepository.addTrip(new Trip(3,dateArrive,dateLeaving,"Amsterdam"));
    persistentRepository.addTrip(new Trip(3,dateArrive,dateLeaving,"Berlin"));


    List<String> result = persistentRepository.listCitiesDescendingTripCount();

    assertThat(result.size(),is(2));
    assertThat(result.get(0),is("Amsterdam"));
    assertThat(result.get(1),is("Berlin"));

  }

  @Test
  public void findCitiesByDate() throws SQLException{
    persistentRepository.addPerson(person);
    Date dateArrive = date.getDate(2015, 1,20);
    Date dateLeaving = date.getDate(2015,1,25);
    persistentRepository.addTrip(new Trip(38746387,dateArrive,dateLeaving,"Amsterdam"));

    List<Person> result = persistentRepository.findCitiesByDate("Amsterdam",dateArrive);

    assertThat(result.size(),is(1));
  }
  
  @Test
  public void addTripToPersonWhoDoNotExist() throws SQLException {
    Date dateArrive = date.getDate(2015, 1,20);
    Date dateLeaving = date.getDate(2015,1,25);
    persistentRepository.addTrip(new Trip(38746387,dateArrive,dateLeaving,"Amsterdam"));

    List<Trip> result = persistentRepository.findTrips();

    assertThat(result.size(),is(0));
  }

}

