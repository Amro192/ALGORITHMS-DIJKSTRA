package com.othman.project3.model;


import java.util.*;

public class Graph {
    private final Map<String, Capital> capitals = new HashMap<>();
    private final Map<String, List<Edge>> adjList = new HashMap<>();

    public void addCapital(Capital capital) {
        capitals.put(capital.name(), capital);
        adjList.put(capital.name(), new ArrayList<>());
    }

    public void addEdge(String src, String dest) {
        Capital sourceCapital = capitals.get(src);
        Capital destCapital = capitals.get(dest);
        if (sourceCapital == null || destCapital == null) throw new IllegalArgumentException("Invalid capital");
        double weight = Capital.calculateDistance(sourceCapital, destCapital);
        adjList.get(src).add(new Edge(sourceCapital, destCapital, weight));
    }

    public Optional<Capital> get(String name) {
        return Optional.ofNullable(capitals.get(name));
    }

    //Complexity is O(V + E log V)
    public List<String> getShortestPath(String start, String end) {
        Queue<CityDistance> pq = new PriorityQueue<>(CityDistance::compareTo); // O(log V)
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String capital : capitals.keySet()) { // O(V)
            distances.put(capital, Double.MAX_VALUE);
        }

        distances.put(start, 0.0);
        pq.add(new CityDistance(capitals.get(start), 0.0)); // O(log V)
        // O(E log V)
        while (!pq.isEmpty()) {
            //returns the city with the smallest distance
            CityDistance current = pq.poll();
            if (visited.contains(current.city.name())) continue;
            visited.add(current.city.name());
            //if we reached the destination city, break
            if (current.city.name().equals(end)) break;
            for (Edge edge : adjList.get(current.city.name())) { // O(E)
                //if the city is already visited, skip
                if (visited.contains(edge.destination().name())) continue;
                double newDist = distances.get(current.city.name()) + edge.weight();
                //if the new distance is smaller than the previous distance, update the distance and the previous city
                if (newDist < distances.get(edge.destination().name())) {
                    distances.put(edge.destination().name(), newDist);
                    previous.put(edge.destination().name(), current.city.name());
                    pq.add(new CityDistance(edge.destination(), newDist)); // O(log V)
                }
            }
        }

        var path = new ArrayList<String>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    //this class used to store distance between capitals (accumulating)
    private record CityDistance(Capital city, double distance) implements Comparable<CityDistance> {
        @Override
        public int compareTo(CityDistance o) {
            return Double.compare(distance, o.distance);
        }
    }

    public Map<String, Capital> getCapitals() {
        return capitals;
    }
}
