package game.models;

import game.models.components.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel
{
    protected boolean running;
    protected Thread gameLoopThread;
    protected final int FPS = 60; // Частота обновления в кадрах в секунду
    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public Player player;
    protected PlayerControls playerControls = new PlayerControls();

    protected List<GameObject> objects = new ArrayList<>();
    protected List<DynamicObject> dynamicObjects = new ArrayList<>();
    protected List<StaticObject> staticObjects = new ArrayList<>();

    protected List<Zombie> zombies = new ArrayList<>();
    protected List<Player> players = new ArrayList<>();
    protected List<SpawnerZombies> spawners = new ArrayList<>();

    public GameModel()
    {
        // zombies.removeIf(zombie -> {zombie.isDead()})
        // временный способ создания игрока
        this.player = getPlayer("Alex");
    }

    public void movePlayerLeft(Player player)
    {
        moveObject(player,player.getX() - 3, player.getY());
    }

    public void movePlayerRight(Player player)
    {
        moveObject(player,player.getX() + 3, player.getY());
    }

    public void movePlayerUp(Player player)
    {
        moveObject(player, player.getX(), player.getY() - 3);
    }

    public void movePlayerDown(Player player)
    {
        moveObject(player, player.getX(), player.getY() + 3);
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
            }
            else if (crossedObject instanceof Zombie && object instanceof Bullet)
            {
                ((Zombie) crossedObject).hit(((Bullet) object).getDamage());
                if (((Zombie) crossedObject).isDead()) removeGameObject(crossedObject);
                removeGameObject(object);
            }
            else if (crossedObject instanceof Player && object instanceof Bullet)
            {
                if (crossedObject.getId() != ((Bullet) object).getSenderId())
                {
                    ((Player) crossedObject).hit(((Bullet) object).getDamage());
                    if (((Player) crossedObject).isDead())
                    {
                        System.out.println("%s died!".formatted(((Player) crossedObject).getName()));
                        ((Player) crossedObject).setHp(1000);
                    }
                    removeGameObject(object);
                }
            }
            else if (crossedObject instanceof Bullet && object instanceof Player)
            {

            }
            else if (crossedObject instanceof Bullet && object instanceof Zombie)
            {

            }
            else if (crossedObject instanceof Player && object instanceof Zombie)
            {
                ((Player) crossedObject).hit(1);
                if (((Player) crossedObject).isDead())
                {
                    System.out.println("%s died!".formatted(((Player) crossedObject).getName()));
                    ((Player) crossedObject).setHp(1000);
                }
                object.move(oldX, oldY);
            }
            else if (crossedObject instanceof Bullet && object instanceof Bullet)
            {
                // ничего не делаем, т.е. пули пролетают друг через друга
            }
            else if (crossedObject instanceof Player && object instanceof Player)
            {

            }
            else
            {
                object.move(oldX, oldY);
            }
        }
    }
    public void update()
    {
        handlePlayers(playerControls, player);
        for (SpawnerZombies spawner : spawners)
        {
            if (zombies.size() < 5)
            {
                Zombie zombie = spawner.getZombie();
                if (CollisionHandler.checkCollisionObject(zombie, new ArrayList<>(objects)).size() == 0)
                {
                    zombie.setTarget(player.getX(), player.getY());
                    addGameObject(zombie);
                }
            }
        }

        for (DynamicObject object : new ArrayList<>(dynamicObjects))
        {
            // TODO иногда object равен null
            if (object instanceof Zombie)
            {
                Player target = getTargetForZombie(object.getX(), object.getY());
                ((Zombie) object).setTarget(target.getX(), target.getY());
            }
            moveObject(object, object.getNewX(), object.getNewY());
        }
    }


    // зомби выбирает цель
    public Player getTargetForZombie(int x, int y)
    {
        return players
                .stream()
                .min((o1, o2) -> getDistance(o1.getX() - x, o1.getY() - y) - getDistance(o2.getX() - x, o2.getY() - y))
                .get(); // возможны ошибки
    }

    public int getDistance(int deltaX, int deltaY)
    {
        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    public void shoot(GameObject object, int targetX, int targetY)
    {
        double deltaX = targetX - object.getX();
        double deltaY = targetY - object.getY();
        if (Double.isFinite(deltaX) && deltaX != 0.0 && Double.isFinite(deltaY) && deltaY != 0.0)
        {
            double len = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double directionX = deltaX / len;
            double directionY = deltaY / len;
            addGameObject(new Bullet(IdGenerator.generateId(), object.getId(), (int) object.getHitbox().getCenterX(), (int) object.getHitbox().getCenterY(), 10, 10, directionX, directionY));
        }
    }

    protected Player getPlayer(String name)
    {
        return new Player(IdGenerator.generateId(), name, 300, 300, 1000, 20, 20);
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
        if (object instanceof Player) players.add((Player) object);
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
        if (object instanceof Player) players.remove((Player) object);
        propertyChangeSupport.firePropertyChange("gameObjectRemoved", object.getId(), null);
    }

    public PlayerControls getPlayerControls()
    {
        return playerControls;
    }

    public void startGameLoop()
    {
        addGameObject(player);
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

    public int getFps()
    {
        return FPS;
    }

    protected void handlePlayers(PlayerControls playerControls, Player player)
    {
        if (playerControls.isMoveUp()) movePlayerUp(player);
        if (playerControls.isMoveDown()) movePlayerDown(player);
        if (playerControls.isMoveLeft()) movePlayerLeft(player);
        if (playerControls.isMoveRight()) movePlayerRight(player);
        if (playerControls.isShoot()) shoot(player, playerControls.getX(), playerControls.getY());
    }
}