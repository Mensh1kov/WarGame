package game.models;

import game.models.components.GameObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable
{
    private List<GameObject> objects = new ArrayList<>();
    private List<Integer> remove = new ArrayList<>();

    public List<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(List<GameObject> objects) {
        this.objects = objects;
    }

    public List<Integer> getRemove() {
        return remove;
    }

    public void setRemove(List<Integer> remove) {
        this.remove = remove;
    }

    public void addRemove(int id)
    {
        remove.add(id);
    }

    public void clearRemove()
    {
        remove.clear();
    }
}
