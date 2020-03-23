package ricochet.modele;

public class Start {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Pas de nom de fichier spécifié");
			System.exit(1);
		}
		Board b = new Board(args[0]);
		b.takeGoal();
		b.takeRobot();
		b.printBoard();
		System.out.println(b.getMainRobot().getPositionRobot());
		new Solver(b).solve();
		//new Solver(b).test();
	}

}
