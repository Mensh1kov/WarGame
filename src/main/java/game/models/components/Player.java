package game.models.components;

public class Player extends GameObject
{
    private int hp;
    private String name;

    public Player(int id, String name, int x, int y, int hp, int width, int height)
    {
        super(id, x, y, width, height);
        this.name = name;
        this.hp = hp;
    }

    public int getHp()
    {
        return hp;
    }

    public String getName()
    {
        return name;
    }

    public void hit(int damage)
    {
        hp -= damage;
    }

    public void setHp(int hp)
    {
        this.hp = hp;
    }

    public boolean isDead()
    {
        return hp <= 0;
    }
}
