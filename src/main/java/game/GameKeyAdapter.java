package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter {
    private GameModel model;

    public GameKeyAdapter(GameModel model) {
        this.model = model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> model.movePlayerUp();
            case KeyEvent.VK_A -> model.movePlayerLeft();
            case KeyEvent.VK_S -> model.movePlayerDown();
            case KeyEvent.VK_D -> model.movePlayerRight();
        }
    }
}
