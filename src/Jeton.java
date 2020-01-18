
public class Jeton {

	//position du jeton sur le board
	protected int x;
	protected int y;
	//sa couleur
	protected String color;
	
	public Jeton(){
		this(-1, -1, "None");
	}
	
	public Jeton(int x, int y, String color){
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
