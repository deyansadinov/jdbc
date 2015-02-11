import com.clouway.task4.AccountService;
import com.clouway.task4.Address;
import com.clouway.task4.ConnectionProvider;
import com.clouway.task4.Contact;
import com.clouway.task4.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public class AccountServiceTest {

  private AccountService accountService;

  @Rule
  public DataStoreRule dataStoreRule = new DataStoreRule();


  @Before
  public void setUp() {
    ConnectionProvider provider = new ConnectionProvider();
    accountService = new AccountService(provider);
    Connection connection = dataStoreRule.getConnection();

  }


  @Test
  public void registerUser() {
    accountService.registerUser(new User(1, "Dido"));

    List<User> result = accountService.findUsers();

    assertThat(result.size(), is(1));
    assertThat(result.get(0).name, is("Dido"));
  }

  @Test
  public void registerMultipleUsers() {
    accountService.registerUser(new User(1, "Kalin"));
    accountService.registerUser(new User(2, "Gosho"));

    List<User> result = accountService.findUsers();

    assertThat(result.size(), is(2));
    assertThat(result.get(1).name, is("Gosho"));
  }

  @Test
  public void registerAddress() {
    accountService.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));

    List<Address> result = accountService.findAddress();

    assertThat(result.size(), is(1));
    assertThat(result.get(0).city, is("Plovdiv"));
  }

  @Test
  public void registerMultipleAddresses() {
    accountService.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));
    accountService.registerAddress(new Address(2, "Ivan Draganov", "Varna"));

    List<Address> result = accountService.findAddress();

    assertThat(result.size(), is(2));
    assertThat(result.get(1).address, is("Ivan Draganov"));
  }

  @Test
  public void registerContact() {
    accountService.registerUser(new User(1, "Dido"));
    accountService.registerAddress(new Address(1, "Denu Chokanov", "Tyrnovo"));
    accountService.registerContact(new Contact(1, 1));

    List<Contact> result = accountService.findContact();

    assertThat(result.size(), is(1));
    assertThat(result.get(0).userId, is(1));
  }

  @Test
  public void registerMultipleContacts() {
    accountService.registerUser(new User(1, "Kalin"));
    accountService.registerUser(new User(2, "Gosho"));

    accountService.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));
    accountService.registerAddress(new Address(2, "Ivan Draganov", "Varna"));


    accountService.registerContact(new Contact(1, 1));
    accountService.registerContact(new Contact(2, 2));

    List<Contact> result = accountService.findContact();

    assertThat(result.size(), is(2));
    assertThat(result.get(1).userId, is(2));
  }

}
