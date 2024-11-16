package com.api.udc.randomAnimal.service;

import com.api.udc.util.response.CustomApiResponse;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RandomServiceImp implements RandomService {
    double x =37.58284829999999;
    double y = 127.0105811;
    private static final double RADIUS = 1.0 / 111.32;
    private final List<double[]> coordinates = new ArrayList<>();


    @Override
    public CustomApiResponse<List<double[]>> randomAnimal() {
        generateRandomCoordinates();
        startCoordinateUpdater();
        return CustomApiResponse.createSuccess(200,coordinates,"7개좌표");
    }

    private void generateRandomCoordinates() {
        coordinates.clear();
        for (int i = 0; i < 7; i++) {
            double randomLat = x + (Math.random() - 0.5) * 2 * RADIUS;
            double randomLon = y + (Math.random() - 0.5) * 2 * RADIUS / Math.cos(Math.toRadians(x));
            coordinates.add(new double[]{randomLat, randomLon});
        }
    }

    private void startCoordinateUpdater() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::generateRandomCoordinates, 1, 1, TimeUnit.HOURS);
    }
}
