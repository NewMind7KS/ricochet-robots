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

public class RicochetGUI extends JFrame implements WindowListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private Board board;
	private JPanel contentPane = new JPanel();

	public RicochetGUI() {
		this.board = new Board("testBoard2.txt");
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

	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_LEFT) {
			board.moveRobot(board.getMainRobot(), Direction.W);
		} else if (key.getKeyCode() == KeyEvent.VK_UP) {
			board.moveRobot(board.getMainRobot(), Direction.N);
		} else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
			board.moveRobot(board.getMainRobot(), Direction.E);
		} else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
			board.moveRobot(board.getMainRobot(), Direction.S);
		}
		if (board.isFinished())
			JOptionPane.showMessageDialog(this, "Jeu Terminé");
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
