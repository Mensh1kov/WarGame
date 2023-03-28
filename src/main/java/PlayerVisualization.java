import javax.swing.*;
import java.awt.*;

public class PlayerVisualization extends JPanel {
    private Player player;

    PlayerVisualization(Player player) {
        this.player = player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(player.getX().intValue(),player.getY().intValue(), 20, 20);
    }
}
