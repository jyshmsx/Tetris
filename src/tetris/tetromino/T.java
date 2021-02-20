package tetris.tetromino;

public class T extends Tetromino {
    public T() {
        super();
        switch (rotaFlag) {
        case 0:
            cells[0] = new Cell(4, 0);
            cells[1] = new Cell(3, 0);
            cells[2] = new Cell(4, 1);
            cells[3] = new Cell(5, 0);
            break;
        case 1:
            cells[0] = new Cell(4, 1);
            cells[1] = new Cell(4, 0);
            cells[2] = new Cell(3, 1);
            cells[3] = new Cell(4, 2);
            break;
        case 2:
            cells[0] = new Cell(4, 1);
            cells[1] = new Cell(5, 1);
            cells[2] = new Cell(4, 0);
            cells[3] = new Cell(3, 1);
            break;
        case 3:
            cells[0] = new Cell(4, 1);
            cells[1] = new Cell(4, 2);
            cells[2] = new Cell(5, 1);
            cells[3] = new Cell(4, 0);
            break;
        default:
            break;
        }

        changeX = new int[][] { { 0, 0, 0, 0 }, { -1, 1, 1, -1 }, { -1, -1, 1, 1 }, { 1, -1, -1, 1 } };
        changeY = new int[][] { { 0, 0, 0, 0 }, { -1, -1, 1, 1 }, { 1, -1, -1, 1 }, { 1, 1, -1, -1 } };
    }
    /*
     * new Cell(a, b)中，a是x的坐标，b是y的坐标 尽量选不动的cell为cells[0]
     * 即对于chagex[i][j]。i表示是第几个cell，j表示从j-1到j状态时，x的相对大小。
     * 即对于第二个cell，从初始状态时到第二个状态时，x的改变量，存储在changeX[1][1]，依此类推，
     * 从第四状态到第一状态（即初始状态）时，x的改变量，存储在chengeX[1][0]。
     * 对于第三个cell，则是changeX[2][0]~changeX[2][3]存储 changeY[][] 同理
     */
}