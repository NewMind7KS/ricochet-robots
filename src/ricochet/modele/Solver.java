package ricochet.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class Solver {

	private ArrayList<Node> closedList;
	private ArrayList<Node> openList;
	private Board board;
	private static final Logger logger = Logger.getLogger("solve.log");

	public Solver(Board board) {
		this.board = board;
		closedList = new ArrayList<Node>();
		openList = new ArrayList<Node>();
	}

	public ArrayList<Position> solve() {
		logger.info("Démarrage de la résolution du jeu");
		closedList.clear();
		openList.clear();

		// Position de départ des mouvements et position cible
		Position startPosition = board.getMainRobot().getPositionRobot();
		Position arrivee = board.getMainGoal().getPositionGoal();
		logger.info("\nPosition de départ : " + startPosition + "\nPosition d'arrivée : " + arrivee);

		// Création du noeud de base
		Node start = new Node(startPosition.getX(), startPosition.getY(), board.distanceManhattan(), 0, null);
		openList.add(start);

		boolean insert = false;
		// Tant qu'un chemin est possible, on itère
		while (!openList.isEmpty()) {
			// Récupération duy noeud avec la valeur la plus basse
			Node minNode = Collections.min(openList);
//			logger.info("Noeud min de openList : " + minNode);

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
				System.out.println(chemin);
				return chemin;
			}
			// Si ce n'est pas l'arrivée, on ajoute tous les enfants noeuds dans openList si
			// ils ne sont pas dans closedList ou s'ils n'existent pas dans openList avec
			// une valeur inférieure.
			for (Position move : board.getAllMoves(minNode.getPosition())) {
				// Vérification non retour en arrière
				if (move.equals(minNode.getPosition())) {
					continue;
				}
//				logger.info("Étude de la position : " + move);
				// Création du nouveau noeud, sa position est la position d'arrivée après
				// mouvement. Le cout d'un noeud est la distance de Manhattan entre son
				// prédécesseur et lui-même. Quand à l'heuristique du noeud, c'est sa distance
				// de Manhattan avec la cible principale.
				Node nodeMove = new Node(move, board.distanceManhattan(minNode.getPosition(), move),
						board.distanceManhattan(move), minNode);
//				logger.info("Création du noeud associé : " + nodeMove);

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
					if (n.x == nodeMove.x && n.y == nodeMove.y && nodeMove.value() < n.value()) {
						insert = true;
						nodeMax = n;
						break;
					}
				}
				if (!insert)
					openList.add(nodeMove);
				if (nodeMax != null) {
					openList.remove(nodeMax);
				}
			}
			openList.remove(minNode);
			closedList.add(minNode);
		}
		logger.warning("Solve error, no path to complete");
		return null;
	}

	class Node implements Comparable<Node> {

		private int x;
		private int y;
		private int cout;
		private int heuristique;
		private Node ancestor;

		public Node(int x, int y, int cout, int heuristique, Node ancestor) {
			this.x = x;
			this.y = y;
			this.cout = cout;
			this.heuristique = heuristique;
			this.ancestor = ancestor;
		}

		public Node(Position p, int cout, int heuristique, Node ancestor) {
			this(p.getX(), p.getY(), cout, heuristique, ancestor);
		}

		public Position getPosition() {
			return new Position(x, y);
		}

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
