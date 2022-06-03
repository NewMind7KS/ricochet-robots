package ricochet.modele;

/**
 * Classe qui permet de lancer l'algorithme de résolution BFS
 */
public class Resolution {

	public static void main(String[] args) {
		Board board = new Board("dist/plateau.txt");
		board.takeGoal();
		for (Robot r : board.getRobots()) {
			board.setMainRobot(r);
			new BFSSearch(board).run();;
			System.out.println("\n\n");
		}
	}

}
