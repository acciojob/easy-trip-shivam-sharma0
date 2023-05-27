package com.driver.repository;

import com.driver.model.Airport;
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
        List<Integer> listOfFilghtsWithPasenger=passengerWithFlightsMap.getOrDefault(passengerId,new ArrayList<>());
        listOfFilghtsWithPasenger.add(flightId);
        passengerWithFlightsMap.put(passengerId,listOfFilghtsWithPasenger);

        Flight flight=flightMap.get(flightId);
        List<Integer> listOfPassenger=flightPassenger.getOrDefault(flightId,new ArrayList<>());
        if (flight.getMaxCapacity()==listOfPassenger.size())return "FAILURE";

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
        return passengerWithFlightsMap.get(passengerId).size();
    }
}
