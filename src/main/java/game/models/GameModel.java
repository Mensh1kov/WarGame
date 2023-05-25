package game.models;

import game.models.components.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameModel
{
    private final String SAVE_FILE_PATH = "src/main/resources/game_save.dat";
    protected final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() - 1);
    protected boolean running;
    protected final int FPS = 60; // Частота обновления в кадрах в секунду
    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected PlayerControls playerControls = new PlayerControls();
    protected GameManager manager;


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
        List<GameObject> collisions = CollisionHandler.checkCollisionObject(object, new ArrayList<>(manager.getObjects()));

        for (GameObject crossedObject : collisions)
        {
            if (crossedObject instanceof Wall && object instanceof Bullet)
            {
                removeGameObject(object);
            }
            else if (crossedObject instanceof Wall)
            {
                object.move(oldX, oldY);
            }
            else if (crossedObject instanceof Zombie && object instanceof Bullet)
            {
                ((Zombie) crossedObject).hit(((Bullet) object).getDamage());
                if (((Zombie) crossedObject).isDead()) removeGameObject(crossedObject);
                removeGameObject(object);
            }
            else if (crossedObject instanceof Zombie && object instanceof Zombie)
            {
                object.move(oldX, oldY);
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
        }
    }
    public void update()
    {
        Player player = manager.getPlayer();
        handlePlayers(playerControls, player);
        for (SpawnerZombies spawner : manager.getSpawners())
        {
            if (manager.getZombies().size() < 5)
            {
                Zombie zombie = spawner.getZombie();
                if (CollisionHandler.checkCollisionObject(zombie, manager.getObjects()).size() == 0)
                {
                    zombie.setTarget(player.getX(), player.getY());
                    addGameObject(zombie);
                }
            }
        }

        for (DynamicObject object : manager.getDynamicObjects())
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
        return manager.getPlayers()
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
            addGameObject(new Bullet(manager.getIdGenerator().generateId(), object.getId(), (int) object.getHitbox().getCenterX(), (int) object.getHitbox().getCenterY(), 10, 10, directionX, directionY));
        }
    }

    protected Player getNewPlayer(String name, IdGenerator generator)
    {
        return new Player(generator.generateId(), name, 300, 300, 1000, 20, 20);
    }

    public Player getPlayer()
    {
        return manager.getPlayer();
    }

    public int getFps()
    {
        return FPS;
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
        manager.addGameObject(object);
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, object);
    }

    public void addSpawner(SpawnerZombies spawner)
    {
        manager.addSpawner(spawner);
    }
    public void removeGameObject(GameObject object)
    {
        manager.removeGameObject(object);
        propertyChangeSupport.firePropertyChange("gameObjectRemoved", object.getId(), null);
    }

    public PlayerControls getPlayerControls()
    {
        return playerControls;
    }

    public void startGame()
    {
        loadGame();
        startGameLoop();
    }

    public void stopGame()
    {
        saveGame();
        stopGameLoop();
    }

    protected void handlePlayers(PlayerControls playerControls, Player player)
    {
        if (playerControls.isMoveUp()) movePlayerUp(player);
        if (playerControls.isMoveDown()) movePlayerDown(player);
        if (playerControls.isMoveLeft()) movePlayerLeft(player);
        if (playerControls.isMoveRight()) movePlayerRight(player);
        if (playerControls.isShoot()) shoot(player, playerControls.getX(), playerControls.getY());
    }

    protected void loadNewGame()
    {
        IdGenerator generator = new IdGenerator();
        Player player = getNewPlayer("Alex", generator);
        propertyChangeSupport.firePropertyChange("gameObjectAdded", null, player);
        manager = new GameManager(player, generator);
        setupGameWorld();
    }
    private void loadGame()
    {
        manager = GameSerializer.loadGame(SAVE_FILE_PATH);
        if (manager == null) loadNewGame();
        else manager.getObjects().forEach(obj -> propertyChangeSupport.firePropertyChange("gameObjectAdded", null, obj));
    }

    private void saveGame()
    {
        GameSerializer.saveGame(manager, SAVE_FILE_PATH);
    }

    protected void startGameLoop()
    {
        running = true;
        threadPool.submit(() -> {
            long targetTime = 1000 / FPS; // Желаемое время между обновлениями

            while (running)
            {
                long startTime = System.currentTimeMillis();

                update(); // Выполнение обновления модели

                long elapsedTime = System.currentTimeMillis() - startTime;
                long sleepTime = targetTime - elapsedTime;

                if (sleepTime > 0)
                {
                    try {
                        Thread.sleep(sleepTime); // Ожидание до следующего обновления
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void stopGameLoop()
    {
        running = false;
    }

    private void setupGameWorld()
    {
        Player player = getPlayer();
        IdGenerator generator = manager.getIdGenerator();
        addGameObject(new Wall(generator.generateId(),0, 0, 20, 500));
        addGameObject(new Wall(generator.generateId(),20, 0, 480, 20));
        addGameObject(new Wall(generator.generateId(), 480, 20, 20, 480));
        addGameObject(new Wall(generator.generateId(), 20, 480, 460, 20));
        addGameObject(new Wall(generator.generateId(), 80, 80,  340, 20));
        addGameObject(new Wall(generator.generateId(), 80, 380,  340, 20));

        addGameObject(new Zombie(generator.generateId(), 20, 20, 20, 20, 100, 2, player.getX(), player.getY()));
        addGameObject(new Zombie(generator.generateId(), 150, 150, 40, 40, 500, 2, player.getX(), player.getY()));
        addGameObject(new Zombie(generator.generateId(), 350, 150, 10, 10, 50, 3, player.getX(), player.getY()));
        addGameObject(new Zombie(generator.generateId(), 460, 20, 20, 20, 100, 2, player.getX(), player.getY()));

        addSpawner(new SpawnerZombies(100, 250, generator));
        addSpawner(new SpawnerZombies(400, 250, generator));
    }

}