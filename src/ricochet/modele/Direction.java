package ricochet.modele;

import java.util.Random;

public enum Direction {
	N, S, E, W;
	
	public static Direction getRandomDirection() {
		Random rand = new Random();
		return values()[rand.nextInt(values().length)];
	}
}
