import java.util.ArrayList;

/**
 * 类名：Judger 功能：判断人们给出的答案是否正确， 如果正确，将结果存储在本类的ans字符串数组变量中, 一个字符串表示一个算式
 */
class Judger {
Num[][] pos;
boolean[][] has;
ArrayList<String> ans;

void init(int[] problem, int[][] pos) {
    has = new boolean[pos.length][2];
    ans = new ArrayList<>();
    this.pos = new Num[pos.length][2];
    for (int i = 0; i < pos.length; i++) {
        for (int j = 0; j < 2; j++) {
            if (pos[i][j] != -1) {
                this.pos[i][j] = new Num(problem[pos[i][j]]);
                has[i][j] = true;
            } else
                has[i][j] = false;
        }
    }
}

public boolean ok(int[] problem, int[][] pos) {
    init(problem, pos);
    return run(0, new ArrayList<Num>());
}

/**
 * which 表示第几层 active 表示可用数字
 */
boolean run(int which, ArrayList<Num> active) {
    if (which == pos.length) {
        System.out.println("**********");
        for (String s : ans)
            System.out.println(s);
        if (active.get(0).up == Config.aim && active.get(0).down == 1 && active.size() == 1)
            return true;
        else
            return false;
    }
    if (has[which][0]) {
        if (has[which][1]) {
            for (int i = 0; i < 4; i++) {
                active.add(pos[which][0].calculate(i, pos[which][1]));
                ans.add(pos[which][0] + " " + pos[which][1] + "=" + active.get(active.size() - 1));
                if (run(which + 1, active))
                    return true;
                active.remove(active.size() - 1);
                ans.remove(ans.size() - 1);
            }
        } else {
            for (int i = 0; i < active.size(); i++) {
                Num temp = active.get(i);
                for (int k = 0; k < 4; k++) {
                    active.set(i, pos[which][0].calculate(k, temp));
                    ans.add(pos[which][0] + " " + temp + "=" + active.get(i));
                    if (run(which + 1, active))
                        return true;
                    active.set(i, temp);
                    ans.remove(ans.size() - 1);
                }
            }
        }
    } else {
        if (has[which][1]) {
            for (int i = 0; i < active.size(); i++) {
                Num temp = active.get(i);
                for (int k = 0; k < 4; k++) {
                    active.set(i, temp.calculate(k, pos[which][1]));
                    ans.add(temp + " " + pos[which][1] + "=" + active.get(i));
                    if (run(which + 1, active))
                        return true;
                    active.set(i, temp);
                    ans.remove(ans.size() - 1);
                }
            }
        } else {
            for (int i = 0; i < active.size(); i++) {
                for (int j = 0; j < active.size(); j++) {
                    if (j == i)
                        continue;
                    ArrayList<Num> nextActive = new ArrayList<Num>();
                    for (int t = 0; t < active.size(); t++)
                        if (t != i && t != j)
                            nextActive.add(active.get(t));
                    for (int k = 0; k < 4; k++) {
                        nextActive.add(active.get(i).calculate(k, active.get(j)));
                        ans.add(active.get(i) + " " + active.get(j) + "=" + nextActive.get(nextActive.size() - 1));
                        if (run(which + 1, nextActive))
                            return true;
                        ans.remove(ans.size() - 1);
                        nextActive.remove(nextActive.size() - 1);
                    }
                }
            }
        }
    }
    return false;
}
}
