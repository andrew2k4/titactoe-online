package view;

import controller.TicTacToeControllerInterface;
import model.TicTacToeModelInterface;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * This class represents the view component in the MVC pattern for our game.
 * It extends JPanel and handles the graphical display.
 */
public class TicTacToeView extends JPanel {
    /**
     * The model representing the game state.
     */
    private TicTacToeModelInterface model;

    /**
     * The controller for managing user interactions.
     */
    private TicTacToeControllerInterface controller;

    /**
     * Constructs a new TicTacToeView with the specified model and controller.
     *
     * @param model the game model.
     * @param controller the game controller.
     */
    public TicTacToeView(TicTacToeModelInterface model, TicTacToeControllerInterface controller) {
        this.model = model;
        this.controller = controller;
        createView();
    }

    /**
     * Sets the current game model.
     *
     * @param g the new game model.
     */
    public void setGame(TicTacToeModelInterface g) {
        model = g;
    }

    /**
     * Gets the current game model.
     *
     * @return the current game model.
     */
    public TicTacToeModelInterface getGame() {
        return model;
    }

    /**
     * The size of each cell in our grid.
     */
    public int UNIT = 200;

    /**
     * Returns the preferred size of the view.
     *
     * @return the preferred dimension of the panel based on the game grid size.
     */
    @Override public Dimension getPreferredSize() {
        return new Dimension(model.getColumns() * UNIT, model.getRows() * UNIT);
    }

    /**
     * Paints the current state of the game onto the panel.
     *
     * @param g the Graphics object used for drawing.
     */
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGridLines(g);

        for (byte c = 0; c < model.getColumns(); c++) {
            for (byte r = 0; r < model.getRows(); r++) {
                if (model.getAtPosition(c, r) != model.getPlayerNone()) {
                    g.setColor(selectColor(model.getAtPosition(c, r)));
                    g.fillOval(c * UNIT, r * UNIT, UNIT, UNIT);
                }
            }
        }
    }

    /**
     * Draws the grid lines for our board.
     *
     * @param g the Graphics object used for drawing the grid lines.
     */
    public void drawGridLines(Graphics g) {
        g.setColor(Color.BLACK);

        // Draw vertical lines
        for (int i = 1; i < model.getColumns(); i++) {
            g.drawLine(i * UNIT, 0, i * UNIT, model.getRows() * UNIT);
        }

        // Draw horizontal lines
        for (int i = 1; i < model.getRows(); i++) {
            g.drawLine(0, i * UNIT, model.getColumns() * UNIT, i * UNIT);
        }
    }

    /**
     * Selects the color for a player.
     *
     * @param player the player byte to determine the color for.
     * @return the corresponding color for the player.
     */
    public Color selectColor(byte player) {
        if (player == model.getPlayerOne()) {
            return Color.YELLOW;
        }
        if (player == model.getPlayerTwo()) {
            return Color.BLUE;
        }
        return Color.BLACK;
    }

    /**
     * Creates and initializes the view for our game.
     * This method sets up the JFrame and attaches necessary event listeners.
     */
    public void createView() {
        JFrame f = new JFrame("Tic Tac Toe");
        f.add(this);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                byte c = (byte) (ev.getPoint().getX() / UNIT);
                byte r = (byte) (ev.getPoint().getY() / UNIT);

                try {
                    controller.whenMousePressed(c, r);
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        f.setVisible(true);
        f.add(this);
    }
}
