import java.util.Random;

public class ProblemGenerator {
Random r = new Random();
Solver solve = new Solver();

int[] getProblem() {
    int size = r.nextInt(Config.maxNumberCount - Config.minNumberCount + 1) + Config.minNumberCount;
    int[] problem = new int[size];
    do {
        for (int i = 0; i < size; i++)
            problem[i] = r.nextInt(Config.maxNumber - Config.minNumber + 1) + Config.minNumber;
    } while (!solve.hasAnswer(problem));
    return problem;
}
}

