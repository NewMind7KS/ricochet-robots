package ricochet.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ricochet.modele.Board;
import ricochet.modele.Solver;
import ricochet.util.EcouteurModele;
import ricochet.util.ModeleEcoutable;

public class VueMenu extends JPanel implements EcouteurModele {

	private static final long serialVersionUID = 1L;
	private Board board;
	private JButton printBoard;
	private JButton test;
	private JButton test2;
	private JButton reset;

	public VueMenu(Board board) {
		this.setLayout(new GridLayout(10, 2));
		this.board = board;
		board.ajoutEcouteur(this);
		printBoard = new JButton("Print board");
		printBoard.addActionListener(new ActionButton());
		add(printBoard);
		test = new JButton("Enregistrer plateau");
		test.addActionListener(new ActionButton());
		add(test);
		test2 = new JButton("A*");
		test2.addActionListener(new ActionButton());
		add(test2);
		reset = new JButton("Reset Board");
		reset.addActionListener(new ActionButton());
		add(reset);
		printBoard.setFocusable(false);
		test.setFocusable(false);
		test2.setFocusable(false);
		reset.setFocusable(false);
	}

	@Override
	public void modeleMisAJour(ModeleEcoutable source) {
//		text.setText("Robot : " + board.getMainRobot() + ", Goal : " + board.getMainGoal());
	}
	
	class ActionButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == printBoard)
				board.printBoard();
			else if (ae.getSource() == test)
				board.writeBoard("Board.txt");
			else if (ae.getSource() == test2)
				new Solver(board).solve();
			else if(ae.getSource() == reset)
				board.reload();
		}
	}
}
