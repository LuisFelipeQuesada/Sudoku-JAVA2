package UI;

import game.Board;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Luis Felipe Quesada
 */
public class TextFieldFocusListener implements FocusListener {
    
    JTextField textField;
    Color color;
    String textFromCell, textName;
    int index, data;
    Board board;
    public static boolean res = false;
    
    public TextFieldFocusListener(Board b) {
        board = b;
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        textField = ((JTextField) e.getSource());
        color = textField.getBackground();
        textField.setBackground(Color.decode("#FFFF99"));
        
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(textField.getText().equals("")) {
            // Si el usuario no escribe nada, la celda se mantiene de color blanco
            textField.setBackground(Color.decode("#FFFFFF"));
        }
        else {
            // Si el número ingresado por el ususario el válido, el color de la celda y el número
            // cambian a gris y negro, respectivamente
            if(board.getIsValid()) {
                textField.setBackground(Color.decode("#F0F0F0"));
                textField.setForeground(Color.decode("#000000"));
            }                
        }
    }
}
