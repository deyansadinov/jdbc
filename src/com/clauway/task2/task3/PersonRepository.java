package com.clauway.task2.task3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface PersonRepository {

  void addPerson(Person person) throws SQLException;

  List<Person> findPeople();

  void updatePerson(Person person, int age);


  List<Person> findByLetters(String letter);


  List<Person> findCitiesByDate(String city, Date date);
}
