package game.models.components;

public class Bullet extends DynamicObject
{
    private int damage;

    public Bullet(int x, int y, int damage, int speed, double directionX, double directionY)
    {
        super(x, y, 5, 5, speed, directionX, directionY);
        this.damage = damage;
    }
}
