package game.models;

import game.models.components.DynamicObject;

public class Zombie extends DynamicObject
{
    private int targetX;
    private int targetY;

    public Zombie(int x, int y, int width, int height, int speed, int targetX, int targetY)
    {
        super(x, y, width, height, speed, 0, 0);
        this.targetX = targetX;
        this.targetY = targetY;
        updateDirectionX();
        updateDirectionY();
    }

    public void setTarget(int x, int y)
    {
        if (x != targetX)
        {
            this.targetX = x;
        }
        else if (y != targetY)
        {
            this.targetY = y;
        }
        updateDirectionX();
        updateDirectionY();
    }

    private double getLenVector(double deltaX, double deltaY)
    {
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private void updateDirectionX()
    {
        double deltaX = targetX - getX();
        setDirectionX(deltaX / getLenVector(deltaX, targetY - getY()));
    }

    private void updateDirectionY()
    {
        double deltaY = targetY - getY();
        setDirectionY(deltaY / getLenVector(targetX - getX(), deltaY));
    }
}
