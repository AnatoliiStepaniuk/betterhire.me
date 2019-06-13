package com.sdehunt.score.cars;

import lombok.Data;

import java.util.List;

/**
 * Trip of a single vehicle (all rides)
 */
@Data
public class Trip {
    private List<Ride> rides;

    /**
     * Returns max step
     */
    public int steps() {
        return rides.get(rides.size() - 1).getFinish();
    }
}
