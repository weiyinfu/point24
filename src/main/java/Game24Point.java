import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Game24Point extends JFrame {

public static void main(String[] args) {
    EventQueue.invokeLater(Game24Point::new);
}

Game24Point() {
    setTitle(Config.aim + "点游戏");
    setLayout(new BorderLayout());
    add(picPanel, BorderLayout.CENTER);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    addMouseListener(listenMouse);
    setExtendedState(MAXIMIZED_BOTH);
    setVisible(true);
    init();
}

int[] problem;
ProblemGenerator ai = new ProblemGenerator();
Judger right = new Judger();
Solver solver = new Solver();
int[][] pos;

int firstClick = -1;
JPanel picPanel = new JPanel() {
    private static final long serialVersionUID = 1L;

    public void paint(Graphics gg) {
        BufferedImage bit = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Font numFont = new Font("Consolas", Font.BOLD, 100);
        Graphics2D g = (Graphics2D) bit.getGraphics();
        g.clearRect(0, 0, bit.getWidth(), bit.getHeight());
        int h = bit.getHeight() / pos.length;
        for (int i = 0; i < pos.length; i++)
            for (int j = 0; j < 2; j++) {
                g.drawRect(j * h, i * h, h, h);
                if (pos[i][j] != -1)
                    drawString(bit, problem[pos[i][j]] + "", new Rectangle(j * h, i * h, h, h), numFont);
            }
        System.out.println(firstClick);
        if (firstClick != -1) {
            g.setStroke(new BasicStroke(6));
            g.drawRect((firstClick % 2) * h + 3, (firstClick / 2) * h + 3, h - 6, h - 6);
        }
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString(Config.aim + " Point", (int) (2.5 * h), h / 2);
        g.drawString("double click to see help", (int) (2.5 * h), h);
        gg.drawImage(bit, 0, 0, null);
    }
};

void init() {
    problem = ai.getProblem();
    pos = new int[problem.length - 1][2];
    int k = 0;
    for (int i = pos.length - 1; i >= 0; i--)
        for (int j = 0; j < 2; j++)
            if (k < problem.length) {
                pos[i][j] = k;
                k++;
            } else
                pos[i][j] = -1;
}

/**
 * On BufferedImage paper draw String s in Rectangle r with Font
 * font
 */
void drawString(BufferedImage paper, String s, Rectangle r, Font font) {
    BufferedImage bit;
    Graphics2D g = (Graphics2D) paper.getGraphics();
    Rectangle2D rec = font.getStringBounds(s, g.getFontRenderContext());
    LineMetrics metric = font.getLineMetrics(s, g.getFontRenderContext());
    bit = new BufferedImage((int) rec.getWidth(), (int) rec.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D gg = (Graphics2D) bit.getGraphics();
    gg.setColor(Color.RED);
    gg.setFont(font);
    gg.drawString(s, 0, metric.getAscent());
    g.drawImage(bit, r.x, r.y, r.width, r.height, null);
}

int getPic(Point p) {
    int h = getHeight() / pos.length;
    if (p.x > h * 2)
        return -1;
    else {
        int ans = (p.y / h) * 2 + p.x / h;
        return ans;
    }
}

MouseListener listenMouse = new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            help();
            return;
        }
        if (firstClick == -1) {
            firstClick = getPic(e.getPoint());
            picPanel.repaint();
        } else {
            int secondClick = getPic(e.getPoint());
            if (firstClick == secondClick) {
                firstClick = -1;
                picPanel.repaint();
                return;
            }
            int temp = pos[firstClick / 2][firstClick % 2];
            pos[firstClick / 2][firstClick % 2] = pos[secondClick / 2][secondClick % 2];
            pos[secondClick / 2][secondClick % 2] = temp;
            picPanel.repaint();
            firstClick = -1;
            if (right.ok(problem, pos)) {
                win();
                init();
                picPanel.repaint();
            }
        }
    }
};

void help() {
    JOptionPane.showConfirmDialog(this, solver.getAns(problem), "help", JOptionPane.DEFAULT_OPTION);
}

void win() {
    String ans = "";
    for (String s : right.ans)
        ans += s + "\n";
    JOptionPane.showConfirmDialog(this, ans, "You win!!", JOptionPane.CLOSED_OPTION);
}
}
