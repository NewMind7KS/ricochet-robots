package ricochet.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import ricochet.modele.BFSSearch;
import ricochet.modele.Board;
import ricochet.modele.Solver;
import ricochet.util.EcouteurModele;
import ricochet.util.ModeleEcoutable;

public class VueMenu extends JPanel implements EcouteurModele {

	private static final long serialVersionUID = 1L;
	/** Un plateau de jeu */
	private Board board;
	/** Bouton pour afficher un plateau en ligne de commande */
	private JButton printBoard;
	/** Bouton pour enregistrer un plateau */
	private JButton writeBoard;
	/** Bouton pour utiliser l'algorithme de BFS */
	private JButton BFSSearch;
	/** Bouton pour utiliser l'algorithme A* */
	private JButton astar;
	/** Bouton pour réinitialiser le plateau */
	private JButton reset;
	/** Liste des positions des robots */
	private JComboBox<Object> liste;
	/** Liste des positions des objectifs */
	private JComboBox<Object> liste2;

	/**
	 * Constructeur de VueMenu. 
	 * Ajoute les différents boutons et listes d'objets
	 * @param board
	 */
	public VueMenu(Board board) {
		this.setLayout(new GridLayout(10, 2));
		this.board = board;
		board.ajoutEcouteur(this);
		printBoard = new JButton("Print board");
		printBoard.addActionListener(new ActionButton());
		add(printBoard);
		writeBoard = new JButton("Enregistrer plateau");
		writeBoard.addActionListener(new ActionButton());
		add(writeBoard);
		BFSSearch = new JButton("BFSSearch");
		BFSSearch.addActionListener(new ActionButton());
		add(BFSSearch);
		astar = new JButton("A* Solver");
		astar.addActionListener(new ActionButton());
		add(astar);
		reset = new JButton("Reset Board");
		reset.addActionListener(new ActionButton());
		add(reset);
		liste = new JComboBox<Object>(board.getRobots().toArray());
		liste2 = new JComboBox<Object>(board.getGoals().toArray());
		add(liste);
		add(liste2);
		liste.addActionListener(new ActionButton());
		liste2.addActionListener(new ActionButton());
		printBoard.setFocusable(false);
		writeBoard.setFocusable(false);
		BFSSearch.setFocusable(false);
		astar.setFocusable(false);
		reset.setFocusable(false);
		liste.setFocusable(false);
		liste2.setFocusable(false);
	}

	/**
	 * Lors de la mise à jour de la Vue, 
	 * les composants de cette dernière se mettent à jour
	 */
	@Override
	public void modeleMisAJour(ModeleEcoutable source) {
//		text.setText("Robot : " + board.getMainRobot() + ", Goal : " + board.getMainGoal());
		liste.repaint();
		liste2.repaint();
	}

	/**
	 * Classe interne qui permet d'utiliser les boutons et les listes d'objets
	 */
	class ActionButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == printBoard)
				board.printBoard();
			else if (ae.getSource() == writeBoard)
				board.writeBoard("Board.txt");
			else if (ae.getSource() == BFSSearch) {
				BFSSearch search = new BFSSearch(board);
				search.profondeur = 5;
				Thread th = new Thread(search);
				th.start();
			}
			else if (ae.getSource() == reset)
				board.loadBoard();
			else if (ae.getSource() == liste)
				board.setMainRobot(board.getRobots().get(liste.getSelectedIndex()));
			else if (ae.getSource() == liste2)
				board.setMainGoal(board.getGoals().get(liste2.getSelectedIndex()));
			else if (ae.getSource() == astar) {
				Thread th = new Thread(new Solver(board));
				th.start();
			}
		}
	}
}
