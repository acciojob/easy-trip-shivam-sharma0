package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AirportRepository {

    Map<String , Airport>airportMap=new HashMap<>();
    List<Airport> airportList=new ArrayList<>();
    Map<Integer , Flight>flightMap=new HashMap<>();
    List<Flight> flightList=new ArrayList<>();
    Map<Integer , Passenger>passengerMap=new HashMap<>();
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
}
