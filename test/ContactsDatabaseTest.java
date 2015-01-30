import com.clouway.task4.Address;
import com.clouway.task4.ConnectionProvider;
import com.clouway.task4.Contact;
import com.clouway.task4.ContactsDatabase;
import com.clouway.task4.User;
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
public class ContactsDatabaseTest {

  private ContactsDatabase contactsDatabase;
  private Connection connection;

  @Before
  public void setUp(){
    ConnectionProvider provider = new ConnectionProvider();
    contactsDatabase = new ContactsDatabase(provider);
    connection = provider.connect();
  }

  @After
  public void tearDown() throws SQLException {
    connection.createStatement().execute("truncate table addresses,contacts,users restart identity");
  }


  @Test
  public void registerUser() {
    contactsDatabase.registerUser(new User(1,"Dido"));

    List<User> result = contactsDatabase.findUsers();

    assertThat(result.size(),is(1));
    assertThat(result.get(0).name,is("Dido"));
  }

  @Test
  public void registerMultipleUsers() {
    contactsDatabase.registerUser(new User(1,"Kalin"));
    contactsDatabase.registerUser(new User(2,"Gosho"));

    List<User> result = contactsDatabase.findUsers();

    assertThat(result.size(),is(2));
    assertThat(result.get(1).name,is("Gosho"));
  }

  @Test
  public void registerAddress() {
    contactsDatabase.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));

    List<Address> result = contactsDatabase.findAddress();

    assertThat(result.size(),is(1));
    assertThat(result.get(0).city,is("Plovdiv"));
  }

  @Test
  public void registerMultipleAddresses() {
    contactsDatabase.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));
    contactsDatabase.registerAddress(new Address(2, "Ivan Draganov", "Varna"));

    List<Address> result = contactsDatabase.findAddress();

    assertThat(result.size(),is(2));
    assertThat(result.get(1).address,is("Ivan Draganov"));
  }

  @Test
  public void registerContact() {
    contactsDatabase.registerUser(new User(1,"Dido"));
    contactsDatabase.registerAddress(new Address(1,"Denu Chokanov","Tyrnovo"));
    contactsDatabase.registerContact(new Contact(1,1));

    List<Contact> result = contactsDatabase.findContact();

    assertThat(result.size(),is(1));
    assertThat(result.get(0).userId,is(1));
  }

  @Test
  public void registerMultipleContacts() {
    contactsDatabase.registerUser(new User(1,"Kalin"));
    contactsDatabase.registerUser(new User(2,"Gosho"));

    contactsDatabase.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));
    contactsDatabase.registerAddress(new Address(2, "Ivan Draganov", "Varna"));



    contactsDatabase.registerContact(new Contact(1,1));
    contactsDatabase.registerContact(new Contact(2,2));

    List<Contact> result = contactsDatabase.findContact();

    assertThat(result.size(),is(2));
    assertThat(result.get(1).userId,is(2));
  }

}
