import java.util.*;
import java.util.stream.Collectors;

/**
 * 发现比较困难的24点游戏，之所以称之为困难，是因为问题的可行解很少
 */
public class HardProblemFinder {
/**
 * 记忆化搜索，每个string形如1/5,5,1，对应的integer表示该问题的解法总数
 */
TreeMap<String, Integer> book = new TreeMap<>();//记录局面和可行解的个数
/**
 * key表示问题的解法种数
 * value表示问题列表
 */
TreeMap<Integer, List<int[]>> problems = new TreeMap<>();

/**
 * 编写一个问题生成器
 */
static class ProblemIterator implements Iterator<int[]> {
    int[] a;//当前问题
    boolean first = true;

    ProblemIterator(int numberCount) {
        a = new int[numberCount];
        for (int i = 0; i < a.length; i++) a[i] = Config.minNumber;
    }

    @Override
    public boolean hasNext() {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != Config.maxNumber) return true;
        }
        return false;
    }

    @Override
    public int[] next() {
        if (first) {
            first = false;
            return a;
        }
        for (int i = a.length - 1; i >= 0; i--) {
            if (a[i] < Config.maxNumber) {
                a[i]++;
                for (int j = i + 1; j < a.length; j++) {
                    a[j] = a[i];
                }
                break;
            }
        }
        return a;
    }
}

/**
 * 把问题记录下来
 */
void insert(int[] problem, int solutionCount) {
    if (!problems.containsKey(solutionCount)) problems.put(solutionCount, new LinkedList<>());
    problems.get(solutionCount).add(problem.clone());
}

/**
 * 对于问题生成器中的每个问题，进行求解
 */
void build() {
    for (int numberCount = Config.minNumberCount; numberCount <= Config.maxNumberCount; numberCount++) {
        ProblemIterator it = new ProblemIterator(numberCount);
        while (it.hasNext()) {
            int[] problem = it.next();
            int solutionCount = getSolutionCount(toNumList(problem));
            if (solutionCount >= 0) {
                insert(problem, solutionCount);
            }
        }
    }
}

/**
 * 打印问题的全部解法
 */
private void printAllSolution(List<Num> problem, LinkedList<String> ops) {
    if (problem.size() == 1) {
        if (problem.get(0).equalInt(Config.aim)) {
            System.out.println(ops);
        }
        return;
    }
    TreeSet<String> visited = new TreeSet<>();
    for (int i = 0; i < problem.size(); i++) {
        //如果i和i-1相同没有必要再来一遍
        for (int j = i + 1; j < problem.size(); j++) {
            String kk = problem.get(i) + "," + problem.get(j);
            if (visited.contains(kk)) continue;
            visited.add(kk);
            ArrayList<Num> next = new ArrayList<>(problem.size() - 1);
            for (int l = 0; l < problem.size(); l++) {
                if (l == i || l == j) continue;
                next.add(problem.get(l));
            }
            next.add(null);
            for (int op = 0; op < 6; op++) {//枚举每种运算
                try {
                    Num last = problem.get(i).calculate(op, problem.get(j));
                    next.set(next.size() - 1, last);
                    ops.addLast(problem.get(i).operation(op, problem.get(j)));
                    printAllSolution(next, ops);
                    ops.removeLast();
                } catch (ArithmeticException e) {

                }
            }
        }
    }
}

/**
 * 递归法求问题解法总数
 */
private int getSolutionCount(List<Num> problem) {
    if (problem.size() == 1) {
        if (problem.get(0).equalInt(Config.aim)) {
            return 1;
        } else {
            return 0;
        }
    }
    String k = problem.stream().sorted().map(Num::toString).collect(Collectors.joining(","));
    if (book.containsKey(k)) {
        return book.get(k);
    }
    int s = 0;
    TreeSet<String> visited = new TreeSet<>();
    for (int i = 0; i < problem.size(); i++) {
        //如果i和i-1相同没有必要再来一遍
        for (int j = i + 1; j < problem.size(); j++) {
            String kk = problem.get(i) + "," + problem.get(j);
            if (visited.contains(kk)) continue;
            else visited.add(kk);
            ArrayList<Num> next = new ArrayList<>(problem.size() - 1);
            for (int l = 0; l < problem.size(); l++) {
                if (l == i || l == j) continue;
                next.add(problem.get(l));
            }
            next.add(null);
            for (int op = 0; op < 6; op++) {//枚举每种运算
                try {
                    Num last = problem.get(i).calculate(op, problem.get(j));
                    next.set(next.size() - 1, last);
                    int solutionCount = getSolutionCount(next);
                    s += solutionCount;
                } catch (ArithmeticException e) {

                }
            }
        }
    }
    book.put(k, s);
    return s;
}

List<Num> toNumList(int[] problem) {
    return Arrays.stream(problem).mapToObj(Num::new).collect(Collectors.toList());
}

/**
 * 展示解法只有一种的问题
 */
void show() {
    List<Integer> solvable = new ArrayList<>(problems.keySet());
    System.out.println("问题的解法种数" + solvable);

    int total = problems.values().stream().mapToInt(List::size).sum();
    long solvableCount = total - problems.get(0).size();
    System.out.println("可解问题总数" + solvableCount + "/" + total);
    for (int i : problems.keySet()) {
        System.out.println("解法种数 " + i + " : 问题个数 " + problems.get(i).size());
    }
    if (problems.containsKey(1)) {
        problems.get(1).forEach(x -> {
            System.out.println(Arrays.stream(x).mapToObj(i -> i + "").collect(Collectors.toList()));
            printAllSolution(toNumList(x), new LinkedList<>());
        });
    } else {
        System.out.println("没有唯一解的问题");
    }
}

/**
 * 统计每个点游戏的可解问题占比
 */
void whichPointIsBest() {
    for (int i = 1; i < 100; i++) {
        Config.aim = i;
        HardProblemFinder finder = new HardProblemFinder();
        finder.build();
        System.out.println(i + " " + finder.problems.get(0).size());
    }
}

public static void main(String[] args) {
    HardProblemFinder finder = new HardProblemFinder();
    finder.build();
    finder.show();
}
}
