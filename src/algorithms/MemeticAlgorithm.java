package algorithms;

import model.Problem;

import java.util.*;
import java.util.stream.Collectors;

public class MemeticAlgorithm {

    public static Problem.Solution solve(Problem problem) {
        Random random = new Random();
        int populationSize = 10;
        int elitismCarryover = 3;
        int iterations = 1000;
        double localSearchThreshold = 0.3;

        Problem.Solution best = problem.emptySolution();


        List<Problem.Solution> population = randomPopulation(random, problem, populationSize);
        for (int i = 0; i < iterations; i++) {
            List<Problem.Solution> newPopulation = population.stream().sorted(Comparator.comparingInt(Problem.Solution::value))
                    .limit(elitismCarryover)
                    .collect(Collectors.toList());

            if (newPopulation.get(0).value() > best.value()) {
                best = newPopulation.get(0);
            }

            while (newPopulation.size() < populationSize) {
                Problem.Solution parent1 = population.get(random.nextInt(population.size()));
                Problem.Solution parent2 = population.get(random.nextInt(population.size()));

                Problem.Solution child = crossover(parent1, parent2);

                if (random.nextDouble() < localSearchThreshold) {
                    child = SimpleLocalSearch.solve(child);
                }

                newPopulation.add(child);
            }
        }

        return best;
    }

    public static List<Problem.Solution> randomPopulation(Random random, Problem problem, int populationSize) {
        List<Problem.Solution> population = new ArrayList<>(populationSize);

        for (int i = 0; i < populationSize; i++) {
            population.add(randomize(problem.emptySolution(), random));
        }

        return population;
    }

    public static Problem.Solution randomize(Problem.Solution solution, Random random) {
        for (int j = 0; j < solution.potentialItems().size(); j++) {
            if (random.nextFloat() > 0.5f) {
                solution = solution.flip(j);
            }
        }
        return solution;
    }

    public static Problem.Solution crossover(Problem.Solution solution1, Problem.Solution solution2) {
        Problem.Solution child = solution1;
        int length = solution1.potentialItems().size();

        for (int i = length/2; i < length; i++) {
            if (solution2.taken(i)) {
                child = child.take(i);
            } else {
                child = child.putBack(i);
            }
        }

        return child;
    }
}
