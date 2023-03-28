import javax.swing.*;

public class Player extends AbstractPersonage {
    private Double step;
    public Player(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.speed = 10D;
        this.step = 5D;
    }

    public void moveUp() {
        move(0D, -step);
    }
    public void moveDown() {
        move(0D, step);
    }
    public void moveLeft() {
        move(-step, 0D);
    }
    public void moveRight() {
        move(step, 0D);
    }
}
