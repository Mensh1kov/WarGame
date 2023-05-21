package game.models;

import game.models.components.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel
{
    private boolean running;
    private Thread gameLoopThread;
    private final int FPS = 60; // Частота обновления в кадрах в секунду
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public Player gameObject;
    public PlayerControls playerControls = new PlayerControls();

    private List<GameObject> objects = new ArrayList<>();
    private List<DynamicObject> dynamicObjects = new ArrayList<>();
    private List<StaticObject> staticObjects = new ArrayList<>();

    private List<Zombie> zombies = new ArrayList<>();
    private List<SpawnerZombies> spawners = new ArrayList<>();
    public GameModel()
    {
//        zombies.removeIf(zombie -> {zombie.isDead()})
        // временный способ создания игрока
        this.gameObject = new Player(300, 300, 10000, 20, 20);
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
                ((Zombie) crossedObject).hit(((Bullet) object).getDamage());
                if (((Zombie) crossedObject).isDead()) removeGameObject(crossedObject);
                removeGameObject(object);
            }
            else if (crossedObject instanceof Player && object instanceof Zombie)
            {
                ((Player) crossedObject).hit(1);
                if (((Player) crossedObject).isDead()) System.out.println("You died!");
                object.move(oldX, oldY);
            }
            else if (crossedObject instanceof Bullet && object instanceof Bullet)
            {
                // ничего не делаем, т.е. пули пролетают друг через друга
            }
            else if (object instanceof Bullet || crossedObject instanceof Bullet)
            {
                // чтобы пули не застревали в игроке
            }
            else
            {
                object.move(oldX, oldY);
            }
        }
    }
    public void update()
    {
        handlePlayers();
        for (SpawnerZombies spawner : spawners)
        {
            if (zombies.size() < 5)
            {
                Zombie zombie = spawner.getZombie();
                if (CollisionHandler.checkCollisionObject(zombie, new ArrayList<>(objects)).size() == 0)
                {
                    zombie.setTarget(gameObject.getX(), gameObject.getY());
                    addGameObject(zombie);
                }
            }
        }

        for (DynamicObject object : new ArrayList<>(dynamicObjects))
        {
            // TODO иногда object равен null
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
        if (Double.isFinite(deltaX) && deltaX != 0.0 && Double.isFinite(deltaY) && deltaY != 0.0)
        {
            double len = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double directionX = deltaX / len;
            double directionY = deltaY / len;
            addGameObject(new Bullet((int) gameObject.getHitbox().getCenterX(), (int) gameObject.getHitbox().getCenterY(), 10, 10, directionX, directionY));
        }
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
        if (object instanceof Zombie) zombies.add((Zombie) object);
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, object);
    }

    public void addSpawner(SpawnerZombies spawner)
    {
        spawners.add(spawner);
    }
    public void removeGameObject(GameObject object)
    {
        objects.remove(object);
        if (object instanceof DynamicObject) dynamicObjects.remove((DynamicObject) object);
        else if (object instanceof StaticObject) staticObjects.remove((StaticObject) object);
        if (object instanceof Zombie) zombies.remove((Zombie) object);
        propertyChangeSupport.firePropertyChange("gameObjectRemoved", object, null);
    }

    public PlayerControls getPlayerControls()
    {
        return playerControls;
    }

    public void startGameLoop()
    {
        addGameObject(gameObject);
        running = true;
        gameLoopThread = new Thread(() -> {
            long targetTime = 1000 / FPS; // Желаемое время между обновлениями

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
        return FPS;
    }

    private void handlePlayers()
    {
        if (playerControls.isMoveUp()) movePlayerUp();
        if (playerControls.isMoveDown()) movePlayerDown();
        if (playerControls.isMoveLeft()) movePlayerLeft();
        if (playerControls.isMoveRight()) movePlayerRight();
        if (playerControls.isShoot()) shoot(playerControls.getX(), playerControls.getY());
    }
}