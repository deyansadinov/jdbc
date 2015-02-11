package com.clouway.task5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class ConnectionProvider {

  public Connection get() {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/didodb","postgres","123456");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
}
