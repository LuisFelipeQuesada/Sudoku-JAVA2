package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Files.ReadXml;
import game.Board;
import java.util.ArrayList;
import javax.swing.border.EtchedBorder;
/**
 *
 * @author Luis Felipe Quesada
 */
public class MainWindow {
    
    JFrame window = null;
    JPanel windowPanel, panelOptions, boardPanel, boardPanelFooter = null;
    JButton buttonOption, buttonBack, buttonClose = null;
    
    // Bloques 3x3 para las submatrices
    ArrayList<JPanel> blocksList = null;
    
    Board board = null;
            
    public MainWindow() {
        blocksList = new ArrayList();
        window = new JFrame("Sudoku");
        window.setSize(530, 530);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        boardPanelFooter = new JPanel();
        boardPanelFooter.setLayout(new FlowLayout(FlowLayout.RIGHT));
        boardPanelFooter.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        createCloseBackButtons();
        
        JPanel panelPrin = new JPanel(new BorderLayout());
        windowPanel = new JPanel(new CardLayout());
        windowPanel.add("panelOptions", createPanelForOptions());
        windowPanel.add("boardPanel", createBoardPanel());
        
        panelPrin.add(windowPanel, BorderLayout.CENTER);
        panelPrin.add(boardPanelFooter, BorderLayout.SOUTH);
        
        window.setLayout(new BorderLayout());
        window.add(panelPrin, BorderLayout.CENTER);
    }
    
    private void createCloseBackButtons() {
        buttonClose = new JButton("Salir");
        buttonClose.addActionListener(new ButtonCloseBackListener());
        buttonClose.setActionCommand("close");
        
        buttonBack = new JButton("Atrás");
        buttonBack.setEnabled(false);
        buttonBack.addActionListener(new ButtonCloseBackListener());
        buttonBack.setActionCommand("back");
        
        boardPanelFooter.add(buttonClose);
        boardPanelFooter.add(buttonBack);
    }
    
    private JPanel createPanelForOptions() {
        panelOptions = new JPanel();
        GridBagLayout gridbaglayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panelOptions.setLayout(gridbaglayout);
        panelOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        
        addItemToGrid(panelOptions, new JLabel("Choose one of the following levels of difficulty"), constraints, 1, 1, 2, 1, 150, 20, new Insets(50, 25, 0, 1), GridBagConstraints.NONE, GridBagConstraints.PAGE_START);
        addItemToGrid(panelOptions, createOptions(String.valueOf(0), "Easy"), constraints, 1, 1, 1, 1, 70, 50, new Insets(110, 1, 1, 1), constraints.NONE, GridBagConstraints.PAGE_START);
        addItemToGrid(panelOptions, createOptions(String.valueOf(1), "Medium"), constraints, 1, 1, 1, 1, 50, 50, new Insets(20, 1, 1, 1), constraints.NONE, GridBagConstraints.CENTER);
        addItemToGrid(panelOptions, createOptions(String.valueOf(2), "Hard"), constraints, 1, 1, 1, 1, 72, 50, new Insets(0, 1, 90, 1), constraints.NONE, GridBagConstraints.PAGE_END);
        
        return panelOptions;
    }
    
    private void addItemToGrid(JPanel parent, JComponent component, GridBagConstraints constraints, int x, int y, int width, int height, int ipadX, int ipadY, Insets insets, int fill, int align) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.ipadx = ipadX;
        constraints.ipady = ipadY;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.fill = fill;
        constraints.insets = insets;
        constraints.anchor = align;
        parent.add(component, constraints);
    }
    
    private JPanel createBoardPanel() {
        boardPanel = new JPanel(new GridLayout(3, 3));     
        return boardPanel;
    }
    
    private JButton createOptions(String num, String name) {
        buttonOption = new JButton(name);
        buttonOption.setName(num);
        
        // "Listeners" y acciones de cada boton
        buttonOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton src = (JButton)e.getSource();
                JTextField cells[][] = null;
                ReadXml xml = null;
                
                int level = Integer.valueOf(src.getName());
                
                blocksList.clear();
                
                switch(level) {
                    case 0:
                        xml = new ReadXml(0);
                        board = new Board(xml.createMatrix(), 0);
                        break;
                    case 1:
                        xml = new ReadXml(1);
                        board = new Board(xml.createMatrix(), 1);
                        break;
                    case 2:
                        xml = new ReadXml(2);
                        board = new Board(xml.createMatrix(), 2);
                        break;
                }
                cells = createBoardCells();
                fillBoardCells(cells, board.getUserBoard());
                setBlocks(boardPanel);
                addBoardCells(blocksList, cells);
                
                buttonBack.setEnabled(true);
                
                CardLayout cardLayout = (CardLayout) windowPanel.getLayout();
                cardLayout.show(windowPanel, "boardPanel");
            }
        });
        return buttonOption;
    }
    
    private JTextField[][] createBoardCells() {
        JTextField[][] mat = new JTextField[9][9];
        JTextField text;
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                text = new JTextField(1);
                text.setName(String.valueOf(row) + "." + String.valueOf(col));
                text.setBorder(BorderFactory.createLineBorder(Color.decode("#6699CC"), 1));
                text.setHorizontalAlignment(JTextField.CENTER);
                text.setFocusTraversalKeysEnabled(false);
                
                // Agregar tipo de Font al TextField
                Font f = new Font("SansSerif", Font.ROMAN_BASELINE, 24);
                text.setFont(f);
                
                text.addKeyListener(new TextFieldKeyListener(board, this));
                text.addFocusListener(new TextFieldFocusListener(board));
                
                // Agregar textfield a la matriz
                mat[row][col] = text;
            }
            text = new JTextField(1);
        }
        return mat;
    }
    
    private void fillBoardCells(JTextField[][] cellsMatrix, int[][] data) {
        JTextField matrix[][] = cellsMatrix;
        for(int row = 0; row < data.length; row++) {
            for(int col = 0; col < data[row].length; col++) {
                if(data[row][col] == 0) {
                    matrix[row][col].setText("");
                    matrix[row][col].setFocusable(true);
                    Font f = new Font("SansSerif", Font.BOLD, 24);
                    matrix[row][col].setFont(f);
                }
                else {
                    matrix[row][col].setText(String.valueOf(data[row][col]));
                    matrix[row][col].setEditable(false);
                    matrix[row][col].setBackground(Color.decode("#F0F0F0"));
                    matrix[row][col].setFocusable(false);
                }
            }
        }
    }
    
    private void setBlocks(JPanel parent) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                parent.add(createBlock());
            }
        }
    }
    
    // Creates the panels that serve a blocks
    private JPanel createBlock() {
        JPanel blockPanel = new JPanel();
        GridLayout blockLayout = new GridLayout(3, 3);
        blockPanel.setLayout(blockLayout);
        blockPanel.setSize(150, 150);
        blockPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#6699CC"), 2));
        blocksList.add(blockPanel);
        return blockPanel;
    }
    
    // Creates the cells and adds them to the parent
    private void addBoardCells(ArrayList<JPanel> parentsList, JTextField[][] cellsMatrix) {
        int parentIndex = 0;
        int counter = 0;
        for(int row = 0; parentIndex < 9; row++) {
            // Se envia la siguiente fila de la matrix
            addRow(parentsList, cellsMatrix[row], parentIndex);
            counter += 1;
            if(counter >= 3) {
                parentIndex += 3;
                counter = 0;
            }
        }
    }
    
    // Adds a row to the correspondent parent
    private void addRow(ArrayList<JPanel> parentsList, JTextField[] row, int index) {
        int parentIndex = index;
        for(int col = 0; col < row.length; col++) {
            parentsList.get(parentIndex).add(row[col]);
            if((col == 2) || (col == 5)) {
                parentIndex += 1;
            }
        }
    }
    
    public class WinningWindow extends JFrame {
        
        public JFrame frame;
        
        public WinningWindow() {
            frame = new JFrame("Felicidades");
            frame.setSize(380, 75);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            frame.add(initGUI());
            frame.show();
            frame.pack();
        }
        
        private JPanel initGUI() {
            JPanel panelPrin = new JPanel(new GridLayout(2, 1));
            
            JPanel panelMsj = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel msj = new JLabel("Felicidades!!!, has ganado. ¿Deseas seguir jugando?");
            panelMsj.add(msj);
            
            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
            
            JButton playAgain = new JButton("Seguir jugando");
            playAgain.addActionListener(new ButtonCloseBackListener());
            playAgain.setActionCommand("back");
            playAgain.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });
            
            JButton exit = new JButton("Salir");
            exit.addActionListener(new ButtonCloseBackListener());
            
            exit.setActionCommand("close");
            
            panelButtons.add(playAgain);
            panelButtons.add(exit);
            
            panelPrin.add(panelMsj);
            panelPrin.add(panelButtons);
            
            return panelPrin;
        }
        
        public void closeWindow() {
            frame.dispose();
        }
    }
    
    public class ButtonCloseBackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if("close".equals(e.getActionCommand())) {
                window.dispose();
                System.exit(0);
            }
            else {
                CardLayout cardLayout = (CardLayout) windowPanel.getLayout();
                cardLayout.show(windowPanel, "panelOptions");
                boardPanel.removeAll();
                buttonBack.setEnabled(false);
            }
        }
    }
}
