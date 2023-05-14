package game.models;


import game.models.components.GameObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameModel {
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public GameObject gameObject;
    public GameModel() {
        this.gameObject = new GameObject(0, 0, 50, 50);
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, gameObject);
    }

    public void movePlayerLeft() {
        gameObject.setX(gameObject.getX() - 5);
        propertyChangeSupport.firePropertyChange("update", null, null);
    }

    public void movePlayerRight() {
        gameObject.setX(gameObject.getX() + 5);
        propertyChangeSupport.firePropertyChange("update", null, null);
    }

    public void movePlayerUp() {
        gameObject.setY(gameObject.getY() - 5);
        propertyChangeSupport.firePropertyChange("update", null, null);
    }

    public void movePlayerDown() {
        gameObject.setY(gameObject.getY() + 5);
        propertyChangeSupport.firePropertyChange("update", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}