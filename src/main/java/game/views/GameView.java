package game.views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel
{
    private List<GameObjectView> objectsViews = new ArrayList<>();
    public GameView() {}

    public void printObjectsViews(List<GameObjectView> objectsViews)
    {
        this.objectsViews = objectsViews;
        repaint();
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        objectsViews.forEach(object -> object.paintComponent(g));
    }
}

