package model;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem {
    private final List<Item> items;
    public final int weightLimit;

    private Problem(List<Item> items, int weightLimit) {
        this.items = items;
        this.weightLimit = weightLimit;
    }

    public List<Item> items() {
        return Collections.unmodifiableList(items);
    }

    public static Problem generate(Random random, ProblemParameters problemParameters) {
        int minValue = problemParameters.minValue;
        int maxValue = problemParameters.maxValue;
        int minWeight = problemParameters.minWeight;
        int maxWeight = problemParameters.maxWeight;

        List<Item> items = IntStream.range(0, random.nextInt())
                .mapToObj( throwaway -> new Item(
                        random.nextInt(maxValue - maxValue) + minValue,
                        random.nextInt(maxWeight - minWeight) +  minWeight))
                .collect(Collectors.toList());

        return new Problem(items, problemParameters.weightLimit);
    }

    public Solution emptySolution() {
        return new Solution();
    }

    public class Solution {
        private final BitSet takenItems;

        private Solution() {
            this.takenItems = new BitSet(Problem.this.items.size());
        }

        private Solution(BitSet takenItems) {
            this.takenItems = takenItems;
        }

        public List<Item> potentialItems() {
            return Problem.this.items();
        }

        public Solution flip(int index) {
            if (index >= Problem.this.items.size()) {
                throw new IndexOutOfBoundsException("Attempting to flip non existent item");
            }

            BitSet newTakenItems = (BitSet)takenItems.clone();
            newTakenItems.flip(index);
            return new Solution(newTakenItems);
        }

        public boolean taken(int index) {
            if (index >= Problem.this.items.size()) {
                throw new IndexOutOfBoundsException("Attempting to flip non existent item");
            }

            return takenItems.get(index);
        }

        public Solution take(int index) {
            if (index >= Problem.this.items.size()) {
                throw new IndexOutOfBoundsException("Attempting to flip non existent item");
            }

            BitSet newTakenItems = (BitSet)takenItems.clone();
            newTakenItems.set(index);
            return new Solution(newTakenItems);
        }

        public Solution putBack(int index) {
            if (index >= Problem.this.items.size()) {
                throw new IndexOutOfBoundsException("Attempting to flip non existent item");
            }

            BitSet newTakenItems = (BitSet)takenItems.clone();
            newTakenItems.clear(index);
            return new Solution(newTakenItems);
        }

        public int value() {
            return takenItems.stream()
                    .mapToObj( index -> Problem.this.items.get(index) )
                    .mapToInt(item -> item.value)
                    .sum();
        }

        public int weight() {
            return takenItems.stream()
                    .mapToObj( index -> Problem.this.items.get(index) )
                    .mapToInt(item -> item.weight)
                    .sum();
        }

        public boolean admissible() {
            return weight() <= weightLimit;
        }
    }
}
