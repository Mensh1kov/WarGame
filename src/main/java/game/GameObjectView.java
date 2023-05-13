package game;

import javax.swing.*;
import java.awt.*;

public class GameObjectView extends JPanel {
    private GameObject object;
    private Color color;

    public GameObjectView(GameObject object, Color color) {
        this.object = object;
        this.color = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(object.getX(), object.getY(), object.getWidth(), object.getHeight());
    }
}
