package game.models.components;

import java.io.Serializable;

public class IdGenerator implements Serializable
{
    private long nextId = 1;
    public long generateId()
    {
        long id = nextId;
        nextId++;
        return id;
    }
}
