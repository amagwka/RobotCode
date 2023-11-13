package frc.robot.utils;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class USensorAverage {
    private static final int MAX_SAMPLES = 5;
    private final Queue<Double> samples;
    private double sum = 0.0;

    public USensorAverage(){
        samples =  new ArrayDeque<>();
    }

    public void addSample(double sample) {
        sum += sample;
        samples.add(sample);
        
        if (samples.size() > MAX_SAMPLES) {
            sum -= samples.remove();
        }
    }

    public double getAverage() {
        if (samples.isEmpty()) {
            return 0.0;
        }
        return sum / samples.size();
    }

    public double getMedian() {
        if (samples.isEmpty()) {
            return 0.0;
        }
    
        ArrayList<Double> sortedSamples = new ArrayList<>(samples);
        sortedSamples.removeIf(Objects::isNull);
        Collections.sort(sortedSamples);
    
        int middle = sortedSamples.size() / 2;
        if (sortedSamples.size() % 2 == 0) {
            return (sortedSamples.get(middle - 1) + sortedSamples.get(middle)) / 2.0;
        } else {
            return sortedSamples.get(middle);
        }
    }
    
}
