/**
 * @author weidiao
 * @类名：Num
 * @功能：实现分数运算
 * @数据成员：分子up，分母down
 * @函数成员：加减乘除，add，sub，div，mul。最后一个总的调用calculate,将分数化为最简simple
 * @说明:二十四点游戏中是会出现分数的，比如1，1,5,5. 答案是：1/5=1/5; 5-1/5=24/5; 5*24/5=24;
 * @since 2014-11-7 / /**
 */
class Num implements Comparable<Num> {
public int up, down;

public Num() {

}

public Num(int num) {
    up = num;
    down = 1;
}

public Num(int up, int down) {
    this.up = up;
    this.down = down;
    simple();
}

Num add(Num b) {
    Num c = new Num();
    c.down = down * b.down;
    c.up = up * b.down + down * b.up;
    c.simple();
    return c;
}

Num sub(Num b) {
    Num c = new Num();
    c.down = down * b.down;
    c.up = up * b.down - down * b.up;
    c.simple();
    return c;
}

Num mul(Num b) {
    Num c = new Num();
    c.down = down * b.down;
    c.up = up * b.up;
    c.simple();
    return c;
}

Num div(Num b) {
    Num c = new Num();
    c.down = down * b.up;
    c.up = up * b.down;
    c.simple();
    return c;
}

int gcd(int x, int y) {
    if (y == 0) return x;
    return gcd(y, x % y);
}

private void simple() {
    int d = gcd(up, down);
    up /= d;
    down /= d;
}

public String toString() {
    if (down == 1)
        return up + "";
    else
        return up + "/" + down;
}

public String operation(int func, Num b) {
    Num res = calculate(func, b);
    switch (func) {
        case 0:
            return this.toString() + '+' + b.toString() + "=" + res;
        case 1:
            return this.toString() + '-' + b.toString() + "=" + res;
        case 2:
            return this.toString() + '*' + b.toString() + "=" + res;
        case 3:
            return this.toString() + '/' + b.toString() + "=" + res;
        case 4:
            return b.toString() + '-' + this.toString() + "=" + res;
        case 5:
            return b.toString() + '/' + this.toString() + "=" + res;
        default:
            throw new RuntimeException("unkown operation " + func);
    }
}

public Num calculate(int whichFunc, Num b) {
    Num ans = new Num();
    switch (whichFunc) {
        case 0:
            ans = add(b);
            break;
        case 1:
            ans = sub(b);
            break;
        case 2:
            ans = mul(b);
            break;
        case 3:
            ans = div(b);
            break;
        case 4:
            ans = b.sub(this);
            break;
        case 5:
            ans = b.div(this);
            break;
    }
    return ans;
}

@Override
public boolean equals(Object obj) {
    if (obj instanceof Num) {
        Num n = (Num) obj;
        return n.up * down == n.down * up;
    }
    return false;
}

public boolean equalInt(int x) {
    if (down == 1 && up == x) {
        return true;
    } else {
        return false;
    }
}

@Override
public int compareTo(Num o) {
    Num c = this.sub(o);
    return c.up * c.down;
}

public static void main(String[] args) {
    System.out.println(new Num(3, 1).equals(3));
}
}
