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


  private final ConnectionProvider provider;

  public ArticleDatabase(ConnectionProvider provider) {

    this.provider = provider;
  }



  public void addArticle(Article article) {
    Connection connection = provider.connect();
    try {
      PreparedStatement pr = connection.prepareStatement("insert into articles(id,title) values (" + article.id + ",'" +
      article.title + "')");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Article> findAll() {
    Connection connection = provider.connect();
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
    Connection connection = provider.connect();
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
    Connection connection = provider.connect();
    try {
      connection.createStatement().execute("update articles set title='" + newArticle + "' where id=" + id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
