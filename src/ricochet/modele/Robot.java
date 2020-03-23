package ricochet.modele;

public class Robot {

	public static int ID = 0;
	private Position position;
	private int id;

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
