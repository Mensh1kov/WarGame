package game.controllers;

import game.models.GameModel;
import game.models.components.GameObject;
import game.models.components.Wall;
import game.views.GameObjectView;
import game.views.GameObjectViewFactory;
import game.views.GameView;
import javax.swing.*;
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
        timer = new Timer(16, e -> updateView());

        setupView();
        setupModel();
    }

    public void startGame()
    {
        timer.start();
        model.startGameLoop();
    }

    public void setupView()
    {
        view.setFocusable(true);
        view.addKeyListener(new GameKeyAdapter(model));
        view.addMouseListener(new GameMouseAdapter(model));
    }

    public void setupModel()
    {
        model.addPropertyChangeListener(this);
        setupGameWorld();
    }

    public void setupGameWorld()
    {
        model.addGameObject(new Wall(0, 0, 20, 500));
        model.addGameObject(new Wall(20, 0, 480, 20));
        model.addGameObject(new Wall(480, 20, 20, 480));
        model.addGameObject(new Wall(20, 480, 460, 20));
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
