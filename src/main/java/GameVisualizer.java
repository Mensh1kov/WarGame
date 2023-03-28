import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameVisualizer extends JPanel implements ActionListener, Visualizer {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int FPS = 60;
    private static final int FRAME_DELAY = 1000 / FPS;

    private java.util.List<JComponent> visualizationList = new ArrayList<>();
    private PlayerVisualization playerVisualization;
    private ZombieVisualization zombieVisualization;
    private ZombieVisualization zombieVisualization2;

    public GameVisualizer() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        Timer timer = new Timer(FRAME_DELAY, this);
//        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void update() {
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.playerVisualization.paintComponent(g);
        this.zombieVisualization.paintComponent(g);
        this.zombieVisualization2.paintComponent(g);
    }
    @Override
    public void addComponent(Personage personage, Personage zombie, Personage zombie2) {
        this.playerVisualization = new PlayerVisualization((Player) personage);
        this.zombieVisualization = new ZombieVisualization((Zombie) zombie);
        this.zombieVisualization2 = new ZombieVisualization((Zombie) zombie2);

    }
}
