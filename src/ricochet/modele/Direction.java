package ricochet.modele;

import java.util.Random;

/**
 * Énumération des directions utilisées pour jouer au jeu ricohet robot. Les
 * directions servent à donner les directions des mouvements.
 */
public enum Direction {
	/** Direction Nord */
	N,
	/** Direction Sud */
	S,
	/** Direction Est */
	E,
	/** Direction Ouest */
	W;

	/**
	 * Renvoi une direction aléatoire parmi les quatre possibles.
	 * 
	 * @return Une direction aléatoire parmi les quatre possibles.
	 */
	public static Direction getRandomDirection() {
		Random rand = new Random();
		return values()[rand.nextInt(values().length)];
	}

	/**
	 * Méthode qui donne un symbole pour une direction
	 */
	public static String symbol(Direction dir) {
		switch (dir) {
		case N:
			return "↑";
		case S:
			return "↓";
		case E:
			return "→";
		case W:
			return "←";
		default:
			return "*";
		}
	}
	
	/**
	 * Méthode qui donne la direction opposée de celle donnée en paramètre
	 * @param dir une Direction
	 * @return la Direction opposée 
	 */
	public static Direction opposite(Direction dir) {
		switch (dir) {
		case N:
			return S;
		case S:
			return N;
		case E:
			return W;
		case W:
			return E;
		}
		return null;
	}
}
