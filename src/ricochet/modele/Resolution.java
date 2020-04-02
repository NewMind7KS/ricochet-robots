package ricochet.modele;

public class Resolution {

	public static void main(String[] args) {
		Board board = new Board("testBoard2.txt");
		board.takeGoal();
		
		for (Robot r : board.getRobots()) {
			board.setMainRobot(r);
			new BFSSearch(board).run();;
			System.out.println("\n\n");
		}
	}

}
