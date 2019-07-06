package com.sdehunt.score.cars;

import com.sdehunt.commons.exception.InvalidSolutionException;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TripsReader {

    @SneakyThrows(IOException.class)
    List<Trip> read(String file, List<Ride> rides, int vehicles) {
        Map<Integer, Boolean> isUsed = isUsedMap(rides.size());
        List<Trip> trips = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                String[] sp = line.split(" ");
                if (Integer.valueOf(sp[0]) != sp.length - 1) {
                    throw new InvalidSolutionException();
                }
                List<Ride> tripRides = new ArrayList();
                for (int i = 1; i < sp.length; i++) {
                    Integer rideIndex = Integer.valueOf(sp[i]);
                    if (isUsed.get(rideIndex)) {
                        throw new InvalidSolutionException(); // Trips was already used.
                    }
                    tripRides.add(rides.get(rideIndex));
                    isUsed.put(rideIndex, true); // To avoid using same trip several times
                }
                Trip trip = new Trip().setRides(tripRides);
                trips.add(trip);
                line = reader.readLine();
            }
        }

        if (trips.size() > vehicles) {
            throw new InvalidSolutionException();
        }

        return trips;
    }

    private Map<Integer, Boolean> isUsedMap(int size) {
        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(i, false);
        }
        return map;
    }
}
