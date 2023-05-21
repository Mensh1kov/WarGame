package game.controllers;

import game.models.components.PlayerControls;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter
{
    private PlayerControls playerControls;

    public GameKeyAdapter(PlayerControls playerControls)
    {
        this.playerControls = playerControls;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_W -> playerControls.setMoveUp(true);
            case KeyEvent.VK_A -> playerControls.setMoveLeft(true);
            case KeyEvent.VK_S -> playerControls.setMoveDown(true);
            case KeyEvent.VK_D -> playerControls.setMoveRight(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_W -> playerControls.setMoveUp(false);
            case KeyEvent.VK_A -> playerControls.setMoveLeft(false);
            case KeyEvent.VK_S -> playerControls.setMoveDown(false);
            case KeyEvent.VK_D -> playerControls.setMoveRight(false);
        }
    }
}
