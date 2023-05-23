package game.models.components;

import java.io.Serializable;

public class Bullet extends DynamicObject implements Serializable
{
    private int damage;
    private int senderId;

    public Bullet(int id, int senderId, int x, int y, int damage, int speed, double directionX, double directionY)
    {
        super(id, x, y, 5, 5, speed, directionX, directionY);
        this.damage = damage;
        this.senderId = senderId;
    }

    public int getSenderId()
    {
        return senderId;
    }

    public int getDamage()
    {
        return damage;
    }
}
