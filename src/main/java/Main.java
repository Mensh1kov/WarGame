import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("My GameLoop");
        GameKeyAdapter adapter = new GameKeyAdapter();
        GameVisualizer visualizer = new GameVisualizer();
        GameLoop gameLoop = new GameLoop(visualizer, adapter);
        gameLoop.start();
        frame.addKeyListener(adapter);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.add(visualizer);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}