package game.views;

import game.models.components.*;
import java.awt.*;

public class GameObjectViewFactory
{
    public static GameObjectView getGameObjectView(GameObject object)
    {
        Color color = Color.red;
        if (object instanceof Wall) color = Color.gray;
        else if (object instanceof Bullet) color = Color.black;

        return new GameObjectView(object, color);
    }
}
