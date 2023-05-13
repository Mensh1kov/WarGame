package game;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameController implements ModelObserver {
    private GameModel model;
    private GameView view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.view.setFocusable(true);
        this.view.addKeyListener(new GameKeyAdapter(model));
        this.model.addObserver(this);
        this.view.addGameObject(new GameObjectView(model.gameObject, Color.red));
    }

    @Override
    public void update() {
        this.view.repaint();
    }
}
