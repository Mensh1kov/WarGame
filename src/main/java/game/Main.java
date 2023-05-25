package game;

import game.controllers.GameController;
import game.models.GameModel;
import game.models.ServerGameModel;
import game.views.GameView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args)
    {
        GameModel model = new GameModel();
        GameView view = new GameView();
        GameController controller = new GameController(model, view);

        JFrame frame = new JFrame("War Game Server");
        frame.setPreferredSize(new Dimension(550, 550));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                controller.stopGame();
            }
        });
        controller.startGame();
    }
}

