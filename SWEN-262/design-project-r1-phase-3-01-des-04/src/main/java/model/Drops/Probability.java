package model.Drops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Probability {

    private Map<String, Double> probabilityMap;

    // Constructor
    public Probability() {
        this.probabilityMap = new HashMap<>();
    }

    public Probability(String csvFilePath) {
        this.probabilityMap = new HashMap<>();
        readProbabilitiesFromCSV(csvFilePath);
        normalize();
    }

    // Add or update a probability for a given key
    public void setProbability(String key, double probability) {
        probabilityMap.put(key, probability);
    }

    // Normalize probabilities in the map
    private void normalize() {
        double sum = probabilityMap.values().stream().reduce(0.0, Double::sum);

        if (sum != 1.0) {
            for (Map.Entry<String, Double> entry : probabilityMap.entrySet()) {
                entry.setValue(entry.getValue() / sum);
            }
        }
    }

    // Read probabilities from a CSV file
    private void readProbabilitiesFromCSV(String csvFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String key = values[0].trim();
                    float probability = Float.parseFloat(values[values.length - 1].trim());
                    setProbability(key, probability);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void scaleProbabilities(double scalingFactor) {
        Map<String, Double> scaledProbabilities = new HashMap<>(probabilityMap);
        scaledProbabilities.replaceAll((key, value) -> Math.pow(value, 1/scalingFactor));
        normalize();
    }

    public String getRandomEvent() {
        Double randomValue = new Random().nextDouble();
        Double cumulativeProbability = 0.0;

        for (Map.Entry<String, Double> entry : probabilityMap.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        // If for some reason the loop doesn't return a key, return the last one
        return null;
    }

    public String getRandomEvent(double scalingFactor) {
        Probability newp = this.deepClone();
        newp.scaleProbabilities(scalingFactor);
        return getRandomEvent();
    }

    protected Probability deepClone() {
        Probability p = new Probability();
        for (Map.Entry<String, Double> entry : probabilityMap.entrySet()) {
            p.setProbability(entry.getKey(), entry.getValue());
        }
        return p;
    }

    @Override
    public String toString() {
        return "ProbabilityMap{" +
                "probabilityMap=" + probabilityMap +
                '}';
    }
}