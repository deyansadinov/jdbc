package com.clauway.task2.task3;

import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface PersonRepository<T> {

  void addPerson(T t);

  List<Person> findPeople();

  void updatePerson(Person person, int age);


  List<Person> findByLetters(String letter);


  List<Person> findCitiesByDate(String city, String date);
}
