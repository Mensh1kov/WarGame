import static java.lang.Math.sqrt;

public class Zombie extends AbstractPersonage {
    public Zombie(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.speed = 30D;
    }

    public void moveToTarget(Double x, Double y, Double elapsedTimed) {
        Double dx = x - this.x;
        Double dy = y - this.y;
        Double distance = sqrt(dx * dx + dy * dy);
        System.out.println((dx / distance) * speed * elapsedTimed / 100000000);
        move((dx / distance) * speed * elapsedTimed, (dy / distance) * speed * elapsedTimed);
    }
}
