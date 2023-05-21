package game.models.components;

import game.models.Zombie;

import java.util.List;
import java.util.Random;

public class SpawnerZombies
{
    private Random rn = new Random();
    private List<Integer> hps = List.of(50, 100, 300, 500);
    private List<Integer> sizes = List.of(10, 20, 30, 40);
    private List<Integer> speeds = List.of(2, 3, 4);
    private int x;
    private int y;

    public SpawnerZombies(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Zombie getZombie()
    {
        int hp = hps.get(rn.nextInt(hps.size()));
        int size = sizes.get(rn.nextInt(sizes.size()));
        int speed = speeds.get(rn.nextInt(speeds.size()));

        return new Zombie(x, y, size, size, hp, speed, 0, 0);
    }
}
