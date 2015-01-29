package com.clouway.task5;

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
public class ArticleDatabase {

  private final String articleTable;
  private final String historyTable;
  private Connection connection;

  public ArticleDatabase(String articleTable,String historyTable) {
    this.articleTable = articleTable;
    this.historyTable = historyTable;
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

  public void addArticle(Article article) {
    try {
      PreparedStatement pr = connection.prepareStatement("insert into articles(id,title) values (" + article.getId() + ",'" +
      article.getTitle() + "')");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Article> findAll() {
    List<Article> list = new ArrayList<Article>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from articles");
      while (rs.next()){
        int id = rs.getInt("id");
        String title = rs.getString("title");
        list.add(new Article(id,title));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public List<Article> findHistory() {
    List<Article> list = new ArrayList<Article>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from articles_history");
      while (rs.next()){
        int id = rs.getInt("id");
        String title = rs.getString("title");
        list.add(new Article(id,title));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void updateArticle(int id, String newArticle) {
    try {
      connection.createStatement().execute("update articles set title='" + newArticle + "' where id=" + id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
