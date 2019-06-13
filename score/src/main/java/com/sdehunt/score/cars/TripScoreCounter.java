package com.sdehunt.score.cars;

import java.util.List;

import static com.sdehunt.score.cars.DistanceCounter.dist;

public class TripScoreCounter {
    public int count(List<Trip> trips, int bonus) {
        int score = 0;
        for (Trip trip : trips) {
            int time = 0;
            Point pos = new Point().setCol(0).setRow(0);
            for (Ride ride : trip.getRides()) {
                // moving from pos to ride start (getting bonus if came on time).
                time += dist(pos, ride.getFrom());
                pos = ride.getFrom(); // just for better understanding
                if (time <= ride.getStart()) {
                    score += bonus;
                }
                // moving to destination. if coming on time, getting bonus.
                int rideDistance = dist(ride.getFrom(), ride.getTo());
                time += rideDistance;
                if (time < ride.getFinish()) {
                    score += rideDistance;
                }
                pos = ride.getTo();
            }
        }

        return score;
    }
}
