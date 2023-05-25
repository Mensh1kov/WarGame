package game.models.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements Serializable
{
    private IdGenerator generator;
    private List<GameObject> objects = new ArrayList<>();
    private List<DynamicObject> dynamicObjects = new ArrayList<>();
    private List<StaticObject> staticObjects = new ArrayList<>();
    private List<Zombie> zombies = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<SpawnerZombies> spawners = new ArrayList<>();
    private Player player;


    public GameManager(Player player, IdGenerator generator)
    {
        this.player = player;
        this.generator = generator;
        addGameObject(player);
    }

    public void addGameObject(GameObject object)
    {
        objects.add(object);
        if (object instanceof StaticObject) staticObjects.add((StaticObject) object);
        else if (object instanceof DynamicObject) dynamicObjects.add((DynamicObject) object);
        if (object instanceof Zombie) zombies.add((Zombie) object);
        if (object instanceof Player) players.add((Player) object);
    }

    public void removeGameObject(GameObject object)
    {
        objects.remove(object);
        if (object instanceof DynamicObject) dynamicObjects.remove((DynamicObject) object);
        else if (object instanceof StaticObject) staticObjects.remove((StaticObject) object);
        if (object instanceof Zombie) zombies.remove((Zombie) object);
        if (object instanceof Player) players.remove((Player) object);
    }

    public void addSpawner(SpawnerZombies spawner)
    {
        spawners.add(spawner);
    }

    public List<GameObject> getObjects()
    {
        return new ArrayList<>(objects);
    }

    public List<DynamicObject> getDynamicObjects()
    {
        return new ArrayList<>(dynamicObjects);
    }

    public List<StaticObject> getStaticObjects()
    {
        return new ArrayList<>(staticObjects);
    }

    public List<Zombie> getZombies()
    {
        return new ArrayList<>(zombies);
    }

    public List<Player> getPlayers()
    {
        return new ArrayList<>(players);
    }

    public List<SpawnerZombies> getSpawners()
    {
        return new ArrayList<>(spawners);
    }

    public Player getPlayer()
    {
        return player;
    }

    public IdGenerator getIdGenerator()
    {
        return generator;
    }
}
