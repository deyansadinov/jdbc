package com.clouway.task1;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface PersistentUserRepository {
  List<User> getAll();

  void register(User user) throws SQLException;

  void update(User user, String name);

  void remove(User user);

  List<User> findByProperty(String retrieveAge);

  List<User> retrieveUsersAge(int age);
}
