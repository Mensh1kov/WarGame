package game.models.components;

import game.models.components.DynamicObject;

public class Zombie extends DynamicObject
{
    private int hp;
    private int targetX;
    private int targetY;

    public Zombie(int id, int x, int y, int width, int height, int hp, int speed, int targetX, int targetY)
    {
        super(id, x, y, width, height, speed, 0, 0);
        this.targetX = targetX;
        this.targetY = targetY;
        this.hp = hp;
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

    public void hit(int damage)
    {
        hp -= damage;
    }

    public boolean isDead()
    {
        return hp <= 0;
    }

    public int getHp()
    {
        return hp;
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
