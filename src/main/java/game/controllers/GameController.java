package game.controllers;


import game.models.GameModel;
import game.models.components.GameObject;
import game.views.GameObjectView;
import game.views.GameView;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class GameController implements PropertyChangeListener {
    private GameModel model;
    private GameView view;
    private Map<GameObject, GameObjectView> gameObjectViewMap;

    public GameController(GameModel model, GameView view) {
        this.gameObjectViewMap = new HashMap<>();
        this.model = model;
        this.view = view;
        this.view.setFocusable(true);
        this.view.addKeyListener(new GameKeyAdapter(model));
        this.model.addPropertyChangeListener(this);
        this.gameObjectViewMap.put(model.gameObject, new GameObjectView(model.gameObject, Color.red));
        this.view.printObjectsViews(this.gameObjectViewMap.values().stream().toList());
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("gameObjectAdded")) {
            GameObject newGameObject = (GameObject) evt.getNewValue();
            GameObjectView newGameObjectView = new GameObjectView(newGameObject, Color.red);
            gameObjectViewMap.put(newGameObject, newGameObjectView);
        } else if (evt.getPropertyName().equals("gameObjectRemoved")) {
            GameObject removedGameObject = (GameObject) evt.getOldValue();
            gameObjectViewMap.remove(removedGameObject);
        } else if (evt.getPropertyName().equals("update")) {
            view.printObjectsViews(gameObjectViewMap.values().stream().toList());
        };
        }
}
