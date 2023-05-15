package game.models;

import game.models.components.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private boolean running;
    private Thread gameLoopThread;
    private int fps = 60; // Частота обновления в кадрах в секунду
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public GameObject gameObject;

    public List<GameObject> objects = new ArrayList<>();
    public List<DynamicObject> dynamicObjects = new ArrayList<>();
    public List<StaticObject> staticObjects = new ArrayList<>();
    public GameModel()
    {
        // временный способ создания игрока
        this.gameObject = new GameObject(300, 300, 20, 20);
    }

    public void movePlayerLeft()
    {
        moveObject(gameObject,gameObject.getX() - 3, gameObject.getY());
    }

    public void movePlayerRight()
    {
        moveObject(gameObject,gameObject.getX() + 3, gameObject.getY());
    }

    public void movePlayerUp()
    {
        moveObject(gameObject,gameObject.getX(), gameObject.getY() - 3);
    }

    public void movePlayerDown()
    {
        moveObject(gameObject,gameObject.getX(), gameObject.getY() + 3);
    }

    private void moveObject(GameObject object, int x, int y)
    {
        int oldX = object.getX();
        int oldY = object.getY();
        object.move(x, y);
        List<GameObject> collisions = CollisionHandler.checkCollisionObject(object, new ArrayList<>(objects));

        for (GameObject crossedObject : collisions)
        {
            if (crossedObject instanceof Wall && object instanceof Bullet)
            {
                removeGameObject(object);
            } else if (crossedObject instanceof Zombie && object instanceof Bullet)
            {
                removeGameObject(crossedObject);
                removeGameObject(object);
            }
            else if (crossedObject instanceof Bullet && object instanceof Bullet)
            {
                // пока ничего не делать
            }
            else
            {
                object.move(oldX, oldY);
            }
        }
    }

    public void update()
    {
        for (DynamicObject object : new ArrayList<>(dynamicObjects))
        {
            if (object instanceof Zombie)
            {
                ((Zombie) object).setTarget(gameObject.getX(), gameObject.getY());
            }
            moveObject(object, object.getNewX(), object.getNewY());
        }
    }
    public void shoot(int targetX, int targetY)
    {
        double deltaX = targetX - gameObject.getX();
        double deltaY = targetY - gameObject.getY();
        double len = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double directionX = deltaX / len;
        double directionY = deltaY / len;
        int x = gameObject.getX() + (int) ((gameObject.getWidth() + 5) * directionX);
        int y = gameObject.getY() + (int) ((gameObject.getHeight() + 5) * directionY);
        addGameObject(new Bullet(x, y, 10, 10, directionX, directionY));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public void addGameObject(GameObject object)
    {
        objects.add(object);
        if (object instanceof StaticObject) staticObjects.add((StaticObject) object);
        else if (object instanceof DynamicObject) dynamicObjects.add((DynamicObject) object);
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, object);
    }

    public void removeGameObject(GameObject object)
    {
        objects.remove(object);
        if (object instanceof DynamicObject) dynamicObjects.remove((DynamicObject) object);
        else if (object instanceof StaticObject) staticObjects.remove((StaticObject) object);
        propertyChangeSupport.firePropertyChange("gameObjectRemoved", object, null);
    }

    public void startGameLoop()
    {
        addGameObject(gameObject);
        running = true;
        gameLoopThread = new Thread(() -> {
            long targetTime = 1000 / fps; // Желаемое время между обновлениями

            while (running)
            {
                long startTime = System.currentTimeMillis();

                update(); // Выполнение обновления модели

                long elapsedTime = System.currentTimeMillis() - startTime;
                long sleepTime = targetTime - elapsedTime;

                if (sleepTime > 0)
                {
                    try
                    {
                        Thread.sleep(sleepTime); // Ожидание до следующего обновления
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        gameLoopThread.start();
    }

    public void stopGameLoop()
    {
        running = false;

        try
        {
            gameLoopThread.join(); // Ожидание завершения потока игрового цикла
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public int getFps() {
        return fps;
    }
}