package game.models;

import game.models.components.GameObject;
import game.models.components.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSave implements Serializable
{
    private Player player;
    public List<GameObject> objects = new ArrayList<>();

    public GameSave(Player player)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }

    public List<GameObject> getObjects()
    {
        return objects;
    }

    public void setObjects(List<GameObject> objects)
    {
        this.objects = objects;
    }
}
