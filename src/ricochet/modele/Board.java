package ricochet.modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ricochet.util.AbstractModeleEcoutable;

/**
 * Classse représentant un plateau de Jeu de RIcochet Robot. Un plateau de 16
 * cases sur 16 contenant des murs et des entités robots et objectifs. Un robot
 * principal doit se rendre sur la cible principale pour terminer le jeu.
 */
public class Board extends AbstractModeleEcoutable {

	/** 256 cases du plateau */
	private Case[][] cases;

	/** Largeur du plateau de jeu */
	private int width = 16;

	/** Largeur du plateau de jeu */
	private int height = 16;

	/**
	 * Liste contenant l'ensemble des entités robot présentes sur le plateau de jeu
	 */
	private ArrayList<Robot> robots;

	/**
	 * Liste contenant l'ensemble des entités objectifs présentes sur le plateau de
	 * jeu
	 */
	private ArrayList<Goal> goals;

	/** Robot principal : doit atteindre l'objectif principal */
	private Robot mainRobot;

	/** Cible principale : doit être atteinte par le robot principal */
	private Goal mainGoal;

	/** Nom du fichier contenant la description du plataeu de jeu */
	private String filename;

	/**
	 * Création du plateau de jeu par rapport à un nom de fichier donné. Il y a 256
	 * cases représentées par des objects Case.
	 * 
	 * @param filename Nom du fichier contenant la description du plateau.
	 */
	public Board(String filename) {
		this.filename = filename;
		this.robots = new ArrayList<Robot>();
		this.goals = new ArrayList<Goal>();
		this.cases = new Case[16][16];
		loadBoard();
	}

	/**
	 * Pioche d'un robot aléatoire sur le plateau de jeu.
	 * 
	 * @return Robot pioché aléatoirement sur le plateau de jeu.
	 */
	public Robot takeRobot() {
		assert robots.size() > 0 : "Robots taille 0";
		Random rand = new Random();
		mainRobot = robots.get(rand.nextInt(robots.size()));
		/* Pour les tests, on fixe le robot */
		for (Robot r : robots) {
			if (r.getId() == 0)
				mainRobot = r;
		}
		return mainRobot;
	}

	/**
	 * Pioche d'une cible aléatoire sur le plateau de jeu.
	 * 
	 * @return Cible piochée aléatoirement sur le plateau de jeu.
	 */
	public Goal takeGoal() {
		assert goals.size() > 0 : "Goals taille 0";
		Random r = new Random();
		mainGoal = goals.get(r.nextInt(goals.size()));
		/* Pour les tests, on fixe la cible */
		mainGoal = goals.get(14);
		return mainGoal;
	}

	/**
	 * Indique si le jeu est terminé, i.e. le robot principal est sur la cible
	 * principale.
	 * 
	 * @return Vrai si le trobot principal est sur la cible principale.
	 */
	public boolean isFinished() {
		return mainGoal.getPositionGoal().equals(mainRobot.getPositionRobot());
	}

	/**
	 * Indique si un robot est présent à la position donnée sur le plateau de jeu.
	 * 
	 * @param p Position testée
	 * @return Vrai si un robot est présent à la position p donnée.
	 */
	public boolean isRobot(Position p) {
		for (Robot r : robots) {
			if (r.getPositionRobot().equals(p))
				return true;
		}
		return false;
	}

	/**
	 * Indique si une cible est présente à la position donnée sur le plateau de jeu.
	 * 
	 * @param p Position testée
	 * @return Vrai si une cible est présente à la position p donnée.
	 */
	public boolean isGoal(Position p) {
		for (Goal g : goals) {
			if (g.getPositionGoal().equals(p))
				return true;
		}
		return false;
	}

	/**
	 * Indique si la cible principale est présente à la position donnée sur le
	 * plateau de jeu.
	 * 
	 * @param p Position testée
	 * @return Vrai si la cible principale est présente à la position p donnée.
	 */
	public boolean isMainGoal(Position p) {
		assert mainGoal != null : "MainGoal null";
		return (mainGoal.getPositionGoal().equals(p));
	}

	/**
	 * Indique si le robot principal est présent à la position donnée sur le plateau
	 * de jeu.
	 * 
	 * @param p Position testée
	 * @return Vrai si le robot principal est présent à la position p donnée.
	 */
	public boolean isMainRobot(Position p) {
		assert mainGoal != null : "MainRobot null";
		return (mainRobot.getPositionRobot().equals(p));
	}

	/**
	 * Indique si un robot est présent dans la case adjaçente à celle donnée et dans
	 * la direction donnée.
	 * 
	 * @param p   Position sur le plateau
	 * @param dir Direction du test
	 * @return Vrai si un robot est présent dans la case dans la direction donnée
	 *         pour la case adjaçente à la position donnée.
	 */
	public boolean isRobot(Position p, Direction dir) {
		switch (dir) {
		case N:
			return isRobotN(p.getX(), p.getY());
		case S:
			return isRobotS(p.getX(), p.getY());
		case E:
			return isRobotE(p.getX(), p.getY());
		case W:
			return isRobotW(p.getX(), p.getY());
		default:
			return false;
		}
	}

	/**
	 * Indique si un robot est présent dans la case à l'Ouest (gauche) de la
	 * position en (x,y).
	 * 
	 * @param x Ligne dans le plateau
	 * @param y Colonne dans le plateau
	 * @return Vrai si un robot est présent dans la case à l'Ouest (gauche) de la
	 *         position en (x,y).
	 */
	private boolean isRobotW(int x, int y) {
		if (x < height && y < width && x >= 0 && y > 0) {
			Position p = new Position(x, y - 1);
			return isRobot(p);
		}
		return false;
	}

	/**
	 * Indique si un robot est présent dans la case à l'Est (droite) de la position
	 * en (x,y).
	 * 
	 * @param x Ligne dans le plateau
	 * @param y Colonne dans le plateau
	 * @return Vrai si un robot est présent dans la case à l'Est (droite) de la
	 *         position en (x,y).
	 */
	private boolean isRobotE(int x, int y) {
		if (x < height && y < width && x >= 0 && y >= 0) {
			Position p = new Position(x, y + 1);
			return isRobot(p);
		}
		return false;
	}

	/**
	 * Indique si un robot est présent dans la case au Sud (bas) de la position en
	 * (x,y).
	 * 
	 * @param x Ligne dans le plateau
	 * @param y Colonne dans le plateau
	 * @return Vrai si un robot est présent dans la case au Sud (bas) de la position
	 *         en (x,y).
	 */
	private boolean isRobotS(int x, int y) {
		if (x < height && y < width && x >= 0 && y >= 0) {
			Position p = new Position(x + 1, y);
			return isRobot(p);
		}
		return false;
	}

	/**
	 * Indique si un robot est présent dans la case au Nord (haut) de la position en
	 * (x,y).
	 * 
	 * @param x Ligne dans le plateau
	 * @param y Colonne dans le plateau
	 * @return Vrai si un robot est présent dans la case au Nord (haut) de la
	 *         position en (x,y).
	 */
	private boolean isRobotN(int x, int y) {
		if (x < height && y < width && x > 0 && y >= 0) {
			Position p = new Position(x - 1, y);
			return isRobot(p);
		}
		return false;
	}

	/**
	 * Ajout d'un mur en position (x,y) et dans la direction dir donnée. Cette
	 * méthode ajoute un mur sur la case mais aussi dans la case adjaçente dans la
	 * direction opposée.
	 * 
	 * @param x   Ligne dans le plateau
	 * @param y   COlonne dans le plateau
	 * @param dir Direction de l'ajout du mur sur la case en position (x,y)
	 */
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

	/**
	 * Ajout d'un mur en position (x,y) et dans la direction dir donnée.
	 * 
	 * @param x   Ligne dans le plateau
	 * @param y   COlonne dans le plateau
	 * @param dir Direction de l'ajout du mur sur la case en position (x,y)
	 */
	public void addSimpleWall(int x, int y, Direction dir) {
		if (x < height && y < width && x >= 0 && y >= 0)
			cases[x][y].setWall(dir);
	}

	/**
	 * Affichage en console de l'état actuel du plateau de jeu. Les murs sont
	 * représentés par la lettre de leur direction. Les entités présente sous forme
	 * de lettre.
	 */
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
	}

	/**
	 * Indique si un mur est présent dans la case à la position (x,y) sur le platau
	 * et dans la direction dir donnée.
	 * 
	 * @param x   Position x de la case concernée
	 * @param y   Position y de la case concernée
	 * @param dir Direction du test pour savoir si un mur est présent dans cette
	 *            case.
	 * @return Vrai si un mur est dans la direction donnée pour la case en position
	 *         (x,y) sur le plateau de jeu.
	 */
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

	/**
	 * Renvoi la position d'arrivée à partir d'une position de départ et dans une
	 * direction donnée.
	 * 
	 * @param start Position de départ du mouvement
	 * @param dir   Direction du mouvement
	 * @return Position d'arrivée du mouvement dans la direction dir et à partir de
	 *         la case en position start.
	 */
	public Position move(Position start, Direction dir) {
		Position p = new Position(start);
		while (!isWall(p.getX(), p.getY(), dir) && !isRobot(p, dir)) {
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

	/**
	 * Déplacement d'un robot sur le plateau de jeu dans la direction donnée. Le
	 * robot se déplace en ligne droite jusqu'au prochain obstacle (mur ou robot)
	 * dans la direction dir.
	 * 
	 * @param r   Robot déplacé sur le plateau.
	 * @param dir Direction du mouvement demandé au robot r sur le plateau de jeu.
	 */
	public void moveRobot(Robot r, Direction dir) {
		Position p = r.getPositionRobot();
		r.setPosition(move(p, dir));
		notifyListener();
	}

	/**
	 * Déplacement d'un robot sur le plateau de jeu à la position donnée. La
	 * position du robot donné est actualisée à la nouvelle position. Permet de
	 * déplacer quand le calcul du déplacement à déjà été fait.
	 * 
	 * @param r           Robot déplacé sur le plateau.
	 * @param newPosition Nouvelle position du robot déplacé.
	 */
	public void moveRobotToPosition(Robot r, Position newPosition) {
		Position p = r.getPositionRobot();
		if (isRobot(p))
			getRobot(p).setPosition(newPosition);
		notifyListener();
	}

	/**
	 * Indique si il est possible de bouger dans une direction donnée à partir d'un
	 * position. Pouvoir se déplacer signifie pouvoir se déplacer d'au moins une
	 * case dans la direction donnée. Permet d'éviter le calcul du déplacement
	 * complet.
	 * 
	 * @param p   Position de départ du mouvement étudié.
	 * @param dir Direction du mouvement pour la position p.
	 * @return Vrai si il est possible de se déplacer d'au moins une case dans la
	 *         direction dir à partir de la position p.
	 */
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

	/**
	 * Retourne la liste complète des position d'arrivée possible pour un robot
	 * donné à sa position acutelle sur le plateau de jeu. Les mouvements possibles
	 * d'un robot sont des déplacements d'au moins une case dans une des quatres
	 * directions cardinales. Les positions renvoyées sont les position d'arrivée du
	 * déplacement.
	 * 
	 * @param r Robot étudié pour trouver tous ses mouvements possible à partir de
	 *          sa position dans le plateau de jeu.
	 * @return Liste complète des déplacements d'au moins une case possibles pour le
	 *         robot r. Les positions sont les positions d'arrivée des mouvements.
	 */
	public ArrayList<Position> getAllMoves(Robot r) {
		ArrayList<Position> directions = new ArrayList<Position>();
		for (Direction d : Direction.values()) {
			if (canMoveInDir(r.getPositionRobot(), d))
				directions.add(move(r.getPositionRobot(), d));
		}
		return directions;
	}

	/**
	 * Retourne la liste complète des position d'arrivée possible pour une position
	 * donnée. Les mouvements possibles sont des déplacements d'au moins une case
	 * dans une des quatres directions cardinales. Les positions renvoyées sont les
	 * position d'arrivée du déplacement.
	 * 
	 * @param position Position étudiée pour trouver tous les mouvements possible à
	 *                 partir de cette position dans le plateau de jeu.
	 * @return Liste complète des déplacements d'au moins une case possibles. Les
	 *         positions sont les positions d'arrivée des mouvements.
	 */
	public ArrayList<Position> getAllMoves(Position position) {
		ArrayList<Position> directions = new ArrayList<Position>();
		for (Direction d : Direction.values()) {
			if (canMoveInDir(position, d))
				directions.add(move(position, d));
		}
		return directions;
	}

	/**
	 * Retourne l'objet case en position (x,y) sur le plateau de jeu.
	 * 
	 * @param x Position x de la case
	 * @param y Position y de la case
	 * @return Object Case en position (x,y) sur le plateau de jeu.
	 */
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

	/**
	 * Retourne le robot à la position donnée si il y en a un. Sinon retourne null.
	 * 
	 * @param p Position p sur le plateau de jeu.
	 * @return Robot à la position donnée si il y en a un, sinon null.
	 */
	public Robot getRobot(Position p) {
		for (Robot r : robots) {
			if (r.getPositionRobot().equals(p))
				return r;
		}
		return null;
	}

	/**
	 * Retourne le robot principal du jeu.
	 * 
	 * @return Le robot principal du jeu.
	 */
	public Robot getMainRobot() {
		if (mainRobot == null)
			takeRobot();
		return mainRobot;
	}

	/**
	 * Retourne la cible principale du jeu.
	 * 
	 * @return La cible principale du jeu.
	 */
	public Goal getMainGoal() {
		if (mainGoal == null)
			takeGoal();
		return mainGoal;
	}

	/**
	 * Retourne la description textuelle de la liste des entités présentes à la case
	 * en position (x,y).
	 * 
	 * @param x Position x de la case
	 * @param y Position y de la case.
	 * @return La description textuelle de la liste des entités présentes à la case
	 *         en position (x,y).
	 */
	public String getEntities(int x, int y) {
		return getEntities(new Position(x, y));
	}

	/**
	 * Retourne la description textuelle de la liste des entités présentes à la case
	 * en position p.
	 * 
	 * @param p Position de la case.
	 * @return La description textuelle de la liste des entités présentes à la case
	 *         en position p.
	 */
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
	 * Affectation d'un nouveau robot principal pour la partie.
	 * 
	 * @param r Nouveau robot principal pour la partie.
	 */
	public void setMainRobot(Robot r) {
		mainRobot = r;
		notifyListener();
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

	/**
	 * Distance de Manhattan entre le robot donné et la cible principale
	 * 
	 * @param r Robot position de départ
	 * @return Distance de Manhattan entre le robot donné et la cible principale
	 */
	public int distanceManhattan(Position r) {
		Position g = mainGoal.getPositionGoal();
		return distanceManhattan(r, g);
	}

	/**
	 * Distance de Manhattan entre deux positions sur le plateau de jeu
	 * 
	 * @param r Position de départ
	 * @param g Position d'arrivée
	 * @return Distance de Manhattan entre deux positions sur le plateau de jeu
	 */
	public int distanceManhattan(Position r, Position g) {
		return Math.abs(r.getX() - g.getX()) + Math.abs(r.getY() - g.getY());
	}

	/**
	 * Indique si le robot principal et la cible principale se trouvent sur la même
	 * ligne ou colonne sans obstacles entre eux.
	 * 
	 * @return Vrai si le robot principal et la cible principale se trouvent sur la
	 *         même ligne ou colonne et sans obstacles entre eux.
	 */
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

	/**
	 * Écriture du plateau tel qu'il est à l'instant dans un fichier sous le nom
	 * filename. Permet de sauvegarder la progression d'un niveau.
	 * 
	 * @param filename Nom de fichier pour l'enregistrement de plateau.
	 */
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

	/**
	 * Chargement du plateau de jeu contenu dans le fichier sous le nom de filename
	 * donné dans le constructeur. Le plataeu est réinitialisé par rapport au
	 * fichier donné. La séralisation des données permet d'enregistrer des parties
	 * pour ensuite les récupérer. Le fichier et composé de lignes sur lesquelles
	 * sont décomposées les informations. La strucuture est la suivante : coordX
	 * coordY N S E W pour mettre les quatres murs à la case en position coordX et
	 * coordY. Puis les entités sont sous forme de lettre avec R pour robots et G
	 * pour les goals (cibles). L'ordre des lignes n'a pas d'importance.
	 */
	public void loadBoard() {
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
