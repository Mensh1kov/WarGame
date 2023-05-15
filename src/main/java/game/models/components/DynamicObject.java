package game.models.components;

public class DynamicObject extends GameObject {
    private int speed = 5;
    private double directionX;
    private double directionY;
    public DynamicObject(int x, int y, int width, int height, int speed, double directionX, double directionY)
    {
        super(x, y, width, height);
        setDirection(directionX, directionY);
        setSpeed(speed);
    }

    public void setDirection(double directionX, double directionY)
    {
        setDirectionX(directionX);
        setDirectionY(directionY);
    }

    public void setDirectionX(double directionX)
    {
        this.directionX = directionX;
    }

    public void setDirectionY(double directionY)
    {
        this.directionY = directionY;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getNewX()
    {
        return getX() + (int) (speed * directionX);
    }

    public int getNewY()
    {
        return getY() + (int) (speed * directionY);
    }
}
