package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.*;

public class AirportRepository {

    Map<String , Airport>airportMap=new HashMap<>();
    List<Airport> airportList=new ArrayList<>();
    Map<Integer , Flight>flightMap=new HashMap<>();
    List<Flight> flightList=new ArrayList<>();
    Map<Integer , Passenger>passengerMap=new HashMap<>();

    Map<Integer,List<Integer>> flightPassenger=new HashMap<>();
    Map<Integer,List<Integer>> passengerWithFlightsMap =new HashMap();
    Map<Integer,Integer> bookingCount =new HashMap();
    public void addAirport(Airport airport) {
        String name=airport.getAirportName();
        airportList.add(airport);
        airportMap.put(name,airport);
    }

    public void addFlight(Flight flight) {
        int id=flight.getFlightId();
        flightList.add(flight);
        flightMap.put(id,flight);
    }

    public void addPassenger(Passenger passenger) {
        int id=passenger.getPassengerId();
        passengerMap.put(id,passenger);
    }

    public List<Airport> getAllAirport() {
        return airportList;
    }

    public List<Flight> getAllFlight() {
        return flightList;
    }

    public String bookTicket(Integer flightId, Integer passengerId) {
        bookingCount.put(passengerId,bookingCount.getOrDefault(passengerId,0)+1);

        Flight flight=flightMap.get(flightId);
        List<Integer> listOfPassenger=flightPassenger.getOrDefault(flightId,new ArrayList<>());
        if (flight.getMaxCapacity()==listOfPassenger.size())return "FAILURE";

        List<Integer> listOfFilghtsWithPasenger=passengerWithFlightsMap.getOrDefault(passengerId,new ArrayList<>());
        listOfFilghtsWithPasenger.add(flightId);
        passengerWithFlightsMap.put(passengerId,listOfFilghtsWithPasenger);

        listOfPassenger.add(passengerId);
        flightPassenger.put(flightId,listOfPassenger);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        Set<Integer> flightIds = flightMap.keySet();
        Set<Integer> passengersIds = passengerMap.keySet();
        if(!flightIds.contains(flightId)) return "FAILURE";
        if(!passengersIds.contains(passengerId)) return "FAILURE";

        if (!passengerWithFlightsMap.containsKey(passengerId)) {
            return "FAILURE";
        }
        if(passengerWithFlightsMap.containsKey(passengerId)){
            List<Integer> listOfFlightsWithPassenger=passengerWithFlightsMap.get(passengerId);
            if(!listOfFlightsWithPassenger.contains(flightId)) return "FAILURE";
            listOfFlightsWithPassenger.remove(flightId);
            passengerWithFlightsMap.put(passengerId,listOfFlightsWithPassenger);
        }

        List<Integer> listOfBookedTicked=flightPassenger.get(flightId);
        if(!listOfBookedTicked.contains(passengerId)) return "FAILURE";
        listOfBookedTicked.remove(passengerId);
        flightPassenger.put(flightId,listOfBookedTicked);
        return "SUCCESS";

    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        int count=0;
        for(Integer flightId:flightPassenger.keySet()){
            Set<Integer> list= (Set<Integer>) flightPassenger.get(flightId);
            if(list.contains(passengerId)){
                count++;
            }
        }
        return count;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        Airport airport=airportMap.get(airportName);
        List<Flight> flights=new ArrayList<>(flightList);
        int people=0;
        for(Flight flight:flights){
            if(flight.getFromCity().equals(airport.getCity()) || flight.getToCity().equals(airport.getCity()) && flight.getFlightDate().equals(date)){
                people+=flight.getMaxCapacity();
            }
        }
        return people;
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        Flight flight=flightMap.get(flightId);
        City city=flight.getFromCity();
        List<Airport> airports=new ArrayList<>(airportList);
        for (Airport airport:airports){
            if(city.equals(airport.getCity())){
                return airport.getAirportName();
            }
        }
        return null;
    }
}
