package com.clouway.task5;

import java.sql.Connection;
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
    Connection connection = provider.get();
    try {
      PreparedStatement pr = connection.prepareStatement("insert into articles(id,title) values (" + article.id + ",'" +
              article.title + "')");
      pr.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Article> findAll() {
    Connection connection = provider.get();
    List<Article> list = new ArrayList<Article>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from articles");
      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        list.add(new Article(id, title));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public List<Article> findHistory() {
    Connection connection = provider.get();
    List<Article> list = new ArrayList<Article>();
    try {
      ResultSet rs = connection.createStatement().executeQuery("select * from articles_history");
      while (rs.next()) {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        list.add(new Article(id, title));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }

  public void updateArticle(int id, String newArticle) {
    Connection connection = provider.get();
    String query = "select id from articles where id=" + id;
    try {
      ResultSet rs = connection.createStatement().executeQuery(query);
      if (!rs.next()){
        throw new NonExistingArticleException();
      }
      connection.createStatement().executeUpdate("update articles set title='" + newArticle + "' where id=" + id);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
