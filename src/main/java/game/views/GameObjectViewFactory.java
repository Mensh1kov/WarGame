package game.views;

import game.models.Zombie;
import game.models.components.*;
import java.awt.*;

public class GameObjectViewFactory
{
    public static GameObjectView getGameObjectView(GameObject object)
    {
        Color color = Color.red;
        if (object instanceof Wall) color = Color.GRAY;
        else if (object instanceof Bullet) color = Color.BLACK;
        else if (object instanceof Zombie) return new ZombieView((Zombie) object, Color.GREEN);

        return new GameObjectView(object, color);
    }
}
