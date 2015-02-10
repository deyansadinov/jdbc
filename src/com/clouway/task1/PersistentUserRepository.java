package com.clouway.task1;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface PersistentUserRepository<T> {
  List<User> getAll();

  void register(T t) throws SQLException;

  void update(User user, String name);

  void remove(T t);

  List<User> findByProperty(int age, String retrieveAge);

  List<User> retrieveUsersByAge(int age);
}
