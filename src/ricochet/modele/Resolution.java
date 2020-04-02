package ricochet.modele;

public class Resolution {

	public static void main(String[] args) {
		Board board = new Board("testBoard2.txt");
		board.takeGoal();
		
		for (Robot r : board.getRobots()) {
			board.setMainRobot(r);
			System.out.println("Démarrage résolution pour robot " + r.getName());
			System.out.println("Cible principale :" + board.getMainGoal().getName());
			new BFSSearch(board).run();;
			System.out.println("\n\n");
		}
		
	}

}
