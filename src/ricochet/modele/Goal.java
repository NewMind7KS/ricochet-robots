package ricochet.modele;

public class Goal {

	public static int ID = 0;
	private Position position;
	private int id;

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
	
	public String getName() {
		return "G" + String.valueOf(id);
	}

	@Override
	public String toString() {
		return "Goal [position=" + position + ", id=" + id + "]";
	}
}
