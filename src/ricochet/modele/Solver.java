package ricochet.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class Solver {

	private ArrayList<Node> closedList;
	private ArrayList<Node> openList;
	private Board board;
	private static final Logger logger = Logger.getLogger("solve.log");
	private ArrayList<ArrayList<Position> > allPossibilities= new ArrayList<ArrayList<Position> >();

	public Solver(Board board) {
		this.board = board;
		closedList = new ArrayList<Node>();
		openList = new ArrayList<Node>();
	}

	public ArrayList<Position> solve() {
		ArrayList<Position> ast = astar();
		if (ast != null) {
			return ast;
		}
		addMoves(5);
		return null;
	}

	public ArrayList<Position> astar() {
		logger.info("Démarrage de la résolution du jeu");
		closedList.clear();
		openList.clear();

		// Position de départ des mouvements et position cible
		Position startPosition = board.getMainRobot().getPositionRobot();
		Position arrivee = board.getMainGoal().getPositionGoal();
		logger.info("\nPosition de départ : " + startPosition + "\nPosition d'arrivée : " + arrivee);

		// Création du noeud de base
		Node start = new Node(startPosition.getX(), startPosition.getY(), 0, board.distanceManhattan(), null);
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
				System.out.println("Chemin : " + chemin);
				board.moveRobotToPosition(board.getMainRobot(), startPosition);
				return chemin;
			}
			// Si ce n'est pas l'arrivée, on ajoute tous les enfants noeuds dans openList si
			// ils ne sont pas dans closedList ou s'ils n'existent pas dans openList avec
			// une valeur inférieure.
			for (Position move : board.getAllMoves(minNode.getPosition())) {
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
		/*
		 * A ce moment, il n'y a pas de chemin direct pour le robot pour se rendre sur
		 * la cible. Il faut don cutiliser les autres robots. Mais utiliser les autres
		 * robots : - Faire bouger le plus proche de la cible ? Et relancer a* -
		 * Ajouterdans l'algo toutes les positions possibles de tous les robots (donc
		 * revoir l'évaluation des noeuds) - Jouer un robot au hasard puis relancer a* :
		 * mauvaise idée - Faire bouger un robot proche de celui du joueur ?
		 */
		logger.warning("Solve error, no direct path to complete");
		board.moveRobotToPosition(board.getMainRobot(), startPosition);
		return null;
	}

	public void addMoves(int c) {
		HashMap<Position, Robot> moves = new HashMap<Position, Robot>();
		for (Robot r : board.getRobots()) {
			if (r == board.getMainRobot())
				continue;
			for (Position p : board.getAllMoves(r)) {
				moves.put(p, r);
			}
		}
		for (Entry<Position, Robot> entry : moves.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
			Position oldPosition = entry.getValue().getPositionRobot();
			board.moveRobotToPosition(entry.getValue(), entry.getKey());
			if (astar() != null) {
				System.err.println("Solution trouvée");
				//ajout solution
				ArrayList<Position> possibility = astar();
				allPossibilities.add(possibility);
				//taille de allPossi
				System.out.println("Taille allPossi : " + allPossibilities.size());
				//taille de chaque élement dans allPossi
				for (int i = 0; i < allPossibilities.size(); i++){
					System.out.println("Taille de " + i + " élément de allPossi : " + allPossibilities.get(i).size()+" ");
				}
				//affichages diverses
				System.out.println("affichage astar()/possibility"+possibility);
				System.out.println("----BEST----");
				int sizeList= 50;
				int index = 0;
				//choisi la taille minimale
				for (int i = 0; i < allPossibilities.size(); i++){
					if (allPossibilities.get(i).size() <  sizeList){
						index = i;
						sizeList = allPossibilities.get(i).size();
					}
				}
				//affichage best
				System.out.println("VOICI LE MEILLEUR "+ index +" de taille " +sizeList+": " + allPossibilities.get(index));
				return;
			} else {
				if (c != 0)
					addMoves(c - 1);
				board.moveRobotToPosition(entry.getValue(), oldPosition);
			}
		}
	}

	class Node implements Comparable<Node> {

		private int x;
		private int y;
		private int cout;
		private int heuristique;
		private Node ancestor;

		/**
		 * Noeud de l'algorithme A*, un Noeud est une future position sur le plateau de
		 * Jeu pour le robot principal. Pour se rendre à ce noeud à partir de la
		 * position actuelle, c'est sa distance qui le sépare de son ancètre.
		 * L'heuristique est la distance de cette position à l'emplacement de la cible
		 * principale sur le plateau.
		 * 
		 * @param x           Position x
		 * @param y           Position y
		 * @param cout        Cout de se rendre sur ce noeud depuis son ancètre
		 *                    (distance)
		 * @param heuristique Distance entre ce noeud et la position de la cible sur le
		 *                    plateau
		 * @param ancestor    Ancètre qui à permis d'accéder à ce noeud.
		 */
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
