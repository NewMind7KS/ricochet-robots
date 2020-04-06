package ricochet.modele;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class Solver implements Runnable {
	
	/** Compteur de noeuds parcourus */
	private static long compteurNode = 0;
	/** Liste fermée */
	private ArrayList<Node> closedList;
	/** Liste ouverte */
	private ArrayList<Node> openList;
	/** Un plateau de jeu */
	private Board board;
	/** Un array list contenant un hashmap listant toutes les possibilités de solution */
	private ArrayList<HashMap<Position, Integer>> allPossibilities = new ArrayList<HashMap<Position, Integer>>();

	/**
	 * Constructeur du solver. Initialise une liste ouverte et une liste fermée
	 * @param board
	 */
	public Solver(Board board) {
		compteurNode = 0;
		this.board = board;
		closedList = new ArrayList<Node>();
		openList = new ArrayList<Node>();
	}
	
	/**
	 * Lance l'algorithme A*
	 */
	@Override
	public void run() {
		astar();
	}

	/**
	 * Lancement de la résolution de A*
	 */
	public void solve() {
		System.out.println("Lancement de A start une première fois");
		ArrayList<Position> ast = astar();
		if (ast != null) {
			System.out.println(ast);
			return;
		}
		System.out.println("Pas de chemin direct, utilisation des autres robots");
		addMoves(2, null);
		int min = 40;
		HashMap<Position, Integer> tmp = null;
		for (HashMap<Position, Integer> map : allPossibilities) {
			if (map.size() < min) {
				min = map.size();
				tmp = map;
			}
		}
		System.out.println("Meilleur chemin trouvé : (! Mouvements pas dans l'ordre !)");
		System.out.println(tmp);
	}

	/**
	 * Algorithme qui permet de résoudre Ricochet Robot suivant l'algorithme A*. 
	 * @return
	 */
	public ArrayList<Position> astar() {
		
		long begin = System.nanoTime();
		
		closedList.clear();
		openList.clear();

		// Position de départ des mouvements et position cible
		Position startPosition = board.getMainRobot().getPositionRobot();
		Position arrivee = board.getMainGoal().getPositionGoal();

		// Création du noeud de base
		Node start = new Node(startPosition.getX(), startPosition.getY(), 0, board.distanceManhattan(), null);
		openList.add(start);

		boolean insert = false;
		// Tant qu'un chemin est possible, on itère
		while (!openList.isEmpty()) {
			compteurNode++;
			// Récupération duy noeud avec la valeur la plus basse
			Node minNode = Collections.min(openList);

			// Effectue le déplacement du robot lié à la position du noeud
			board.moveRobotToPosition(board.getMainRobot(), minNode.getPosition());

			// Si le noeud correspond à l'arrivée : algorithme terminé, reconstitution du
			// chemin
			if (minNode.getPosition().equals(arrivee)) {
				ArrayList<Position> chemin = new ArrayList<Position>();
				while (minNode.ancestor != null) {
					chemin.add(minNode.getPosition());
					minNode = minNode.ancestor;
				}
				board.moveRobotToPosition(board.getMainRobot(), startPosition);
				System.out.println("----------- Optimal Solution ------------");
				System.out.println("Noeuds parcourus : \t\t" + compteurNode);
				System.out.println(chemin);
				long end = System.nanoTime() - begin;
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(3);
				System.out.println("Temps execution : \t\t" + df.format(end / 1e9) + "s");
				return chemin;
			}
			// Si ce n'est pas l'arrivée, on ajoute tous les enfants noeuds dans openList si
			// ils ne sont pas dans closedList ou s'ils n'existent pas dans openList avec
			// une valeur inférieure.
			for (Position move : board.getAllMoves(minNode.getPosition())) {
				// Création du nouveau noeud, sa position est la position d'arrivée après
				// mouvement. Le cout d'un noeud est la distance de Manhattan entre son
				// prédécesseur et lui-même. Quand à l'heuristique du noeud, c'est sa distance
				// de Manhattan avec la cible principale.
				Node nodeMove = new Node(move, board.distanceManhattan(minNode.getPosition(), move),
						board.distanceManhattan(move), minNode);
				insert = false;
				// Si le nodeMove à une valeur plus basse que le même noeud dans closed, on peut
				// l'ajouter à open
				for (Node n : closedList) {
					if (n.x == nodeMove.x && n.y == nodeMove.y) {
						insert = true;
						break;
					}
				}
				Node nodeMax = null;
				for (Node n : openList) {
					if (n.x == nodeMove.x && n.y == nodeMove.y && nodeMove.value() > n.value()) {
						insert = true;
						nodeMax = n;
						break;
					}
				}
				if (!insert) {
					openList.add(nodeMove);
				}
				if (nodeMax != null) {
					openList.remove(nodeMax);
				}
			}
			openList.remove(minNode);
			closedList.add(minNode);
		}
		board.moveRobotToPosition(board.getMainRobot(), startPosition);
		System.out.println("----------- No solution founded ------------");
		long end = System.nanoTime() - begin;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		System.out.println("Noeuds parcourus : \t\t" + compteurNode);
		System.out.println("Temps execution : \t\t" + df.format(end / 1e9) + "s");
		System.out.println("Pas de chemin trouvé avec A*");
		return null;
	}

	/**
	 * Méthode récursive qui permet d'ajouter les différents mouvements possibles d'une solution.
	 * @param c profondeur
	 * @param path Le chemin déjà parcouru 
	 */
	public void addMoves(int c, HashMap<Position, Integer> path) {
		HashMap<Position, Robot> moves = new HashMap<Position, Robot>();
		for (Robot r : board.getRobots()) {
			if (r == board.getMainRobot())
				continue;
			for (Position p : board.getAllMoves(r)) {
				moves.put(p, r);
			}
		}
		for (Entry<Position, Robot> entry : moves.entrySet()) {
			if (path == null) {
				path = new HashMap<Position, Integer>();
			}
			path.put(entry.getKey(), entry.getValue().getId());

			Position oldPosition = entry.getValue().getPositionRobot();
			board.moveRobotToPosition(entry.getValue(), entry.getKey());
			if (astar() != null) {
				ArrayList<Position> solution = astar();
				for (Position p : solution) {
					path.put(p, board.getMainRobot().getId());
				}
				allPossibilities.add(new HashMap<Position, Integer>(path));
				for (Position p : solution) {
					path.remove(p, board.getMainRobot().getId());
				}
				path.remove(entry.getKey());
				board.moveRobotToPosition(entry.getValue(), oldPosition);
			} else {
				path.remove(entry.getKey());
				if (c != 0)
					addMoves(c - 1, path);
				board.moveRobotToPosition(entry.getValue(), oldPosition);
			}
		}
	}

	/**
	 * Classe interne représentant un noeud.
	 */
	class Node implements Comparable<Node> {

		/** Position x */
		private short x;
		/** Position y */
		private short y;
		/** Cout de se rendre sur ce noeud depuis son ancêtre */
		private int cout;
		/** coût heuristique(distance entre le noeud et la position de la cible) */
		private int heuristique;
		/** Noeud ancêtre */
		private Node ancestor;

		/**
		 * Noeud de l'algorithme A*, un Noeud est une future position sur le plateau de
		 * Jeu pour le robot principal. Pour se rendre à ce noeud à partir de la
		 * position actuelle, c'est sa distance qui le sépare de son ancêtre.
		 * L'heuristique est la distance de cette position à l'emplacement de la cible
		 * principale sur le plateau.
		 * 
		 * @param x           Position x
		 * @param y           Position y
		 * @param cout        Cout de se rendre sur ce noeud depuis son ancêtre
		 *                    (distance)
		 * @param heuristique Distance entre ce noeud et la position de la cible sur le
		 *                    plateau
		 * @param ancestor    Ancètre qui à permis d'accéder à ce noeud.
		 */
		public Node(short x, short y, int cout, int heuristique, Node ancestor) {
			this.x = x;
			this.y = y;
			this.cout = cout;
			this.heuristique = heuristique;
			this.ancestor = ancestor;
		}

		/**
		 * Retourne un noeud et ses paramètres
		 */
		public Node(Position p, int cout, int heuristique, Node ancestor) {
			this(p.getX(), p.getY(), cout, heuristique, ancestor);
		}

		public Position getPosition() {
			return new Position(x, y);
		}

		/**
		 * La valeur d'un Noeud est la sommme des couts qui ont permis de l'atteindre
		 * plus son heuristique.
		 * 
		 * @return Valeur du Noeud courant
		 */
		public int value() {
			int value = 0;
			Node tmp = this;
			while (tmp.ancestor != null) {
				value += cout;
				tmp = tmp.ancestor;
			}
			return value + heuristique;
		}

		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", cout=" + cout + ", heuristique=" + heuristique + ", ancestor="
					+ ancestor + ", value()=" + value() + "]";
		}

		/**
		 * Comparaison entre deux Noeuds, c'est la valeur du Noeud qui fait office de
		 * comparateur.
		 * 
		 * @param n Noeud à comparer
		 * @return 0 Si noeuds égaux, -1 si n est plus petit et >0 si plus grand.
		 */
		@Override
		public int compareTo(Node n) {
			int value = value();
			int valueN = n.value();
			if (valueN == value)
				return 0;
			else if (value > valueN)
				return value - valueN;
			else
				return -1;
		}
	}
}