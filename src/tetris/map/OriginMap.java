package tetris.map;

import tetris.Tetris;

public class OriginMap{
    public static final int MAP_COUNT = 2;

    protected final int BOARD_WIDTH = Tetris.BOARD_WIDTH;
    protected final int BOARD_HEIGHT = Tetris.BOARD_HEIGHT;

    protected int mapFlag;
    protected boolean [][] mapBoard;
    public OriginMap(){
        mapFlag = 0;
        mapBoard = new boolean [BOARD_WIDTH][BOARD_HEIGHT];
        for(int i = 0; i < BOARD_WIDTH; i++)
        {
            for(int j = 0; j < BOARD_HEIGHT; j++)
            {
                mapBoard[i][j] = false;
            }
        }
    }

    public boolean getXY(int i, int j)
    {
        return mapBoard[i][j];
    }

    public OriginMap nextMap()
    {
        mapFlag++;
        mapFlag %= MAP_COUNT;
        OriginMap om = null;
        switch (mapFlag) {
            case 0:
                om = new OriginMap();
                break;
            case 1:
                om =  new Map1(mapFlag);
                break;
            default:
                om = new OriginMap();
                break;
        }
        return om;
    }
}