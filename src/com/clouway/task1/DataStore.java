package com.clouway.task1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class DataStore  {

  private final ConnectionProvider connectionProvider ;

//  interface RowFetcher<T> {
//    T fetchRow(ResultSet rs) throws SQLException;
//  }

  public DataStore(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  public <T> List<T> findAll(String tableName, RowFetcher<T> fetcher) {
    return fetchRow("select * from " + tableName,fetcher);
  }

  public  <T> List<T> fetchRow(String sql, RowFetcher<T> rowFetcher) {
    Connection connection = connectionProvider.get();
    List<T> list = new ArrayList<T>();
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        T item = rowFetcher.fetchRow(rs);
        list.add(item);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }

  public void execute(String sql){
    Connection connection = connectionProvider.get();
    try {
      connection.createStatement().executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
