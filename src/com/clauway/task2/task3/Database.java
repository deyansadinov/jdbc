package com.clauway.task2.task3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Database implements PersonRepository<Person>, TripRepository {


  private final ConnectionProvider provider;
  private Statement statement;
  private List<Person> personList = new ArrayList<Person>();
  private ResultSet rs;
  private PreparedStatement pr;

  public Database(ConnectionProvider provider) {

    this.provider = provider;
  }


  @Override
  public void addPerson(Person person) {
    Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
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
  public List<Person> findPeople() {
    Connection connection = provider.connect();
    try {
      rs = connection.createStatement().executeQuery("select * from people");
      peopleManipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeResultSet(rs);
    }
    return personList;

  }

  @Override
  public void updatePerson(Person person, int age) {
    Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
      pr = connection.prepareStatement("update people set age=? where name=?");
      pr.setInt(1, age);
      pr.setString(2, person.name);
      pr.execute();
      pr.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      closeStatement(statement);
      closePreparedStatement(pr);
    }
  }

  @Override
  public List<Person> findByLetters(String letter) {
    Connection connection = provider.connect();
    try {
      rs = connection.createStatement().executeQuery("select * from people where name::text like '" + letter + "%'");
      peopleManipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      closeResultSet(rs);
    }
    return personList;
  }

  @Override
  public List<Person> findCitiesByDate(String city, Date date) {
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Connection connection = provider.connect();
    try {
      rs = connection.createStatement().executeQuery("select * from people where ucn in (select ucn from trip where city='" + city + "' and " +
              "date_of_arrival='" + date + "')");
      peopleManipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      closeResultSet(rs);
    }
    return personList;
  }

  @Override
  public void addTrip(Trip trip) {
    Connection connection = provider.connect();
    try {
      pr = connection.prepareStatement("insert into trip(ucn, date_of_arrival,date_of_departure,city) values (?,?,?,?)");
      pr.setInt(1,trip.ucn);
      pr.setDate(2,trip.arrivalDate);
      pr.setDate(3,trip.departureDate);
      pr.setString(4,trip.city);
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      closePreparedStatement(pr);
    }
  }

  @Override
  public List<Person> findPeopleAtSameCityAtSameDate(String city, Date date) {
    Connection connection = provider.connect();
    try {
      statement = connection.createStatement();
      pr = connection.prepareStatement("select * from people where ucn in (select ucn from trip where city='"+city+"' and date_of_arrival>='"+date+"')");
      rs = pr.executeQuery();
      peopleManipulator(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      closePreparedStatement(pr);
      closeResultSet(rs);
      closeStatement(statement);
    }
    return personList;
  }

  @Override
  public List<String> listCitiesDescendingTripCount() {
    Connection connection = provider.connect();
    List<String> cityList = new ArrayList<String>();
    try {
      statement = connection.createStatement();
      pr = connection.prepareStatement("select city from trip group by city order by count(ucn) desc");
      rs = pr.executeQuery();
      while (rs.next()){
        cityList.add(rs.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      closeStatement(statement);
      closePreparedStatement(pr);
      closeResultSet(rs);
    }
    return cityList;
  }

  @Override
  public List<Trip> findTrips() throws SQLException {
    Connection connection = provider.connect();
    List<Trip> tripList = new ArrayList<Trip>();
    try {
      rs = connection.createStatement().executeQuery("select * from trip");
      while (rs.next()){
        int ucn = rs.getInt(1);
        Date arrivalDate = (rs.getDate(2));
        Date departureDate = rs.getDate(3);
        String city = rs.getString(4);
        tripList.add(new Trip(ucn,arrivalDate,departureDate,city));
      }
    } catch (SQLException e) {
      e.printStackTrace();

    }finally {
      closeResultSet(rs);
    }
    return tripList;
  }


  private void peopleManipulator(ResultSet rs) {
    try {
      while (rs.next()) {
        String name = rs.getString("name");
        int ucn = rs.getInt("ucn");
        int age = rs.getInt("age");
        String email = rs.getString("e_mail");
        personList.add(new Person(name, ucn, age, email));
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
