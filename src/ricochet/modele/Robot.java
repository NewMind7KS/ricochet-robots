package ricochet.modele;

/**
 * Représentation d'un robot de Ricochet Robot. Un robot possède sa propre
 * position sur le plateau de jeu. Elle peut changer suivant ses déplacements.
 */
public class Robot {

	/** Générateur d'identifiant unique de la cible */
	public static int ID = 0;

	/** Position de la cible sur le plateau de jeu */
	private Position position;

	/** Identifiant unique de la cible */
	private int id;

	/**
	 * Création d'un robot en position p. Un identifiant lui est associée.
	 * 
	 * @param p Position du robot sur le plateau de jeu.
	 */
	public Robot(Position p) {
		this.position = p;
		this.id = ID++;
	}

	public Position getPositionRobot() {
		return position;
	}

	public void setPosition(Position p) {
		position = p;
	}

	/**
	 * Description textuelle du robot, il s'agit de la lettre R pour Robot suivi de
	 * son identifiant unique
	 * 
	 * @return Description textuelle du robot, il s'agit de la lettre R pour Robot
	 *         suivi de son identifiant unique
	 */
	public String getName() {
		return "R" + String.valueOf(id);
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Robot [position=" + position + ", id=" + id + "]";
	}
}
