package game.models;

import game.models.components.GameObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable
{
    private List<GameObject> objects = new ArrayList<>();
    private List<Long> remove = new ArrayList<>();

    public List<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(List<GameObject> objects) {
        this.objects = objects;
    }

    public List<Long> getRemove() {
        return remove;
    }

    public void setRemove(List<Long> remove) {
        this.remove = remove;
    }

    public void addRemove(long id)
    {
        remove.add(id);
    }

    public void clearRemove()
    {
        remove.clear();
    }
}
