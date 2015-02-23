package com.clouway.task5;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class ArticleDatabase {


  private final DataStore dataStore;

  public ArticleDatabase(DataStore dataStore) {


    this.dataStore = dataStore;
  }


  public void addArticle(Article article) {
    dataStore.execute("insert into articles(id,title) values (" + article.id + ",'" + article.title + "')");
  }

  public List<Article> findAll() {
    return dataStore.findAll("articles",new RowFetcher<Article>() {
      @Override
      public Article fetchRow(ResultSet rs) throws SQLException {
        return new Article(rs.getInt(1),rs.getString(2));
      }
    });
  }

  public List<Article> findHistory() {
    return dataStore.findAll("articles_history",new RowFetcher<Article>() {
      @Override
      public Article fetchRow(ResultSet rs) throws SQLException {
        return new Article(rs.getInt(1),rs.getString(2));
      }
    });
  }

  public void updateArticle(int id, String newArticle) {
    dataStore.execute("update articles set title='" + newArticle + "' where id=" + id);
  }
}
