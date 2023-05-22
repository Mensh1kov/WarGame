package game.models.components;

public class IdGenerator
{
    private static int nextId = 1;

    public static int generateId()
    {
        int id = nextId;
        nextId++;
        return id;
    }
}
