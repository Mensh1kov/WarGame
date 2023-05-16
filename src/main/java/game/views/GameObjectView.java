package game.views;

import game.models.components.GameObject;
import javax.swing.*;
import java.awt.*;

public class GameObjectView extends JPanel
{
    protected GameObject object;
    protected Color color;

    public GameObjectView(GameObject object, Color color)
    {
        this.object = object;
        this.color = color;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(object.getX(), object.getY(), object.getWidth(), object.getHeight());
    }
}
