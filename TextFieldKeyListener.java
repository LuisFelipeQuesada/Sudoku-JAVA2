package UI;

import UI.MainWindow.WinningWindow;
import game.Board;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Luis Felipe Quesada
 */
public class TextFieldKeyListener implements KeyListener {
    
    Board board;
    int row, col, index, numberEntered;
    JTextField origin;
    MainWindow main;
    Object[] options = {"Jugar de nuevo", "No"};
    
    public TextFieldKeyListener(Board b, MainWindow win) {
        board = b;
        main = win;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        getUserInput(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    private void getUserInput(KeyEvent e) {
        try {
            numberEntered = Integer.valueOf(String.valueOf(e.getKeyChar()));
            origin = (JTextField) e.getComponent();

            // Basado en el nombre del TextField se obtienen las filas y columnas
            String originName = origin.getName();
            index = originName.indexOf('.');
            row = Integer.valueOf(originName.substring(0, index));
            col = Integer.valueOf(originName.substring(index + 1, originName.length()));

            if(board.validateInput(row, col, numberEntered)) {
                origin.setForeground(Color.decode("#000000"));
                if(board.isWon()) {
                    WinningWindow win = main.new WinningWindow();
                }
            }
        } catch(NumberFormatException exception) {}
    }
}
