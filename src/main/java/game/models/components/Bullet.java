package game.models.components;

import java.io.Serializable;

public class Bullet extends DynamicObject implements Serializable
{
    private int damage;
    private long senderId;

    public Bullet(long id, long senderId, int x, int y, int damage, int speed, double directionX, double directionY)
    {
        super(id, x, y, 5, 5, speed, directionX, directionY);
        this.damage = damage;
        this.senderId = senderId;
    }

    public long getSenderId()
    {
        return senderId;
    }

    public int getDamage()
    {
        return damage;
    }
}
