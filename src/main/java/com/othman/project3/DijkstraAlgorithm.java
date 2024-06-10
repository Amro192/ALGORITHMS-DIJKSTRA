package com.othman.project3;

import com.othman.project3.model.Capital;
import com.othman.project3.model.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class DijkstraAlgorithm {
    public static void run(Graph graph) {
        try (BufferedReader br = new BufferedReader(new FileReader("capitals.txt"))) {
            String line;
            int counter = 0;
            int numberOfCapitals = 0;
            while ((line = br.readLine()) != null && !line.isBlank()) {
                //If it's the first line
                if (counter == 0) {
                    Map<String, Integer> stringIntegerMap = parseFirstLine(line);
                    numberOfCapitals = stringIntegerMap.getOrDefault("capitals", 0);
                    counter++;
                    continue;
                }
                // after the first line
                if (counter <= numberOfCapitals) {
                    Optional<Capital> capitalFromMethod = parseCoordinates(line);
                    capitalFromMethod.ifPresent(graph::addCapital);
                    counter++;
                    continue;
                }
                String[] partsOfEdges = parseEdges(line);
                if (partsOfEdges == null) continue;
                graph.addEdge(partsOfEdges[0].trim(), partsOfEdges[1].trim());
            }


        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

    }


    public static Map<String, Integer> parseFirstLine(String line) {
        try {
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid input format in first line");
                return Map.of();
            }
            return Map.of("capitals", Integer.parseInt(parts[0].trim()), "edges", Integer.parseInt(parts[1].trim()));
        } catch (NumberFormatException e) {
            System.out.println("Check numbers in first line again");
        }

        return Map.of();
    }


    public static Optional<Capital> parseCoordinates(String line) {
        double latitude = 0;
        double longitude = 0;

        String[] parts = line.split("\\s+"); // Split by one or more spaces

        if (parts.length < 3) { // At least 3 parts are required
            System.out.println("Invalid input format in coordinates");
            return Optional.empty();
        }

        StringBuilder name = new StringBuilder(parts[0].trim());

        for (int i = 1; i < parts.length; i++) {

            // Check if the next part is not a number then it's part of the name
            if (!Character.isDigit(parts[i].trim().charAt(0)) && parts[i].trim().charAt(0) != '-' && parts[i].trim().charAt(0) != '.') {
                name.append(" ").append(parts[i].trim());
                continue;
            }

            try {
                latitude = Double.parseDouble(parts[i++].trim());

            } catch (NumberFormatException e) {
                System.out.println("Invalid latitude: " + e.getMessage());
                return Optional.empty();
            }

            try {
                longitude = Double.parseDouble(parts[i]);


            } catch (NumberFormatException e) {
                System.out.println("Invalid longitude: " + e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.of(new Capital(name.toString(), latitude, longitude));
    }

    public static String[] parseEdges(String line) {
        //this split based on 2 empty spaces or more
        String[] parts = line.split("\\s{2,}");

        if (parts.length != 2) {
            System.out.println("Invalid input format in edges");
            return null;
        }
        return new String[]{parts[0], parts[1]};
    }

}
