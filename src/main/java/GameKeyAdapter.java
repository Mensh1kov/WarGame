import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class GameKeyAdapter extends KeyAdapter implements ActionListener, Controller {
    private Boolean W_PRESSED = false;
    private Boolean A_PRESSED = false;
    private Boolean S_PRESSED = false;
    private Boolean D_PRESSED = false;
    private Queue<ControllerEvent> queue = new LinkedList<>();

    public GameKeyAdapter() {
        Timer timer = new Timer(1000 / 60 , this);
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> W_PRESSED = true;
            case KeyEvent.VK_A -> A_PRESSED = true;
            case KeyEvent.VK_S -> S_PRESSED = true;
            case KeyEvent.VK_D -> D_PRESSED = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> W_PRESSED = false;
            case KeyEvent.VK_A -> A_PRESSED = false;
            case KeyEvent.VK_S -> S_PRESSED = false;
            case KeyEvent.VK_D -> D_PRESSED = false;
        }
    }

    public void update() {
        if (queue.size() <= 4) {
            if (W_PRESSED) queue.add(ControllerEvent.PLAYER_UP);
            if (A_PRESSED) queue.add(ControllerEvent.PLAYER_LEFT);
            if (S_PRESSED) queue.add(ControllerEvent.PLAYER_DOWN);
            if (D_PRESSED) queue.add(ControllerEvent.PLAYER_RIGHT);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }

    @Override
    public ControllerEvent getEvent() {
        return (queue.isEmpty()) ? ControllerEvent.NOTHING : queue.remove();
    }
}
