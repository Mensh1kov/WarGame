package game.models;

import game.models.components.GameObject;

import java.util.ArrayList;
import java.util.List;

public class CollisionHandler
{
    public static List<GameObject> checkCollisionObject(GameObject suspect, List<GameObject> objects)
    {
        List<GameObject> collisions = new ArrayList<>();

        for (GameObject object : objects)
        {
            // каким-то образом в objects попадают null
            if (object != suspect && checkCollision(suspect, object))
                collisions.add(object);
        }
        return collisions;
    }

    public static boolean checkCollision(GameObject objA, GameObject objB)
    {
        // пришлось добавить проверку на null, из-за проблемы, описанной в checkCollisionObject
        if (objA != null && objB != null)
            return objA.getHitbox().intersects(objB.getHitbox());
        return false;
    }
}
