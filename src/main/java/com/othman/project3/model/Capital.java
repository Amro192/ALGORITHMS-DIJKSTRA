package com.othman.project3.model;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public record Capital(String name, double latitude, double longitude) {

    //random number used with color of circles
    private static final Random RANDOM = new Random();

    private static final double IMAGE_X = 1000;
    private static final double IMAGE_Y = 570;
    private static final double LONGITUDE_MIN = -130.00841812115695;
    private static final double LONGITUDE_MAX = 153.87828308616503;
    private static final double LATITUDE_MIN = 69.6747178217249;
    private static final double LATITUDE_MAX = -53.51014265616152;
    private static final double MIN_IMAGE_X = 0.0;
    private static final double MIN_IMAGE_Y = 0.0;

    public Capital {
        if (latitude > 90 || latitude < -90) {
            throw new IllegalArgumentException("Latitude out of range: " + latitude);
        }
        if (longitude > 180 || longitude < -180) {
            throw new IllegalArgumentException("Longitude out of range: " + longitude);
        }
    }


    public double mapLongitudeToX() {
//        return (((longitude - LONGITUDE_MIN) * (IMAGE_X - MIN_IMAGE_X)) / (LONGITUDE_MAX - LONGITUDE_MIN)) - MIN_IMAGE_X;
        return ((IMAGE_X * longitude - IMAGE_X * LONGITUDE_MIN) - (MIN_IMAGE_X * longitude - MIN_IMAGE_X * LONGITUDE_MAX)
                + (MIN_IMAGE_X * LONGITUDE_MAX - MIN_IMAGE_X * LONGITUDE_MIN)) / (LONGITUDE_MAX - LONGITUDE_MIN);
    }


    // Helper method to map latitude to Y coordinate on the image
    public double mapLatitudeToY() {
        if (latitude > 20 && latitude < 57) {
            return (((latitude - 13 - LATITUDE_MIN) * (IMAGE_Y - MIN_IMAGE_Y)) / (LATITUDE_MAX - LATITUDE_MIN)) - MIN_IMAGE_Y;
        }
        if (latitude > 0 && latitude < 20) {
            return (((latitude - 8 - LATITUDE_MIN) * (IMAGE_Y - MIN_IMAGE_Y)) / (LATITUDE_MAX - LATITUDE_MIN)) - MIN_IMAGE_Y;
        }
        if (latitude > 57 && latitude < 70) {
            return (((latitude - 5 - LATITUDE_MIN) * (IMAGE_Y - MIN_IMAGE_Y)) / (LATITUDE_MAX - LATITUDE_MIN)) - MIN_IMAGE_Y;
        }
        return (((latitude - LATITUDE_MIN) * (IMAGE_Y - MIN_IMAGE_Y)) / (LATITUDE_MAX - LATITUDE_MIN)) - MIN_IMAGE_Y;
//        return ((IMAGE_Y * latitude - IMAGE_Y * LATITUDE_MIN) - (MIN_IMAGE_Y * latitude - MIN_IMAGE_Y * LATITUDE_MAX)
//                + (MIN_IMAGE_Y * LATITUDE_MAX - MIN_IMAGE_Y * LATITUDE_MIN)) / (LATITUDE_MAX - LATITUDE_MIN);
    }

    public Circle projectToImage() {
        return new Circle(mapLongitudeToX(), mapLatitudeToY(), 5, Color.rgb(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)));
    }

    public static double calculateDistance(Capital a, Capital b) {
        final double R = 6378.1; // Radius of the Earth in km

        double latDistance = Math.toRadians(b.latitude() - a.latitude());
        double lonDistance = Math.toRadians(b.longitude() - a.longitude());

        double lat1 = Math.toRadians(a.latitude());
        double lat2 = Math.toRadians(b.latitude());

        double aVal = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1 - aVal));

        return R * c; // Returns distance in km
    }


    @Override
    public String toString() {
        return "Capital{" + "name='" + name + '\'' + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
}
