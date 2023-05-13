package game;


import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<ModelObserver> observers = new ArrayList<>();
    GameObject gameObject;
    public GameModel() {
        this.gameObject = new GameObject(0, 0, 50, 50);
    }

    public void movePlayerLeft() {
        gameObject.setX(gameObject.getX() - 5);
        notifyObservers();
    }

    public void movePlayerRight() {
        gameObject.setX(gameObject.getX() + 5);
        notifyObservers();
    }

    public void movePlayerUp() {
        System.out.println(123);
        gameObject.setY(gameObject.getY() - 5);
        notifyObservers();
    }

    public void movePlayerDown() {
        gameObject.setY(gameObject.getY() + 5);
        notifyObservers();
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ModelObserver observer : observers) {
            observer.update();
        }
    }
}