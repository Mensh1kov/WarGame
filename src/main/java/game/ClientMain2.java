package game;

import game.controllers.ClientController;
import game.models.ClientGameModel;
import game.views.GameView;
import javax.swing.*;
import java.awt.*;

public class ClientMain2 {
    public static void main(String[] args)
    {
        ClientGameModel model = new ClientGameModel("Boba");
        GameView view = new GameView();
        ClientController controller = new ClientController(model, view);

        JFrame frame = new JFrame("War Game Client 2");
        frame.setPreferredSize(new Dimension(550, 550));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
        controller.startGame();
    }
}
