import javax.swing.*;
import java.awt.*;

public class ZombieVisualization extends JPanel {
    private Zombie zombie;

    ZombieVisualization(Zombie zombie) {
        this.zombie = zombie;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(zombie.getX().intValue(), zombie.getY().intValue(), 20, 20);
    }
}
