package tetris.map;

public class Map1 extends OriginMap {

    public Map1(int mapFlag) {
        super();
        this.mapFlag = mapFlag;
        mapBoard[1][BOARD_HEIGHT - 1] = true;
        mapBoard[3][BOARD_HEIGHT - 1] = true;
        mapBoard[6][BOARD_HEIGHT - 1] = true;
        mapBoard[8][BOARD_HEIGHT - 1] = true;

    }
}