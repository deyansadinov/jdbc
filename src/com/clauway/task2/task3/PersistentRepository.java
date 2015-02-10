package com.clauway.task2.task3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class PersistentRepository implements PersonRepository, TripRepository {

  private final ConnectionProvider provider;

  public PersistentRepository(ConnectionProvider provider) {

    this.provider = provider;
  }


  @Override
  public void addUser(Person person) throws SQLException {
    Connection connection = provider.get();
    Statement statement = connection.createStatement();
    try {
      String sql = "insert into people (name,ucn,age,e_mail) values ('" + person.name + "'," + person.ucn + "," +
              person.age + ",'" + person.email + "')";
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
    }
  }


  @Override
  public List<Person> findUsers() {
    List<Person> personList = new ArrayList<Person>();
    Connection connection = provider.get();
    ResultSet rs = null;
    try {
      rs = connection.createStatement().executeQuery("select * from people");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    peopleManipulator(rs,personList);
    closeResultSet(rs);

    return personList;

  }

  @Override
  public void updatePerson(Person person, int age) {
    Connection connection = provider.get();

    PreparedStatement pr = null;
    try {
      pr = connection.prepareStatement("update people set age=? where name=?");
      pr.setInt(1, age);
      pr.setString(2, person.name);
      pr.execute();
      pr.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closePreparedStatement(pr);
    }
  }

  @Override
  public List<Person> findByLetters(String letter) {
    List<Person> personList = new ArrayList<Person>();
    Connection connection = provider.get();
    ResultSet rs = null;
    try {
      rs = connection.createStatement().executeQuery("select * from people where name::text like '" + letter + "%'");
      peopleManipulator(rs,personList);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
    }
    return personList;
  }

  @Override
  public List<Person> findCitiesByDate(String city, Date date) {
    List<Person> personList = new ArrayList<Person>();
    Connection connection = provider.get();
    ResultSet rs = null;
    try {
      rs = connection.createStatement().executeQuery("select * from people where ucn in (select ucn from trip where city='" + city + "' and " +
              "date_of_arrival='" + date + "')");
      peopleManipulator(rs,personList);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
    }
    return personList;
  }

  @Override
  public void addTrip(Trip trip) {
    Connection connection = provider.get();
    PreparedStatement pr = null;
    try {
      pr = connection.prepareStatement("insert into trip(ucn, date_of_arrival,date_of_departure,city) values (?,?,?,?)");
      pr.setInt(1, trip.ucn);
      pr.setDate(2, trip.arrivalDate);
      pr.setDate(3, trip.departureDate);
      pr.setString(4, trip.city);
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closePreparedStatement(pr);
    }
  }

  @Override
  public List<Person> findPeopleAtSameCityAtSameDate(String city, Date date) {
    List<Person> personList = new ArrayList<Person>();
    Connection connection = provider.get();
    PreparedStatement pr = null;
    ResultSet rs = null;
    try {
      pr = connection.prepareStatement("select * from people where ucn in (select ucn from trip where city='" + city + "' and date_of_arrival>='" + date + "')");
      rs = pr.executeQuery();
      peopleManipulator(rs,personList);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closePreparedStatement(pr);
      closeResultSet(rs);
    }
    return personList;
  }

  @Override
  public List<String> listCitiesDescendingTripCount() {
    Connection connection = provider.get();
    List<String> cityList = new ArrayList<String>();
    PreparedStatement pr = null;
    ResultSet rs = null;
    try {
      pr = connection.prepareStatement("select city from trip group by city order by count(ucn) desc");
      rs = pr.executeQuery();
      while (rs.next()) {
        cityList.add(rs.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closePreparedStatement(pr);
      closeResultSet(rs);
    }
    return cityList;
  }

  @Override
  public List<Trip> findTrips()  {
    Connection connection = provider.get();
    List<Trip> tripList = new ArrayList<Trip>();
    ResultSet rs = null;
    try {
      rs = connection.createStatement().executeQuery("select * from trip");
      while (rs.next()) {
        int ucn = rs.getInt(1);
        Date arrivalDate = (rs.getDate(2));
        Date departureDate = rs.getDate(3);
        String city = rs.getString(4);
        tripList.add(new Trip(ucn, arrivalDate, departureDate, city));
      }
    } catch (SQLException e) {
      e.printStackTrace();

    } finally {
      closeResultSet(rs);
    }
    return tripList;
  }


  private void peopleManipulator(ResultSet rs,List<Person> list) {

    try {
      while (rs.next()) {
        String name = rs.getString("name");
        int ucn = rs.getInt("ucn");
        int age = rs.getInt("age");
        String email = rs.getString("e_mail");
        list.add(new Person(name, ucn, age, email));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void closeStatement(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private void closePreparedStatement(PreparedStatement pr) {
    if (pr != null) {
      try {
        pr.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
