package game.models.components;

import java.awt.Rectangle;

public class GameObject
{
    private Rectangle hitbox;

    public GameObject(int x, int y, int width, int height)
    {
        this.hitbox = new Rectangle(x, y, width, height);
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
}
