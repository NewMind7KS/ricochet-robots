package ricochet.modele;

public class Case {

	private boolean wallN = false;
	private boolean wallS = false;
	private boolean wallE = false;
	private boolean wallW = false;

	public Case() {
	}

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
