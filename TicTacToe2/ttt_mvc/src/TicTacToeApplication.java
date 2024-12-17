import controller.TicTacToeControllerImpl;
import model.TicTacToeModelImpl;

/**
 * This class includes the main-Method.
 * From here we start the program.
 */
public class TicTacToeApplication {
    /**
     * main-Method for starting the application.
     * @param args no optional args required.
     */
    public static void main(String[] args) {
        TicTacToeModelImpl ticTacToeModel = new TicTacToeModelImpl();
        TicTacToeControllerImpl ticTacToeController = new TicTacToeControllerImpl(ticTacToeModel);
    }
}
