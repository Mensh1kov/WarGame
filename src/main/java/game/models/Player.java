package game.models;

import game.models.components.GameObject;

public class Player extends GameObject
{
    private int hp;

    public Player(int x, int y, int hp, int width, int height)
    {
        super(x, y, width, height);
        this.hp = hp;
    }

    public int getHp()
    {
        return hp;
    }

    public void hit(int damage)
    {
        hp -= damage;
    }

    public boolean isDead()
    {
        return hp <= 0;
    }
}
