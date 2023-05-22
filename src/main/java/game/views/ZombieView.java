package game.views;

import game.models.components.Zombie;
import java.awt.*;

public class ZombieView extends GameObjectView
{
    public ZombieView(Zombie object, Color color)
    {
        super(object, color);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(((Zombie) object).getHp()), object.getX(), object.getY());
    }
}
