package game;

import game.controllers.GameController;
import game.models.GameModel;
import game.views.GameView;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args)
    {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);

        JFrame frame = new JFrame("My Game");
        frame.setPreferredSize(new Dimension(550, 550));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
        controller.startGame();
    }
}

