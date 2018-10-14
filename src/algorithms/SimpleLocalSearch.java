package algorithms;

import model.Problem;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimpleLocalSearch {

    public static Problem.Solution solve(Problem problem) {
        return solve(problem.emptySolution());
    }

    public static Problem.Solution solve(Problem.Solution solution) {
        Optional<Problem.Solution> betterSolution = bestAdmissibleAndBetterNeighbour(solution);
        while (betterSolution.isPresent()) {
            solution = betterSolution.get();
            betterSolution = bestAdmissibleAndBetterNeighbour(solution);
        }

        return solution;
    }

    public static Stream<Problem.Solution> neighbours(Problem.Solution solution) {
        return IntStream.range(0, solution.potentialItems().size())
                .mapToObj( solution::flip );
    }

    public static Stream<Problem.Solution> admissibleAndBetterneighbours(Problem.Solution solution) {
        return neighbours(solution)
                .filter( newSolution -> newSolution.admissible() )
                .filter( newSolution -> newSolution.value() > solution.value());
    }

    public static Optional<Problem.Solution> bestAdmissibleAndBetterNeighbour(Problem.Solution solution) {
        return admissibleAndBetterneighbours(solution)
                .max(Comparator.comparingInt(Problem.Solution::value));
    }
}
