package com.clouway.task6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class PersistentCar {

  private final String carTable;
  private Connection connection;


  public PersistentCar(String carTable) {
    this.carTable = carTable;
  }

  public Connection connection(String user,String pass){
    try {
      connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/didodb", user, pass);
      return connection;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }


  public void register(Car car) {
    try {
      PreparedStatement pr = connection.prepareStatement("insert into car(id,power,year) values(" + car.getId() + ","
      + car.getPower() + "," + car.getYear() + ")");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Car> findAll() {
    List<Car> list = new ArrayList<Car>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from car");
      while (rs.next()){
        int id = rs.getInt("id");
        int  power = rs.getInt("power");
        int year = rs.getInt("year");
        list.add(new Car(id,power,year));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }


  public void register1MillionRows(Car car) {
    try {
      connection.setAutoCommit(false);
      for (int i = 0; i < 1000000; i++) {
        PreparedStatement pr = connection.prepareStatement("insert into car(power,year) values ("
        + car.getPower() + "," + car.getYear() + ")");
        pr.execute();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      connection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
