package game.models;


import game.models.components.DynamicObject;
import game.models.components.GameObject;
import game.models.components.StaticObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public GameObject gameObject;

    public List<GameObject> objects = new ArrayList<>();
    public List<GameObject> dynamicObjects = new ArrayList<>();
    public List<GameObject> staticObjects = new ArrayList<>();
    public GameModel() {
        this.gameObject = new GameObject(0, 0, 50, 50);
        addGameObject(gameObject);
    }

    public void movePlayerLeft() {
        moveObject(gameObject,gameObject.getX() - 5, gameObject.getY());
    }

    public void movePlayerRight() {
        moveObject(gameObject,gameObject.getX() + 5, gameObject.getY());
    }

    public void movePlayerUp() {
        moveObject(gameObject,gameObject.getX(), gameObject.getY() - 5);
    }

    public void movePlayerDown() {
        moveObject(gameObject,gameObject.getX(), gameObject.getY() + 5);
    }

    private void moveObject(GameObject object, int x, int y) {
        int oldX = object.getX();
        int oldY = object.getY();
        object.move(x, y);
        if (CollisionHandler.checkCollisionObject(object, staticObjects) != null) object.move(oldX, oldY);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public void addGameObject(GameObject object)
    {
        objects.add(object);
        if (object instanceof StaticObject) staticObjects.add(object);
        else if (object instanceof DynamicObject) dynamicObjects.add(object);
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, object);
    }
}