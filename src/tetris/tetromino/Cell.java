package tetris.tetromino;


//一个小格
public class Cell {
    private int xPos;
    private int yPos;

    public Cell(int xPos, int yPos) //顶坐标
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    
    public void leftMove()  //左移
    {
        this.xPos--;
    }
    public void rightMove() //右移
    {
        this.xPos++;
    }
    public void dropMove()  //下降
    {
        this.yPos++;
    }

    public int getXPos()    //获取x坐标
    {
        return xPos;
    }

    public int getYPos()    //获取y坐标
    {
        return yPos;
    }

    public void changeXPos(int xChange) //改变x坐标，
    {
        this.xPos += xChange;
    }

    public void changeYPos(int yChange) //改变y坐标
    {
        this.yPos += yChange;
    }
}