package game.models;

import game.models.components.GameObject;
import java.util.List;

public class CollisionHandler {
    public static GameObject checkCollisionObject(GameObject suspect, List<GameObject> objects) {
        for (GameObject object : objects) {
            if (checkCollision(suspect, object)) return object;
        }
        return null;
    }

    public static boolean checkCollision(GameObject objA, GameObject objB) {
        // Проверка столкновения между objA и objB
        // Возвращаем true, если столкновение обнаружено, и false в противном случае
        return objA.getHitbox().intersects(objB.getHitbox());
    }
}
