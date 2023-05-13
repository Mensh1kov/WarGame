package game;


import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel {
    private List<GameObjectView> objects = new ArrayList<>();
    public GameView() {
    }

    public void addGameObject(GameObjectView gameObjectView) {
        objects.add(gameObjectView);
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GameObjectView object : objects) {
            object.paintComponent(g);
        }
    }
}

