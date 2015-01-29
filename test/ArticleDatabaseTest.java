import com.clouway.task5.Article;
import com.clouway.task5.ArticleDatabase;
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
    articleDatabase = new ArticleDatabase("articles", "articles_history");
    connection = articleDatabase.connection("postgres", "123456");
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
  public void updateArticle() {
    articleDatabase.addArticle(new Article(1,"postgres"));

    articleDatabase.updateArticle(1,"mysql");

    List<Article> result = articleDatabase.findAll();

    List<Article> listHistory = articleDatabase.findHistory();

    assertThat(listHistory.size(),is(1));
    assertThat(result.get(0).getTitle(),is("mysql"));
  }
}
