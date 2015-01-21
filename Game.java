package game;
import UI.*;

/**
 *
 * @author Luis Felipe Quesada
 */
public class Game {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow window = new MainWindow();
            }
        });
    }
}
