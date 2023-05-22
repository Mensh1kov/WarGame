package game.models.components;

import java.io.Serializable;

public class StaticObject extends GameObject implements Serializable
{
    public StaticObject(int id, int x, int y, int width, int height)
    {
        super(id, x, y, width, height);
    }
}
