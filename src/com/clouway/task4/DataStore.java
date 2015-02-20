package com.clouway.task4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class DataStore {

  private final ConnectionProvider provider;

  public DataStore(ConnectionProvider provider) {
    this.provider = provider;
  }

  public <T>List<T>findAll(String tableName,RowFetcher<T>fetcher){
    return fetchRow("select * from " + tableName,fetcher);
  }

  public <T>List<T>fetchRow(String sql,RowFetcher<T>fetcher){
    Connection connection = provider.get();
    List<T>list = new ArrayList<T>();
    Statement stmt = null;
    ResultSet rs = null;
    try {
       stmt = connection.createStatement();
       rs = stmt.executeQuery(sql);
      while (rs.next()){
        T item = fetcher.fetchRow(rs);
        list.add(item);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }finally {
      if (stmt != null){
        try {
          stmt.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (rs != null){
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return list;
  }

  public void execute(String sql){
    Connection connection = provider.get();
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
