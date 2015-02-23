package com.clauway.task2.task3;

import java.sql.Date;
import java.util.List;

/**
 * @author Deyan Sadinov <sadinov88@gmail.com>
 */
public interface TripRepository {

  void addTrip(Trip trip);

  List<Person> findPeopleAtSameCityAtSameDate(String city, Date startDate,Date endDate);

  List<String> listCitiesDescendingTripCount();


  List<Trip> findTrips() ;

  void updateCityFromTrip(Trip trip, String city);
}
