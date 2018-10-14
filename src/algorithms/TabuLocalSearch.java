package algorithms;

import model.Problem;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TabuLocalSearch {

    public static Problem.Solution solve(Problem problem) {
        Problem.Solution best = problem.emptySolution();
        Problem.Solution current = best;
        Set<Problem.Solution> visited = new HashSet<>();
        visited.add(current);

        Optional<Problem.Solution> potentialSolution = bestAdmissibleUnvisitedNeighbour(current, visited);
        while (potentialSolution.isPresent()) {
            current = potentialSolution.get();
            best = current.value() > best.value() ? current : best;
        }

        return best;
    }

    public static Stream<Problem.Solution> neighbours(Problem.Solution solution) {
        return IntStream.range(0, solution.potentialItems().size())
                .mapToObj( solution::flip );
    }

    public static Stream<Problem.Solution> admissibleNeighbours(Problem.Solution solution) {
        return neighbours(solution)
                .filter( newSolution -> newSolution.admissible() );
    }

    public static Optional<Problem.Solution> bestAdmissibleUnvisitedNeighbour(Problem.Solution solution, Set<Problem.Solution> visited) {
        return admissibleNeighbours(solution)
                .filter( newSolution -> !visited.contains(newSolution) )
                .max(Comparator.comparingInt(Problem.Solution::value));

    }
}
