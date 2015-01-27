package com.clauway.task2.task3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface TripRepository<T> {

  void addTrip(Trip trip);

  List<Person> findPeopleAtSameCityAtSameDate(String city, String date);

  List<String> listCitiesDescendingTripCount();


  List<Trip> findTrips() throws SQLException;
}
