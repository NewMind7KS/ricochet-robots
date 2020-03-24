package ricochet.modele;

/**
 * Représente une case du platau de jeu ricochet robot. Une peut posséder un mur
 * dans les quatres directions cardinales.
 */
public class Case {

	/** Boolean : si actif présence d'un mur dans la case au Nord */
	private boolean wallN = false;

	/** Boolean : si actif présence d'un mur dans la case au Sud */
	private boolean wallS = false;

	/** Boolean : si actif présence d'un mur dans la case à l'Est */
	private boolean wallE = false;

	/** Boolean : si actif présence d'un mur dans la case à l'Ouest */
	private boolean wallW = false;

	/**
	 * Ajout d'un mur dans la direction donnée.
	 * 
	 * @param dir Direction de l'ajout du mur.
	 */
	public void setWall(Direction dir) {
		switch (dir) {
		case N:
			wallN = true;
			break;
		case S:
			wallS = true;
			break;
		case E:
			wallE = true;
			break;
		case W:
			wallW = true;
			break;
		}
	}

	/**
	 * Indique si un mur est présent dans la direction donnée.
	 * 
	 * @param dir Direction testée pour la présence de mur.
	 * @return Vrai si un mur est présent dans la direction donnée.
	 */
	public boolean isWall(Direction dir) {
		switch (dir) {
		case N:
			return wallN;
		case S:
			return wallS;
		case E:
			return wallE;
		case W:
			return wallW;
		default:
			return false;
		}
	}

	/**
	 * Description textuelle de la case pour sa sérialisation dans un fichier de
	 * sauvegarde de plateau.
	 * 
	 * @return Description textuelle de la case pour sa sérialisation dans un
	 *         fichier de sauvegarde de plateau.
	 */
	public String toWrite() {
		String str = "";
		str = wallE ? str + "E " : str;
		str = wallW ? str + "W " : str;
		str = wallN ? str + "N " : str;
		str = wallS ? str + "S " : str;
		return str;
	}

	@Override
	public String toString() {
		String str = "";
		str = wallE ? str + "E" : str + " ";
		str = wallW ? str + "W" : str + " ";
		str = wallN ? str + "N" : str + " ";
		str = wallS ? str + "S" : str + " ";
		return str;
	}
}
