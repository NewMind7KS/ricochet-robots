package ricochet.modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ricochet.util.AbstractModeleEcoutable;

public class Board extends AbstractModeleEcoutable {

	private Case[][] cases;
	private int width = 16;
	private int height = 16;
	private ArrayList<Robot> robots;
	private ArrayList<Goal> goals;
	private Robot mainRobot;
	private Goal mainGoal;
	private String filename;

	public Board(String filename) {
		this.filename = filename;
		this.robots = new ArrayList<Robot>();
		this.goals = new ArrayList<Goal>();
		this.cases = new Case[16][16];
		reload();
	}

	public Robot takeRobot() {
		assert robots.size() > 0 : "Robots taille 0";
		Random rand = new Random();
		mainRobot = robots.get(rand.nextInt(robots.size()));
		for (Robot r : robots) {
			if (r.getId() == 0)
				mainRobot = r;
		}
		return mainRobot;
	}

	public Goal takeGoal() {
		assert goals.size() > 0 : "Goals taille 0";
		Random r = new Random();
		mainGoal = goals.get(r.nextInt(goals.size()));
		mainGoal = goals.get(12);
		return mainGoal;
	}

	public boolean isFinished() {
		return mainGoal.getPositionGoal().equals(mainRobot.getPositionRobot());
	}

	public boolean isRobot(Position p) {
		for (Robot r : robots) {
			if (r.getPositionRobot().equals(p))
				return true;
		}
		return false;
	}

	public boolean isGoal(Position p) {
		for (Goal g : goals) {
			if (g.getPositionGoal().equals(p))
				return true;
		}
		return false;
	}

	public boolean isMainGoal(Position p) {
		assert mainGoal != null : "MainGoal null";
		return (mainGoal.getPositionGoal().equals(p));
	}

	public boolean isMainRobot(Position p) {
		assert mainGoal != null : "MainRobot null";
		return (mainRobot.getPositionRobot().equals(p));
	}

	public boolean isRobot(int x, int y, Direction dir) {
		switch (dir) {
		case N:
			return isRobotN(x, y);
		case S:
			return isRobotS(x, y);
		case E:
			return isRobotE(x, y);
		case W:
			return isRobotW(x, y);
		default:
			return false;
		}
	}

	private boolean isRobotW(int x, int y) {
		if (x < height && y < width && x >= 0 && y > 0) {
			Position p = new Position(x, y - 1);
			return isRobot(p);
		}
		return false;
	}

	private boolean isRobotE(int x, int y) {
		if (x < height && y < width && x >= 0 && y >= 0) {
			Position p = new Position(x, y + 1);
			return isRobot(p);
		}
		return false;
	}

	private boolean isRobotS(int x, int y) {
		if (x < height && y < width - 1 && x >= 0 && y >= 0) {
			Position p = new Position(x + 1, y);
			return isRobot(p);
		}
		return false;
	}

	private boolean isRobotN(int x, int y) {
		if (x < height && y < width && x > 0 && y >= 0) {
			Position p = new Position(x - 1, y);
			return isRobot(p);
		}
		return false;
	}

	public void addWall(int x, int y, Direction dir) {
		if (x < height && y < width && x >= 0 && y >= 0)
			cases[x][y].setWall(dir);
		switch (dir) {
		case W:
			if (y - 1 >= 0)
				cases[x][y - 1].setWall(Direction.E);
			break;
		case S:
			if (x + 1 < height)
				cases[x + 1][y].setWall(Direction.N);
			break;
		case N:
			if (x - 1 >= 0)
				cases[x - 1][y].setWall(Direction.S);
			break;
		case E:
			if (y + 1 < width)
				cases[x][y + 1].setWall(Direction.W);
			break;
		}
	}

	public void addSimpleWall(int x, int y, Direction dir) {
		if (x < height && y < width && x >= 0 && y >= 0)
			cases[x][y].setWall(dir);
	}

	public void printBoard() {
		String putStr = "";
		for (int i = 0; i < height; i++) {
			putStr = String.valueOf(i) + " ";
			for (int j = 0; j < width; j++) {
				putStr += isRobot(new Position(i, j)) ? "R" : " ";
				putStr += isGoal(new Position(i, j)) ? "G" : " ";
				putStr += cases[i][j].toString() + "|";
			}
			System.out.println(putStr);
		}
		notifyListener();
	}

	public boolean isWall(int x, int y, Direction dir) {
		if (x < height && y < width && x >= 0 && y >= 0) {
			switch (dir) {
			case N:
				return cases[x][y].isWall(Direction.N);
			case S:
				return cases[x][y].isWall(Direction.S);
			case E:
				return cases[x][y].isWall(Direction.E);
			case W:
				return cases[x][y].isWall(Direction.W);
			default:
				return false;
			}
		}
		return false;
	}

	public Position move(Position start, Direction dir) {
		Position p = new Position(start);
		while (!isWall(p.getX(), p.getY(), dir) && !isRobot(p.getX(), p.getY(), dir)) {
			switch (dir) {
			case N:
				p.setX(p.getX() - 1);
				break;
			case S:
				p.setX(p.getX() + 1);
				break;
			case W:
				p.setY(p.getY() - 1);
				break;
			case E:
				p.setY(p.getY() + 1);
				break;
			}
		}
		return p;
	}

	public void moveRobot(Robot r, Direction dir) {
		Position p = r.getPositionRobot();
		r.setPosition(move(p, dir));
		notifyListener();
	}

	public void moveRobotToPosition(Robot r, Position newPosition) {
		Position p = r.getPositionRobot();
		if (isRobot(p))
			getRobot(p).setPosition(newPosition);
		notifyListener();
	}

	public boolean canMoveInDir(Position p, Direction dir) {
		switch (dir) {
		case N:
			return !(p.getX() == 0 || isWall(p.getX(), p.getY(), dir) || isRobotN(p.getX(), p.getY()));
		case S:
			return !(p.getX() == height - 1 || isWall(p.getX(), p.getY(), dir) || isRobotS(p.getX(), p.getY()));
		case E:
			return !(p.getY() == width - 1 || isWall(p.getX(), p.getY(), dir) || isRobotE(p.getX(), p.getY()));
		case W:
			return !(p.getY() == 0 || isWall(p.getX(), p.getY(), dir) || isRobotW(p.getX(), p.getY()));
		}
		return false;
	}

	public ArrayList<Position> getAllMoves(Robot r) {
		ArrayList<Position> directions = new ArrayList<Position>();
		for (Direction d : Direction.values()) {
			if (canMoveInDir(r.getPositionRobot(), d))
				directions.add(move(r.getPositionRobot(), d));
		}
		return directions;
	}

	public ArrayList<Position> getAllMoves(Position position) {
		ArrayList<Position> directions = new ArrayList<Position>();
		for (Direction d : Direction.values()) {
			if (canMoveInDir(position, d))
				directions.add(move(position, d));
		}
		return directions;
	}

	public Case getCase(int x, int y) {
		return cases[x][y];
	}

	public ArrayList<Robot> getRobots() {
		return robots;
	}

	public ArrayList<Goal> getGoals() {
		return goals;
	}

	public int getWidth() {
		return width;
	}

	public int getHeigth() {
		return height;
	}

	public Robot getRobot(Position p) {
		for (Robot r : robots) {
			if (r.getPositionRobot().equals(p))
				return r;
		}
		return null;
	}

	public Robot getMainRobot() {
		if (mainRobot == null)
			takeRobot();
		return mainRobot;
	}

	public Goal getMainGoal() {
		if (mainGoal == null)
			takeGoal();
		return mainGoal;
	}

	public String getEntities(int x, int y) {
		return getEntities(new Position(x, y));
	}

	public String getEntities(Position p) {
		String str = "";
		for (Robot r : robots) {
			if (r.getPositionRobot().equals(p)) {
				str += r.getName();
				break;
			}
		}
		for (Goal r : goals) {
			if (r.getPositionGoal().equals(p)) {
				str += r.getName();
				break;
			}
		}
		return str;
	}

	/**
	 * Distance de Manhattan entre le robot principal et la cible principale
	 * 
	 * @return Distance de Manhattan entre le robot principal et la cible principale
	 */
	public int distanceManhattan() {
		Position r = mainRobot.getPositionRobot();
		Position g = mainGoal.getPositionGoal();
		return distanceManhattan(r, g);
	}

	public int distanceManhattan(Position r) {
		Position g = mainGoal.getPositionGoal();
		return distanceManhattan(r, g);
	}

	public int distanceManhattan(Position r, Position g) {
		return Math.abs(r.getX() - g.getX()) + Math.abs(r.getY() - g.getY());
	}

	public boolean onTheLine() {
		if (mainGoal.getPositionGoal().getX() == mainRobot.getPositionRobot().getX()) {
			return (move(mainRobot.getPositionRobot(), Direction.E).equals(mainGoal.getPositionGoal())
					|| move(mainRobot.getPositionRobot(), Direction.W).equals(mainGoal.getPositionGoal()));
		} else if (mainGoal.getPositionGoal().getY() == mainRobot.getPositionRobot().getY()) {
			return (move(mainRobot.getPositionRobot(), Direction.N).equals(mainGoal.getPositionGoal())
					|| move(mainRobot.getPositionRobot(), Direction.S).equals(mainGoal.getPositionGoal()));
		}
		return false;
	}

	public void writeBoard(String filename) {
		String str = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (cases[i][j].toWrite() != "" || isRobot(new Position(i, j)) || isGoal(new Position(i, j))) {
					str += String.valueOf(i) + " " + String.valueOf(j) + " " + cases[i][j].toWrite();
					str += isRobot(new Position(i, j)) ? "R " : "";
					str += isGoal(new Position(i, j)) ? "G  " : "";
					str += "\n";
				}
			}
		}
		try {
			File file = new File(filename);
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			} else {
				System.out.println("File already exists.");
			}
			FileWriter fileWriter = new FileWriter(filename);
			fileWriter.write(str);
			fileWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void reload() {
		robots.clear();
		goals.clear();
		Robot.ID = 0;
		Goal.ID = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				cases[i][j] = new Case();
			}
		}
		try {
			BufferedReader file = new BufferedReader(new FileReader(new File("./" + this.filename)));
			String line = "";
			int nLine = 0;
			int x, y;
			while ((line = file.readLine()) != null) {
				nLine++;
				String[] values = line.split(" ");
				if (values.length < 2) {
					System.err.println("Erreur sur la ligne " + nLine);
					break;
				}
				try {
					x = Integer.valueOf(values[0]);
					y = Integer.valueOf(values[1]);
					if (x < 0 || y < 0 || x > 15 || y > 15)
						new NumberFormatException();
				} catch (NumberFormatException ne) {
					System.err.println("Bad index on line " + nLine);
					break;
				}
				for (String s : values) {
					switch (s) {
					case "N":
						addSimpleWall(x, y, Direction.N);
						break;
					case "S":
						addSimpleWall(x, y, Direction.S);
						break;
					case "E":
						addSimpleWall(x, y, Direction.E);
						break;
					case "W":
						addSimpleWall(x, y, Direction.W);
						break;
					case "R":
						robots.add(new Robot(new Position(x, y)));
						break;
					case "G":
						goals.add(new Goal(new Position(x, y)));
						break;
					}
				}
			}
			file.close();
		} catch (IOException e) {
			System.err.println("Erreur lors de la lecture du fichier " + filename);
		}
		takeRobot();
		takeGoal();
		notifyListener();
	}

}
