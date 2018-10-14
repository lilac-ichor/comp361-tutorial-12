package model;

public class ProblemParameters {
    public final int minValue;
    public final int maxValue;
    public final int minWeight;
    public final int maxWeight;
    public final int weightLimit;

    public ProblemParameters(int minValue, int maxValue, int minWeight, int maxWeight, int weightLimit) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.weightLimit = weightLimit;
    }
}
