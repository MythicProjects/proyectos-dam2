package view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;

import contract.Direction;
import contract.IPresenter;

final class KeyHandler extends KeyAdapter implements FocusListener {
    private final IPresenter presenter;
    private final Deque<Direction> held = new ArrayDeque<>();

    KeyHandler(IPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Direction direction = toDirection(e.getKeyCode());
        if (direction == null || this.held.contains(direction)) {
            return;                     // not an arrow, or the keyboard's auto-repeat
        }
        this.held.push(direction);
        report();
    }

    @Override public void keyReleased(KeyEvent e){
        Direction direction = toDirection(e.getKeyCode());
        if (direction != null && this.held.remove(direction)){
            report();
        }
    }

    @Override public void focusGained(FocusEvent e) {}

    @Override 
    public void focusLost(FocusEvent e){
        if (!this.held.isEmpty()) { 
            this.held.clear(); 
            report();
        }
    }

    private void report(){
        if(this.held.isEmpty()) { this.presenter.stopPlayer();}
        else { this.presenter.movePlayer(this.held.peek()); }
    }

    private static Direction toDirection(int keyCode){
        return switch (keyCode){
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            default -> null;
        };
    }
}
