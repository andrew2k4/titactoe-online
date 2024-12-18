package controller;

import model.TicTacToeModelInterface;
import org.json.simple.parser.ParseException;
import server.controller.FetchGameStatut;
import server.controller.SubmitMove;
import server.model.StatutGame;
import view.TicTacToeView;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is part of our controller.
 * It implements the right interface.
 * It serves as the controller in the MVC pattern.
 * Facilitating the interaction between view and model.
 */
public class TicTacToeControllerImpl implements TicTacToeControllerInterface {
    private final TicTacToeView view;
    private TicTacToeModelInterface model;
    private final byte player = 22;
    private StatutGame statutGame = new StatutGame();

    /**
     * Constructor, also initialize view and model.
     *
     * @param model game model with respect to an MVC pattern.
     */
    public TicTacToeControllerImpl(TicTacToeModelInterface model) {
        this.model = model;
        this.view = new TicTacToeView(model, this);

        updateViewObserver();
    }

    /**
     * Called when a mouse press event occurs in the game.
     * This method handles the user interaction to update the game state.
     * It checks if the game already ended or if the position is already occupied.
     * If not, the position will be set.
     *
     * @param c the column index of the position where the mouse is pressed.
     * @param r the row index of the position where the mouse is pressed
     */
    @Override
    public void whenMousePressed(byte c, byte r) throws IOException, ParseException {
        if (c >= model.getColumns()) {
            return;
        }
        if (r >= model.getRows()) {
            return;
        }
        if (model.ended()) {
            return;
        }
        if (model.getAtPosition(c, r) != model.getPlayerNone()) {
            return;
        }
        if (statutGame.getCurrentPlayer() != player) {
            return;
        }

        // Envoi du mouvement au serveur
        SubmitMove.execute(c, r, player);

        // Mise à jour du modèle local
        model = model.setAtPosition(c, r, player);
        view.setGame(model);
        view.repaint();
    }

    /**
     * Méthode qui met à jour la vue en fonction de l'état du jeu
     * récupéré périodiquement du serveur.
     */
    public void updateViewObserver() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {

                    statutGame = FetchGameStatut.get();


                    for (byte i = 0; i < statutGame.getBoard().length; i++) {
                        for (byte j = 0; j < statutGame.getBoard()[i].length; j++) {
                            int value = statutGame.getBoard()[i][j];
                            if (value != 0) {

                                model = model.setAtPosition(j, i, (byte) value);
                            }
                        }
                    }


                    view.setGame(model);
                    view.repaint();
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Vérification toutes les 5 secondes au lieu de 20 secondes
        timer.scheduleAtFixedRate(task, 0, 5000);
    }
}
