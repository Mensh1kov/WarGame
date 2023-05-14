package game.controllers;


import game.models.GameModel;
import game.models.components.GameObject;
import game.models.components.Wall;
import game.views.GameObjectView;
import game.views.GameObjectViewFactory;
import game.views.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class GameController implements PropertyChangeListener
{
    private Timer timer;
    private GameModel model;
    private GameView view;
    private Map<GameObject, GameObjectView> gameObjectViewMap;

    public GameController(GameModel model, GameView view)
    {
        this.gameObjectViewMap = new HashMap<>();
        this.model = model;
        this.view = view;
        this.view.setFocusable(true);
        this.view.addKeyListener(new GameKeyAdapter(model));
        this.model.addPropertyChangeListener(this);
        this.gameObjectViewMap.put(model.gameObject, GameObjectViewFactory.getGameObjectView(model.gameObject));
        model.addGameObject(new Wall(50, 59, 20, 100));
        timer = new Timer(16, e -> updateView());
        timer.start();
    }

    public void updateView()
    {
        view.printObjectsViews(gameObjectViewMap.values().stream().toList());
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals("gameObjectAdded"))
        {
            GameObject newGameObject = (GameObject) evt.getNewValue();
            gameObjectViewMap.put(newGameObject, GameObjectViewFactory.getGameObjectView(newGameObject));
        }
        else if (evt.getPropertyName().equals("gameObjectRemoved"))
        {
            gameObjectViewMap.remove((GameObject) evt.getOldValue());
        }
        else if (evt.getPropertyName().equals("update"))
        {
            updateView();
        }
    }
}
