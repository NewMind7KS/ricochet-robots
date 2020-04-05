package ricochet.gui;

/**
 * Classe qui permet de démarrer une partie de Ricochet Robot
 */
public class Start {

	public static void main(String[] args) {
		if (args.length > 0)
			new RicochetGUI(args[0]);
		else
			new RicochetGUI();
	}

}
