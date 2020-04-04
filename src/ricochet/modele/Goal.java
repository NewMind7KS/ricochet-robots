package ricochet.modele;

/**
 * Représentation d'un objectif (cible) de ricochet robot. Une cible possède sa
 * propre position sur le plateau de jeu. Elle ne change pas.
 */
public class Goal {

	/** Générateur d'identifiant unique de la cible */
	public static int ID = 0;

	/** Position de la cible sur le plateau de jeu */
	private Position position;

	/** Identifiant unique de la cible */
	private int id;

	/**
	 * Création d'une cible en position p. Un identifiant lui est associée.
	 * 
	 * @param p Position de la cible sur le plateau de jeu.
	 */
	public Goal(Position p) {
		this.position = p;
		this.id = ID++;
	}

	public Position getPositionGoal() {
		return position;
	}

	public void setPosition(Position p) {
		position = p;
	}

	/**
	 * Description textuelle de la cible, il s'agit de la lettre G pour Goal suivi
	 * de son identifiant unique
	 * 
	 * @return Description textuelle de la cible, il s'agit de la lettre G pour Goal
	 *         suivi de son identifiant unique
	 */
	public String getName() {
		return "G" + String.valueOf(id);
	}

	@Override
	public String toString() {
		return "Goal [position=" + position + ", id=" + id + "]";
	}
}
