package game.views;

import game.models.components.Player;
import java.awt.*;

public class PlayerView extends GameObjectView
{
    public PlayerView(Player object, Color color)
    {
        super(object, color);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(((Player) object).getHp()), object.getX(), object.getY());
        g.drawString((((Player) object).getName()), object.getX(), object.getY() - 10);
    }
}
