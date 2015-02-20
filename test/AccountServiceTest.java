import com.clouway.task4.AccountService;
import com.clouway.task4.Address;
import com.clouway.task4.ConnectionProvider;
import com.clouway.task4.Contact;
import com.clouway.task4.DataStore;
import com.clouway.task4.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
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
    DataStore dataStore = new DataStore(provider);
    accountService = new AccountService(dataStore);
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
    assertThat(result.get(0).name,is("Kalin"));
    assertThat(result.get(1).name, is("Gosho"));
  }

  @Test
  public void registerAddress() {
    accountService.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));

    List<Address> result = accountService.findAddress();

    assertThat(result.size(), is(1));
    assertThat(result.get(0).address,is("Ivan Shishman"));
    assertThat(result.get(0).city, is("Plovdiv"));
  }

  @Test
  public void registerMultipleAddresses() {
    accountService.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));
    accountService.registerAddress(new Address(2, "Ivan Draganov", "Varna"));

    List<Address> result = accountService.findAddress();

    assertThat(result.size(), is(2));
    assertThat(result.get(0).city,is("Plovdiv"));
    assertThat(result.get(0).address,is("Ivan Shishman"));
    assertThat(result.get(1).address, is("Ivan Draganov"));
    assertThat(result.get(1).city,is("Varna"));
  }

  @Test
  public void findAddress() {
    accountService.registerAddress(new Address(1, "Ivan Shishman", "Plovdiv"));
    accountService.registerAddress(new Address(2, "Ivan Draganov", "Varna"));

    List<Address> result = accountService.findAddress("Ivan Draganov", "Varna");

    assertThat(result.size(),is(1));
    assertThat(result.get(0).city,is("Varna"));
    assertThat(result.get(0).address,is("Ivan Draganov"));
  }


  @Test
  public void registerContact() {
    User dido = new User(1, "Dido");
    accountService.registerUser(dido);
    Address tyrnovo = new Address(1, "Denu Chokanov", "Tyrnovo");
    accountService.registerAddress(tyrnovo);
    accountService.registerContact(new Contact(dido, tyrnovo));

    List<Contact> result = accountService.findContact();

    assertThat(result.size(), is(1));
    assertThat(result.get(0).user.id, is(1));
  }

  @Test
  public void registerMultipleContacts() {
    User kalin = new User(1, "Kalin");
    accountService.registerUser(kalin);
    User gosho = new User(2, "Gosho");
    accountService.registerUser(gosho);

    Address plovdiv = new Address(1, "Ivan Shishman", "Plovdiv");
    accountService.registerAddress(plovdiv);
    Address varna = new Address(2, "Ivan Draganov", "Varna");
    accountService.registerAddress(varna);


    accountService.registerContact(new Contact(kalin,plovdiv ));
    accountService.registerContact(new Contact(gosho, varna));

    List<Contact> result = accountService.findContact();

    assertThat(result.size(), is(2));
    assertThat(result.get(0).user.name,is("Kalin"));
    assertThat(result.get(1).user.id, is(2));
  }
  
  @Test
  public void findUserNameByAddress() {
    User kalin = new User(1, "Kalin");
    accountService.registerUser(kalin);

    Address plovdiv = new Address(1, "Ivan Shishman", "Plovdiv");
    accountService.registerAddress(plovdiv);

    Contact contact = new Contact(kalin, plovdiv);
    accountService.registerContact(contact);

    List<User> result = accountService.findUsersByAddress("Ivan Shishman", "Plovdiv");

    assertThat(result.get(0).name,is("Kalin"));
    assertThat(result.get(0).id,is(1));
  }

  @Test
  public void findMultipleUsersNamesByAddress() {
    User kalin = new User(1, "Kalin");
    accountService.registerUser(kalin);
    User gosho = new User(2, "Gosho");
    accountService.registerUser(gosho);

    Address plovdiv = new Address(1, "Ivan Shishman", "Plovdiv");
    accountService.registerAddress(plovdiv);
    Address varna = new Address(2, "Ivan Shishman", "Plovdiv");
    accountService.registerAddress(varna);

    accountService.registerContact(new Contact(kalin,plovdiv ));
    accountService.registerContact(new Contact(gosho, varna));

    List<User> result = accountService.findUsersByAddress("Ivan Shishman", "Plovdiv");

    assertThat(result.get(0).name,is("Kalin"));
    assertThat(result.get(1).name,is("Gosho"));

  }

}
