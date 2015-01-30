package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class Test {
  public static void main(String[] args) {

    try {
      connectToAndQueryDatabase("postgres", "123456");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public static void connectToAndQueryDatabase(String user,String pass) throws SQLException {

    Connection con  = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test1",user,pass);

    Statement statement = con.createStatement();

    ResultSet rs = statement.executeQuery("SELECT name FROM dido");

    while (rs.next()){
      String name = rs.getString("name");
    }

  }


}
