/**
 * 类名：Solver 功能：判断给定题目是否有解 数据成员：level 6层，层层递减 函数成员：init初始化类中数据 run主要算法部分
 * hasAnswer与外界的唯一接口 说明：效率第一，出题要保证有解，出题必须要快 负数是可以避免的，遇到负数马上返回 分数是不可以避免的
 */
class Solver {
public Num[][] level;

void init(int[] problem) {
    // for pretendingB's sake,i used alternate array
    level = new Num[problem.length][];
    for (int i = 0; i < problem.length; i++)
        level[i] = new Num[i + 1];
    // 为本类之level赋值，把int转化为Num
    for (int i = 0; i < problem.length; i++)
        level[problem.length - 1][i] = new Num(problem[i]);
}

boolean hasAnswer(int[] problem) {
    init(problem);
    return run(problem.length);
}

boolean run(int layer) {
    if (layer == 1) {
        if (level[0][0].up == Config.aim && level[0][0].down == 1)
            return true;
        else
            return false;
    }
    for (int i = 0; i < layer; i++)
        for (int j = i + 1; j < layer; j++) {
            int index = 0;
            //把除了i和j以外的值复制上去
            for (int k = 0; k < layer; k++) {
                if (k != i && k != j) {
                    level[layer - 2][index++] = level[layer - 1][k];
                }
            }
            for (int k = 0; k < 6; k++) {
                try {
                    level[layer - 2][layer - 2] = level[layer - 1][i].calculate(k, level[layer - 1][j]);
                    if (level[layer - 2][layer - 2].up < 0 || level[layer - 2][layer - 2].down < 0)
                        return false;
                    if (run(layer - 1))
                        return true;
                } catch (ArithmeticException e) {

                }
            }
        }
    return false;
}

public String getAns(int[] problem) {
    if (!hasAnswer(problem)) return "无解";
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < level.length; i++) {
        for (int j = 0; j <= i; j++) {
            builder.append(level[i][j]).append(" ");
        }
        builder.append('\n');
    }
    return builder.toString();
}
}
