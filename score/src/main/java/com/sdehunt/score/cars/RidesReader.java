package com.sdehunt.score.cars;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RidesReader {

    @SneakyThrows(IOException.class)
    public List<Ride> read(String file) {
        List<Ride> rides = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int ridesNum = Integer.valueOf(reader.readLine().split(" ")[3]);
            for (int i = 0; i < ridesNum; i++) {
                rides.add(parse(reader.readLine()));
            }
        }
        return rides;
    }

    private Ride parse(String str) {
        String[] sp = str.split(" ");
        return new Ride()
                .setFrom(new Point().setRow(Integer.valueOf(sp[0])).setCol(Integer.valueOf(sp[1])))
                .setTo(new Point().setRow(Integer.valueOf(sp[2])).setCol(Integer.valueOf(sp[3])))
                .setStart(Integer.valueOf(sp[4]))
                .setFinish(Integer.valueOf(sp[5]));
    }
}
