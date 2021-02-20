package tetris;

import tetris.tetromino.*;
import tetris.map.*;

import java.util.Random;
// import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tetris {
    private final int ACTION_TIME = 500; // 最大下落时间
    private final int CHECK_TIME = 250; // 最大检查时间（检查是指检查是否下一个，是否胜利等，在allCheck()调用;
    public static final int BOARD_WIDTH = 10; // 板宽
    public static final int BOARD_HEIGHT = 25; // 板高
    private final int CELL_SIZE = 20; // 一个Cell的大小
    private boolean[][] posiBoard = new boolean[BOARD_WIDTH][BOARD_HEIGHT]; // 板占位情况
    private int[][] colorBoard = new int[BOARD_WIDTH][BOARD_HEIGHT]; // 板颜色

    private Tetromino nowTetromino; // 现在下落的方块
    private Tetromino nextTetromino; // 下一个下落的方块

    private MyJPanel mjp = new MyJPanel(); // MyJpanel类实现
    private JFrame jf = new JFrame("Tetris"); // JFream实现

    private int nowTetroColor; // 现在下落方块的颜色
    private int nextTetroColor; // 下一个方块颜色

    private final Color[] allColors = new Color[] { Color.gray, Color.blue, Color.red, Color.green, Color.PINK,
            Color.yellow, Color.orange, Color.MAGENTA }; // 总颜色

    private boolean isLose; // 失败标志
    private int level; // 等级
    private int point; // 分数
    private int maxPoint; // 最高分数

    private Timer actionTimer; // 下落定时器
    private Timer checkTimer; // 检查定时器
    private int checkTime = CHECK_TIME; // 最大检查时间
    private int actionTime = ACTION_TIME; // 最大下落时间

    private OriginMap mapBoard; // 地图
    private int mapLevel; // 地图等级
    private boolean lastMap; // 最后地图flag

    private long allTime; // 总时长
    private long oneGameTime; // 一次游戏时长
    private int playTimes; // 游玩次数

    public void nextMap() // 换地图
    {
        mapBoard = mapBoard.nextMap();
        reStart();
    }

    public void cleanBoard() // 清空板
    {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                this.colorBoard[i][j] = 0;
                this.posiBoard[i][j] = false;
            }
        }
    }

    public void destroyLine() // 判断消行
    {
        int pointAdd = 0;
        for (int j = BOARD_HEIGHT - 1; j >= 0; j--) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                if (this.posiBoard[i][j] == false && mapBoard.getXY(i, j) == false) {
                    break;
                }
                if (i == BOARD_WIDTH - 1) {
                    this.destroyL(j);
                    j++;
                    pointAdd++;
                }
            }
        }
        for (int i = 0; i < pointAdd; i++) {
            point += pointAdd;
        }
    }

    public void dropCell(int i, int j) { // 下落
        this.posiBoard[i][j] = this.posiBoard[i][j - 1];
        this.colorBoard[i][j] = this.colorBoard[i][j - 1];
    }

    public void destroyL(int l) // 消行
    {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = l; j > 0; j--) {
                if (mapBoard.getXY(i, j)) {
                    break;
                } else {
                    dropCell(i, j);
                }

            }
        }
        for (int i = 0; i < BOARD_WIDTH; i++) {
            this.posiBoard[i][0] = false;
            this.colorBoard[i][0] = 0;
        }
    }

    public void checkNext() { // 判断是否切换下一个方块
        boolean flag = false;
        int[][] cellsXY = nowTetromino.getCellsXY();

        for (int i = 0; i < 4; i++) {
            if (cellsXY[i][1] != BOARD_HEIGHT - 1) {
                if (posiBoard[cellsXY[i][0]][cellsXY[i][1] + 1] || mapBoard.getXY(cellsXY[i][0], cellsXY[i][1] + 1)) {
                    flag = true;
                    break;
                }
            } else if (cellsXY[i][1] == BOARD_HEIGHT - 1) {
                flag = true;
            }
        }
        if (flag) {
            for (int i = 0; i < 4; i++) {
                posiBoard[cellsXY[i][0]][cellsXY[i][1]] = true;
                colorBoard[cellsXY[i][0]][cellsXY[i][1]] = nowTetroColor;
            }
            this.nowTetromino = this.nextTetromino;
            nowTetroColor = nextTetroColor;
            
            nextTetromino = Tetromino.randomTetromino();
            Random rd = new Random();
            nextTetroColor = rd.nextInt(7) + 1;
        }
    }

    public void checkLose() { // 检查失败
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (posiBoard[i][0]) {
                isLose = true;
            }
        }
    }

    public void checkLevel() { // 检查等级
        if (point >= 10 + (int) Math.pow(level, 2) * 10 && level < 9) {
            level++;
            actionTime = ACTION_TIME - ((int) Math.pow(level, 2) * 5);
            checkTime = CHECK_TIME - (level * 5);
            actionTimer.setDelay(actionTime);
            checkTimer.setDelay(checkTime);
        }
    }

    public void checkLastMap() { // 检查是否是最后一张地图
        if (mapLevel == OriginMap.MAP_COUNT) {
            lastMap = true;
        }
    }

    public void allCheck() { // 执行所有check任务
        if (!isLose) {
            this.checkNext();
            this.destroyLine();
            this.checkLose();
            this.checkLevel();
            this.checkLastMap();
        }
    }

    public void startGame() { // 初始化所有项目
        oneGameTime = System.currentTimeMillis();
        cleanBoard();
        isLose = false;
        nowTetromino = Tetromino.randomTetromino();
        nextTetromino = Tetromino.randomTetromino();
        Random rd = new Random();
        nowTetroColor = rd.nextInt(7) + 1;
        nextTetroColor = rd.nextInt(7) + 1;
        level = 0;
        point = 0;
        playTimes++;
        checkTimer.setDelay(CHECK_TIME);
        actionTimer.setDelay(ACTION_TIME);
    }

    public void returnFirstMap() {
        mapBoard = new OriginMap();
        mapLevel = 1;
        lastMap = false;
        reStart();
    }

    public void reStart() { // 调用startGame()
        checkTimer.restart();
        actionTimer.restart();

        startGame();
    }

    public void tetroRotateRight() { // 换形状
        boolean flag = true;
        int[][] c = nowTetromino.changeDa();
        for (int i = 0; i < 4; i++) {
            if ((c[i][0] >= 0 && c[i][0] < BOARD_WIDTH) && (c[i][1] >= 0 && c[i][1] < BOARD_HEIGHT)) {
                if (posiBoard[c[i][0]][c[i][1]]) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            nowTetromino.rotateRight();
        }
    }

    public void tetroLeftMove() { // 左移
        boolean flag = true;
        int[][] c = nowTetromino.getCellsXY();
        for (int i = 0; i < 4; i++) {
            if (c[i][0] - 1 > 0 && c[i][0] - 1 < BOARD_WIDTH) {
                if (posiBoard[c[i][0] - 1][c[i][1]] || mapBoard.getXY(c[i][0] - 1, c[i][1])) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            nowTetromino.leftMove();
        }
    }

    public void tetroRightMove() { // 右移
        boolean flag = true;
        int[][] c = nowTetromino.getCellsXY();
        for (int i = 0; i < 4; i++) {
            if (c[i][0] + 1 > 0 && c[i][0] + 1 < BOARD_WIDTH) {
                if (posiBoard[c[i][0] + 1][c[i][1]] || mapBoard.getXY(c[i][0] + 1, c[i][1])) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            nowTetromino.rightMove();
        }
    }

    public void tetroDropMove() { // 下移
        boolean flag = true;
        int[][] c = nowTetromino.getCellsXY();
        for (int i = 0; i < 4; i++) {
            if (c[i][1] + 1 > 0 && c[i][1] + 1 < BOARD_HEIGHT) {
                if (posiBoard[c[i][0]][c[i][1] + 1] || mapBoard.getXY(c[i][0], c[i][1] + 1)) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            nowTetromino.dropMove();
        }
    }

    public void init() { // 主要程序
        allTime = System.currentTimeMillis();
        mapBoard = new OriginMap();
        mapLevel = 1;
        maxPoint = 0;
        playTimes = 0;
        this.cleanBoard();

        mjp.setPreferredSize(new Dimension((BOARD_WIDTH + 10) * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        KeyAdapter keyProcessor = new myKeyAdapter();
        jf.addKeyListener(keyProcessor);
        jf.add(mjp);
        actionTimer = new Timer(ACTION_TIME, new actionTimerListener());
        actionTimer.start();
        checkTimer = new Timer(CHECK_TIME, new checkTimerListener());
        checkTimer.start();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.startGame();
        jf.pack();
        jf.setVisible(true);
        Timer updateTitle = new Timer(1, new actionTimerListener() {
            public void actionPerformed(ActionEvent e) {
                jf.setTitle("Tetris  " + "总游戏时长 " + (int) ((System.currentTimeMillis() - allTime) / 1000f) + "s "
                        + "游玩次数: " + playTimes);
                mjp.updateUI();
            }
        });

        updateTitle.start();
    }

    public static void main(String[] args) {
        new Tetris().init();
    }

    class MyJPanel extends JPanel { // 定义MyJpanel类画图

        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g) {

            if (isLose) {
                g.setColor(Color.black);
                g.drawString("you lose, press \"R\" to restart", 12 * CELL_SIZE, 10 * CELL_SIZE);
            } else {
                g.setColor(Color.white);
                g.fillRect(0, 0, BOARD_HEIGHT * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE);
                for (int i = 0; i < BOARD_WIDTH; i++) { // 画背景
                    for (int j = 0; j < BOARD_HEIGHT; j++) {

                        if (colorBoard[i][j] == 0) {
                            g.setColor(Color.white);
                            g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            g.setColor(allColors[colorBoard[i][j]]);
                            g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        } else {
                            g.setColor(allColors[colorBoard[i][j]]);
                            g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            g.setColor((allColors[0]));
                            g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        }

                        if (mapBoard.getXY(i, j)) {
                            g.setColor(Color.black);
                            g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        }
                    }
                }

                int[][] a = nowTetromino.getCellsXY();
                for (int i = 0; i < 4; i++) {
                    g.setColor(allColors[nowTetroColor]);
                    g.fillRect(a[i][0] * CELL_SIZE, a[i][1] * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }

                int[][] b = nextTetromino.getCellsXY();
                for (int i = 0; i < 4; i++) {
                    g.setColor(allColors[nextTetroColor]);
                    g.fillRect((b[i][0] + 10) * CELL_SIZE, (b[i][1] + 5) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }

            g.setColor(Color.black);
            g.drawString("分数：" + point, 11 * CELL_SIZE, 2 * CELL_SIZE);
            g.drawString("等级：" + level, 15 * CELL_SIZE, 2 * CELL_SIZE);
            g.drawString("使用上下左右控制", 11 * CELL_SIZE, 18 * CELL_SIZE);
            g.drawString("按L+U升一级", 1 * CELL_SIZE, (BOARD_HEIGHT + 1) * CELL_SIZE);
            if (mapLevel < OriginMap.MAP_COUNT) {
                g.drawString("当分数大于" + mapLevel * 50 + "后，可按m换地图", 11 * CELL_SIZE, 19 * CELL_SIZE);
            } else {
                g.drawString("当前已是最后地图，按m回到最初地图", 11 * CELL_SIZE, 19 * CELL_SIZE);
            }

            if (point > maxPoint) {
                maxPoint = point;
            }
            g.drawString("最高分数: " + maxPoint, 11 * CELL_SIZE, 3 * CELL_SIZE);
            if (!isLose) {
                g.drawString("此次游戏时长: " + (int) ((System.currentTimeMillis() - oneGameTime) / 1000f) + "s",
                        11 * CELL_SIZE, 1 * CELL_SIZE);
            }
        }

    }

    class actionTimerListener implements ActionListener { // 定义定时下落监听
        public void actionPerformed(ActionEvent e) {
            tetroDropMove();
            // allCheck();
        }
    }

    class checkTimerListener implements ActionListener { // 定义定时检查监听
        public void actionPerformed(ActionEvent e) {
            allCheck();
        }
    }

    class myKeyAdapter extends KeyAdapter { // 定义键盘监听

        public void keyPressed(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_UP) {
                tetroRotateRight();
            }
            if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                tetroRightMove();
            }
            if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                tetroLeftMove();
            }
            if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                tetroDropMove();
            }
            if (ke.getKeyCode() == KeyEvent.VK_R && isLose) {
                reStart();
            }
            if (ke.getKeyCode() == KeyEvent.VK_U && ke.getKeyCode() == KeyEvent.VK_U) {
                point = 10 + (int) Math.pow(level, 2) * 10;
            }
            if (ke.getKeyCode() == KeyEvent.VK_M && point >= mapLevel * 50) {
                mapLevel++;
                nextMap();
            }
            if (ke.getKeyCode() == KeyEvent.VK_M && lastMap) {
                returnFirstMap();
            }
        }

    }

}

// 定义myCanvas类，继承Canvas，已弃用
// class myCanvas extends Canvas{
// public void paint(Graphics g) {
// if (isLose) {
// g.setColor(Color.black);
// g.drawString("you lose, press \"R\" to restart", 12 * CELL_SIZE, 10 *
// CELL_SIZE);
// } else {
// g.setColor(Color.white);
// g.fillRect(0, 0, 20 * CELL_SIZE, 20 * CELL_SIZE);
// for (int i = 0; i < 10; i++) {
// for (int j = 0; j < 20; j++) {

// if (colorBoard[i][j] == 0) {
// g.setColor(Color.white);
// g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
// g.setColor(allColors[colorBoard[i][j]]);
// g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
// } else {
// g.setColor(allColors[colorBoard[i][j]]);
// g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
// g.setColor((allColors[0]));
// g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
// }
// }
// }

// int[][] a = nowTetromino.getCellsXY();
// for (int i = 0; i < 4; i++) {
// g.setColor(allColors[nowTetroColor]);
// g.fillRect(a[i][0] * CELL_SIZE, a[i][1] * CELL_SIZE, CELL_SIZE, CELL_SIZE);
// }

// int[][] b = nextTetromino.getCellsXY();
// for (int i = 0; i < 4; i++) {
// g.setColor(allColors[nextTetroColor]);
// g.fillRect((b[i][0] + 10) * CELL_SIZE, (b[i][1] + 2) * CELL_SIZE, CELL_SIZE,
// CELL_SIZE);
// }
// }
// }
// }
