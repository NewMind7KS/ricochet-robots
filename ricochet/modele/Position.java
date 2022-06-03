package ricochet.modele;

/**
 * Classe définissant une position sur un plan à deux dimensions. Deux
 * coordonnées en x et y.
 */
public class Position {

	/** Position x : abscisse */
	private short x;

	/** Position y : ordonnée */
	private short y;

	/**
	 * Création d'une position de coordonnées (x,y).
	 * 
	 * @param x Position x
	 * @param y Position y
	 */
	public Position(short x, short y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Création d'une position copie d'une autre position.
	 * @param p Position copiée.
	 */
	public Position(Position p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public void setPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * Redéfinition de la méthode "equals" qui permet maintenant de savoir si deux positions sont équivalentes.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}

}
