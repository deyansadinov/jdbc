package com.clauway.task2.task3;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class TravelAgency implements PersonRepository, TripRepository {


  private final DataStore dataStore;

  public TravelAgency(DataStore dataStore) {
    this.dataStore = dataStore;
  }


  @Override
  public void addPerson(Person person) {
    dataStore.execute("insert into person (name,ucn,age,user_email) values ('" + person.name + "'," + person.ucn + "," +
            person.age + ",'" + person.email + "')");
  }

  @Override
  public List<Person> findPerson() {
    return dataStore.findAll("person", new RowFetcher<Person>() {
      @Override
      public Person fetchRow(ResultSet rs) throws SQLException {
         String name = rs.getString(1);
         int ucn = rs.getInt(2);
         int age = rs.getInt(3);
         String email = rs.getString(4);
        return new Person(name, ucn, age, email);
      }
    });
  }

  @Override
  public void updatePerson(Person person, int age) {
    dataStore.execute("update person set age=" + age + " where name='" + person.name + "'");
  }

  @Override
  public List<Person> findPersonWhichNameStartsWith(String letter) {
    return dataStore.fetchRow("select * from person where name::text like '" + letter + "%'", new RowFetcher<Person>() {
      @Override
      public Person fetchRow(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        int ucn = rs.getInt(2);
        int age = rs.getInt(3);
        String email = rs.getString(4);
        return new Person(name, ucn, age, email);
      }
    });
  }

  @Override
  public List<Person> findPersonBy(String city, Date date) {
    return dataStore.fetchRow("select * from person where ucn in (select ucn from trip where city='" + city + "' and " +
              "date_of_arrival='" + date + "')", new RowFetcher<Person>() {

      @Override
      public Person fetchRow(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        int ucn = rs.getInt(2);
        int age = rs.getInt(3);
        String email = rs.getString(4);
        return new Person(name, ucn, age, email);
      }
    });
  }

  @Override
  public void addTrip(Trip trip) {
    dataStore.execute("insert into trip(ucn, date_of_arrival,date_of_departure,city) values (" + trip.ucn + ",'"+trip.arrivalDate+"','" +
            trip.departureDate + "','" + trip.city + "')");
  }

  @Override
  public List<Person> findPeopleAtSameCityAtSameDate(String city, Date startDate, Date endDate) {
    return dataStore.fetchRow("select * from person join trip on person.ucn=trip.ucn where city='" + city + "' " +
              "and (date_of_arrival, date_of_departure) overlaps ('" + startDate + "', '" + endDate + "')",new RowFetcher<Person>() {
      @Override
      public Person fetchRow(ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        int ucn = rs.getInt(2);
        int age = rs.getInt(3);
        String email = rs.getString(4);
        return new Person(name, ucn, age, email);
      }
    });
  }

  @Override
  public List<String> listCitiesDescendingTripCount() {
    return dataStore.fetchRow("select city from trip group by city order by count(ucn) desc",new RowFetcher<String>() {
      @Override
      public String fetchRow(ResultSet rs) throws SQLException {
        return rs.getString(1);
      }
    });
  }

  @Override
  public List<Trip> findTrips() {
    return dataStore.findAll("trip",new RowFetcher<Trip>() {
      @Override
      public Trip fetchRow(ResultSet rs) throws SQLException {
         int ucn = rs.getInt(1);
         Date arrivalDate = rs.getDate(2);
         Date departureDate = rs.getDate(3);
         String city = rs.getString(4);
        return new Trip(ucn,arrivalDate,departureDate,city);
      }
    });
  }

  @Override
  public void updateCityFromTrip(Trip trip, String city) {
    dataStore.execute("update trip set city='" + city + "' where ucn=" + trip.ucn);
  }
}
