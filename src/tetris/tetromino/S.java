package tetris.tetromino;

/**
 * 
 * @author 徐钦妮
 *
 */

public class S extends Tetromino {
    public S() {
        super();
        switch (rotaFlag) {
        case 0:
            cells[0] = new Cell(4, 0);
            cells[1] = new Cell(5, 0);
            cells[2] = new Cell(3, 1);
            cells[3] = new Cell(4, 1);
            break;
        case 1:
            cells[0] = new Cell(4, 1);
            cells[1] = new Cell(4, 0);
            cells[2] = new Cell(5, 1);
            cells[3] = new Cell(5, 2);
            break;
        case 2:
            cells[0] = new Cell(4, 0);
            cells[1] = new Cell(5, 0);
            cells[2] = new Cell(3, 1);
            cells[3] = new Cell(4, 1);
            break;
        case 3:
            cells[0] = new Cell(4, 1);
            cells[1] = new Cell(4, 0);
            cells[2] = new Cell(5, 1);
            cells[3] = new Cell(5, 2);
            break;
        default:
            break;
        }

        changeX = new int[][] { { 0, 0, 0, 0 }, { 1, -1, 1, -1 }, { -2, 2, -2, 2 }, { -1, 1, -1, 1 } };
        changeY = new int[][] { { 0, 0, 0, 0 }, { 1, -1, 1, -1 }, { 1, -1, 1, -1 }, { 0, 0, 0, 0 } };
    }
}
