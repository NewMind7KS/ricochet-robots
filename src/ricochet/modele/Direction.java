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
}
