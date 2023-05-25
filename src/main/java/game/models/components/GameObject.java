package game.models.components;

import java.awt.Rectangle;
import java.io.Serializable;

public class GameObject implements Serializable
{
    private Rectangle hitbox;
    private long id;

    public GameObject(long id, int x, int y, int width, int height)
    {
        this.hitbox = new Rectangle(x, y, width, height);
        this.id = id;
    }

    public Rectangle getHitbox()
    {
        return hitbox;
    }

    public void setX(int x)
    {
        hitbox.x = x;
    }

    public void setY(int y)
    {
        hitbox.y = y;
    }

    public int getX()
    {
        return hitbox.x;
    }

    public int getY()
    {
        return hitbox.y;
    }

    public int getWidth()
    {
        return hitbox.width;
    }

    public int getHeight()
    {
        return hitbox.height;
    }

    public void move(int x, int y)
    {
        setX(x);
        setY(y);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
