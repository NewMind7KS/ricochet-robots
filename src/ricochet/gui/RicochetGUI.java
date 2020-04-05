package ricochet.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ricochet.modele.Board;
import ricochet.modele.Direction;

/**
 * Interface graphique du jeu de Ricochet Robot
 */
public class RicochetGUI extends JFrame implements WindowListener, KeyListener {

	private static final long serialVersionUID = 1L;
	/** Un plateau de jeu */
	private Board board;
	/** Un JPanel auquel on va ajouter un menu et un plateau */
	private JPanel contentPane = new JPanel();
	/** Un compteur pour comptabiliser le nombre de coups joué dans une partie*/
	private static int compteur = 0;


	/**
	 * Constructeur de l'interface graphique
	 */
	public RicochetGUI() {
		this.board = new Board("dist/plateau.txt");
		this.board.takeGoal();
		this.board.takeRobot();
		this.setTitle("Ricochet Robot");
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		this.setResizable(false);
		this.setVisible(true);
		this.setContentPane(contentPane);
		contentPane.add(new VueBoard(board));
		contentPane.add(new VueMenu(board));
		this.addKeyListener(this);
		this.pack();
	}
	
	
	public RicochetGUI(String string) {
		this.board = new Board(string);
		this.board.takeGoal();
		this.board.takeRobot();
		this.setTitle("Ricochet Robot");
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
		this.setResizable(false);
		this.setVisible(true);
		this.setContentPane(contentPane);
		contentPane.add(new VueBoard(board));
		contentPane.add(new VueMenu(board));
		this.addKeyListener(this);
		this.pack();
	}


	public static int getCompteur() {
		return compteur;
	}

	public static void setCompteur(int compteur) {
		RicochetGUI.compteur = compteur;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	/**
	 * Méthode qui permet de jouer avec les flèches directionnelles du clavier
	 */
	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.W)) {
				board.moveRobot(board.getMainRobot(), Direction.W);
				RicochetGUI.compteur++;
			}
		} else if (key.getKeyCode() == KeyEvent.VK_UP) {
			if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.N)) {
				board.moveRobot(board.getMainRobot(), Direction.N);
				RicochetGUI.compteur++;
			}
		} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.E)) {
				board.moveRobot(board.getMainRobot(), Direction.E);
				RicochetGUI.compteur++;
			}
		} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.S)) {
				board.moveRobot(board.getMainRobot(), Direction.S);
				RicochetGUI.compteur++;
			}
		}
		if (board.isFinished())
			JOptionPane.showMessageDialog(this, "Jeu Terminé en " + getCompteur() + " coups");
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
