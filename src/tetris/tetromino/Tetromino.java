package tetris.tetromino;

import java.util.Random;

import tetris.Tetris;

public abstract class Tetromino {
    protected final int X_Max = Tetris.BOARD_WIDTH - 1;      //最大X坐标
    protected final int X_Min = 0;      //最小x坐标
    protected final int Y_Max = Tetris.BOARD_HEIGHT - 1;     //最大y坐标
    protected final int Y_Min = 0;      //最小y坐标
    protected Cell[] cells;
    protected int[][] changeX; //x的改变量来决定状态改变的相对坐标值
    protected int[][] changeY; //同上，y
    protected int rotaFlag;     // 当前状态flag

    public Tetromino() {
        Random rd = new Random();
        rotaFlag = rd.nextInt(4);        
        cells = new Cell[4];
    }

    private boolean checkRMove() {          //检查右移是否允许
        boolean flag = true;
        for (Cell cell : cells) {
            if (cell.getXPos() + 1> X_Max) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean checkLMove() {          //检查左移是否允许
        boolean flag = true;
        for (Cell cell : cells) {
            if (cell.getXPos() - 1 < X_Min) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private boolean checkDMove() {          //检查下落是否允许
        boolean flag = true;
        for (Cell cell : cells) {
            if (cell.getYPos() + 1> Y_Max) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public void rightMove() // 右移
    {
        if (checkRMove()) {
            for (Cell cell : cells) {
                cell.rightMove();
            }
        }

    }

    public void leftMove() // 左移
    {
        if (checkLMove()) {
            for (Cell cell : cells) {
                cell.leftMove();
            }
        }

    }

    public void dropMove() // 下落
    {
        if (checkDMove()) {
            for (Cell cell : cells) {
                cell.dropMove();
            }
        }

    }

    public static Tetromino randomTetromino() // 随机定义一个方块
    {                                         
        Tetromino te = null;
        Random rd = new Random();
        switch (rd.nextInt(70) % 7) {   
        case 0:
            te = new T();
            break;
        case 1:                    
            te = new O();          
            break;
        case 2:
            te = new Z();
            break;
        case 3:
            te = new S();
            break;    
        case 4:
            te = new J();
            break;
        case 5:
            te = new L();
            break;
        case 6:
            te = new I();
            break;
        default:
            te = new T();                             
            break;
        }
        return te;
    }

    public int[][] getCellsXY() {           //获取当前cells的坐标
        int[][] a = new int[4][2];
        for (int i = 0; i < 4; i++) {
            a[i][0] = cells[i].getXPos();
            a[i][1] = cells[i].getYPos();
        }
        return a;
    }

    public int[][] changeDa() { // 获取下一次状态改变后的坐标
        int[][] a = new int[4][2];
        for (int i = 0; i < 4; i++) {
            a[i][0] = cells[i].getXPos() + changeX[i][(rotaFlag + 1) % 4];
            a[i][1] = cells[i].getYPos() + changeY[i][(rotaFlag + 1) % 4];
        }
        return a;
    }

    public boolean checkChange() { // 检查是否允许变形
        boolean flag = true;
        for (int i = 0; i < 4; i++) {
            int xc = cells[i].getXPos() + changeX[i][(rotaFlag + 1) % 4];
            int yc = cells[i].getYPos() + changeY[i][(rotaFlag + 1) % 4];
            if (!((xc >= X_Min && xc <= X_Max) && (yc >= Y_Min && yc <= Y_Max))) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public void rotateRight() { // 顺时针变形

        if (checkChange()) {
            rotaFlag++;
            rotaFlag %= 4; // 保证rotaFlag小于4

            for (int i = 0; i < 4; i++) {
                cells[i].changeXPos(changeX[i][rotaFlag]);
                cells[i].changeYPos(changeY[i][rotaFlag]);
            }
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(i + " : (" + cells[i].getXPos() + " , " + cells[i].getYPos() + "), ");
        }
        return sb.toString();
    }
}