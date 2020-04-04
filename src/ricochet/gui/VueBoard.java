package ricochet.gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import ricochet.modele.Board;
import ricochet.util.EcouteurModele;
import ricochet.util.ModeleEcoutable;

public class VueBoard extends JPanel implements EcouteurModele {

	private static final long serialVersionUID = 1L;
	/** Liste des 26 JPanels qui représentent les cases */
	private ArrayList<VueCase> cases = new ArrayList<VueCase>(256);
	/** Plateau de jeu affiché */
	private Board board;

	/**
	 * Constructeur de VueBoard. Permet d'avoir une vue sur les cases du plateau
	 * @param board 
	 */
	public VueBoard(Board board) {
		this.board = board;
		board.ajoutEcouteur(this);
		GridLayout grid = new GridLayout(16, 16);
		this.setLayout(grid);
		for (short i = 0; i < board.getHeigth(); i++) {
			for (short j = 0; j < board.getWidth(); j++) {
				cases.add(new VueCase(i, j, board));
				this.add(cases.get(i * 16 + j));
			}
		}
	}

	public ModeleEcoutable getModele() {
		return board;
	}

	@Override
	public void modeleMisAJour(ModeleEcoutable source) {
		for (VueCase vc : cases)
			vc.updateContent();
	}
}
