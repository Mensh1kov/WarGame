package game.models.components;

import java.io.Serializable;

public class DynamicObject extends GameObject implements Serializable
{
    private int speed = 5;
    private double directionX;
    private double directionY;
    public DynamicObject(long id, int x, int y, int width, int height, int speed, double directionX, double directionY)
    {
        super(id, x, y, width, height);
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
