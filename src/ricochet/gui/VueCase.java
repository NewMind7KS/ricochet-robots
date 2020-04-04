package ricochet.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import ricochet.modele.Board;
import ricochet.modele.Direction;
import ricochet.modele.Position;

public class VueCase extends JPanel {

	private static final long serialVersionUID = 1L;
	/** Permet de donner une dimension à une case */
	private Dimension dimCase = new Dimension(42, 42);
	/** Position x */
	private short x;
	/** Position y */
	private short y;
	/** un plateau de jeu */
	private Board board;

	/**
	 * Constructeur de VueCase. 
	 * @param x position sur les lignes
	 * @param y position sur les colonnes
	 * @param board 
	 */
	public VueCase(short x, short y, Board board) {
		this.board = board;
		this.setPreferredSize(dimCase);
		this.x = x;
		this.y = y;
	}

	/**
	 * Méthode qui permet de mettre en couleur le robot sélectionné ainsi que l'objectif principal
	 * Ajoute aussi un trait pour chaque mur.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (board.isMainGoal(new Position(x, y)))
			setBackground(Color.GREEN);
		else if (board.isMainRobot(new Position(x, y)))
			setBackground(Color.RED);
		else
			setBackground(Color.WHITE);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawString(board.getEntities(x, y), 10, 20);
		if (board.isWall(x, y, Direction.W)) {
			Line2D linOuest = new Line2D.Double(1, 1, 1, 41);
			g2.draw(linOuest);
		}
		if (board.isWall(x, y, Direction.S)) {
			Line2D linSud = new Line2D.Double(1, 41, 41, 41);
			g2.draw(linSud);
		}
		if (board.isWall(x, y, Direction.E)) {
			Line2D linEst = new Line2D.Double(41, 1, 41, 41);
			g2.draw(linEst);
		}
		if (board.isWall(x, y, Direction.N)) {
			Line2D linNord = new Line2D.Double(1, 1, 41, 1);
			g2.draw(linNord);
		}
	}

	public void updateContent() {
		repaint();
	}

}
