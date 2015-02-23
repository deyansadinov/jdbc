import com.clouway.task5.Article;
import com.clouway.task5.ArticleDatabase;
import com.clouway.task5.ConnectionProvider;
import com.clouway.task5.DataStore;
import com.clouway.task5.NonExistingArticleException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class ArticleDatabaseTest {

  private ArticleDatabase articleDatabase;
  private Connection connection;

  @Before
  public void setUp() {
    ConnectionProvider provider = new ConnectionProvider();
    DataStore dataStore = new DataStore(provider);
    articleDatabase = new ArticleDatabase(dataStore);
    connection = provider.get();
  }

  @After
  public void tearDown() throws SQLException {
    connection.createStatement().execute("truncate articles,articles_history restart identity");
  }

  @Test
  public void addArticle() {
    articleDatabase.addArticle(new Article(1,"postgres"));

    List<Article> result = articleDatabase.findAll();

    assertThat(result.size(),is(1));
  }

  @Test
  public void updateSingleArticle() {
    articleDatabase.addArticle(new Article(1,"postgres"));

    articleDatabase.updateArticle(1,"mysql");

    List<Article> result = articleDatabase.findAll();

    List<Article> listHistory = articleDatabase.findHistory();

    assertThat(listHistory.size(),is(1));
    assertThat(result.get(0).title,is("mysql"));
  }

  @Test
  public void updateAnotherArticle() {
    articleDatabase.addArticle(new Article(1,"postgres"));
    articleDatabase.addArticle(new Article(2,"mysql"));

    articleDatabase.updateArticle(1,"dido");
    articleDatabase.updateArticle(2,"dido2");

    List<Article> result = articleDatabase.findAll();

    List<Article> listHistory = articleDatabase.findHistory();

    assertThat(listHistory.size(),is(2));
    assertThat(result.get(1).title,is("dido2"));
    assertThat(result.get(0).title,is("dido"));
  }

  @Test(expected = NonExistingArticleException.class)
  public void updateNonExistingArticle () {
    articleDatabase.addArticle(new Article(1,"postgres"));

    articleDatabase.updateArticle(2,"mysql");
  }

  @Test
  public void updateSingleArticleMultipleTimes () {
    articleDatabase.addArticle(new Article(1,"postgres"));

    articleDatabase.updateArticle(1,"mysql");
    articleDatabase.updateArticle(1,"dido");

    List<Article> result = articleDatabase.findAll();

    List<Article> listHistory = articleDatabase.findHistory();

    assertThat(listHistory.size(),is(2));
    assertThat(result.get(0).title,is("dido"));
  }
}
