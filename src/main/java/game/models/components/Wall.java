package game.models.components;

import java.io.Serializable;

public class Wall extends StaticObject implements Serializable
{
    public Wall(long id, int x, int y, int width, int height)
    {
        super(id, x, y, width, height);
    }
}
