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

	private boolean Verrou;
	private boolean blackCase = false;
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

	public boolean isVerrou() {
		return Verrou;
	}

	public void setVerrou(boolean verrou) {
		Verrou = verrou;
	}

	public boolean isBlackCase() {
		return blackCase;
	}

	public void setBlackCase() {
		this.blackCase = blackCase;
	}
    public boolean isMur() {
        return (wallN || wallS || wallE || wallW);
    }

    /**
     * Création d'un angle sur la case en fonction de l'angle donné.
     * 
     * @param direction Entier qui donne la position de l'angle sur la case
     */
    public void faireAngle(int direction) {
            switch (direction) {
            case 0:
                    this.setWall(Direction.N);
                    this.setWall(Direction.W);
                    break;
            case 1:
                    this.setWall(Direction.N);
                    this.setWall(Direction.E);
                    break;
            case 2:
                    this.setWall(Direction.S);
                    this.setWall(Direction.W);
                    break;
            case 3:
                    this.setWall(Direction.S);
                    this.setWall(Direction.E);
                    break;
            default:
                    System.err.println("Erreur de clé dans la méthode faireAngle" + direction);
                    System.exit(1);
                    break;
            }
    }
    
    /**
     * Retourne vrai si un mur est présent sur la case dans la direction donnée.
     * 
     * @param direction Côté de la case où le mur est
     * @return Retourne vrai si un mur est présent sur la case dans la direction.
     */
    public boolean isMur(Direction direction) {
            switch (direction) {
            case N:
                    return this.wallN;
            case S:
                    return this.wallS;
            case W:
                    return this.wallW;
            case E:
                    return this.wallE;
            default:
                    return false;
            }
    }

	public boolean isWallN() {
		return wallN;
	}

	public void setWallN(boolean wallN) {
		this.wallN = wallN;
	}

	public boolean isWallS() {
		return wallS;
	}

	public void setWallS(boolean wallS) {
		this.wallS = wallS;
	}

	public boolean isWallE() {
		return wallE;
	}

	public void setWallE(boolean wallE) {
		this.wallE = wallE;
	}

	public boolean isWallW() {
		return wallW;
	}

	public void setWallW(boolean wallW) {
		this.wallW = wallW;
	}
	
	public void setAllWalls(boolean wall){
		setWallW(wall);
		setWallN(wall);
		setWallE(wall);
		setWallS(wall);
	}
	
}
