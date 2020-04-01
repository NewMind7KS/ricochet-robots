package ricochet.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Heuristique : h(n) est une fonction d'estimation du cout restant entre un
 * noeud n d'un graphe et le but
 */

/**
 * Open : contient les noeuds pas encore tratiés : cad la frontière de la partie
 * du grphe explorée jusqu'à maintenant. Les noeuds sont triées selon l'estimé
 * f(n) la fonction d'évaluation. On explore les noeuds les plus prometteurs en
 * premier.
 */

/**
 * Closed contient les noedus déjà traités ; cad à l'intérieur de la frontière
 * délimitée par Open
 */

/**
 * f(n) la fonction d'évaluation est un nombre réeel positif ou nul, estimant le
 * cout du meileur chemin du noeud initial passant par n et arrivant au but.
 * Cette fonction tente d'estimer le cout du chemin optimal entre le noeud
 * initail et le but en passant par n. f() se décompose en deux parties : g(n)
 * le cout du meilleur chemin ayant mené au noeud n depuis le noeud initial. Et
 * h(n) le cout estimé du reste du chemin optimal partant de n jusqu'au but.
 * C'est la fonction heuristique. h(n) = 0 si le noeud n est le but.
 */

/**
 * Ici la fonction heuristique est la somme des distances de manhattan des
 * robots à la cible et permettant d'avoir une heuristique à zéro si le robot
 * principal est sur la cible principale.
 */

/**
 * Algo : déclarer deux noeud n et n' : n est le noeud courant et n' un noeud
 * enfant déclaration de deux listes open et closed insertion du noeud initial
 * donné en paramètre tant que vrai: si open est vide, sortir de la boucle avec
 * erreur -> aucune facon de se rendre au noeud but n = noeud au début de open :
 * celui au début de open enlever n de open et l'ajouter dans closed si n
 * correspond au but alors on sort de la boucle et on retourne le chemin qui
 * nous a mené à ce noeud (il faut donc les parents des parents sinon pour
 * chaque noeud enfant n' de n appartenant à transitions(n) on initialisation la
 * valeur de g(n') à g(n) + c(n,n') -> somme des couts jusqu'au noeud n' mettre
 * le parent de n'à n si closed ou open contient n'' égal à n' avec f(n') <=
 * f(n'') -> noeud plus prometteur alors insérer n'' de closed ou open et
 * insérer n' dans open fin si si n' n'est pas dans open ou closed alors insérer
 * n' dans open avec ordre croissant fin si fn pour fin tant que
 * 
 */
public class Solve implements Runnable {

	private Board board;
	private ArrayList<Node> open;
	private ArrayList<Node> closed;

	public Solve(Board board) {
		this.board = board;
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
	}

	@Override
	public void run() {
		Node noeudInitial = new Node(board.getMainRobot().getPositionRobot(), board.getMainRobot(), null);
		for (Node s : aStar(noeudInitial)) {
			System.out.println(s);
		}
	}

	public LinkedList<Node> aStar(Node noeudInitial) {
		open.add(noeudInitial);

		while (true) {
			if (open.isEmpty()) {
				System.err.println("Pas de chemin trouvé");
				return new LinkedList<Node>();
			}
			Node n = Collections.min(open);
			board.moveRobotToPosition(n.robotMoved, n.p);
			System.out.println("Noeud min : " + n);
			open.remove(n);
			closed.add(n);

			if (board.isFinished())
				return n.path();
			for (Node child : transitions(n)) {
				if (!addOpenOrClosedMinor(child)) {
					open.add(child);
				}
			}
		}
	}

	public boolean addOpenOrClosedMinor(Node n) {
		boolean in = false;
		boolean inListOpenOrClosed = false;
		Node tmp = null;
		for (Node o : closed) {
			if (n.p.equals(o.p) && n.robotMoved == o.robotMoved) {
				inListOpenOrClosed = true;
				if (n.f() <= o.f()) {
					in = true;
					tmp = o;
					break;
				}
			}
		}
		if (in) {
			closed.add(n);
			return inListOpenOrClosed;
		}
		
		for (Node o : open) {
			if (n.p.equals(o.p) && n.robotMoved == o.robotMoved) {
				inListOpenOrClosed = true;
				if (n.f() <= o.f()) {
					in = true;
					tmp = o;
					break;
				}
			}
		}
		if (in) {
			open.remove(tmp);
			open.add(n);
			return inListOpenOrClosed;
		}
		return inListOpenOrClosed;
	}

	public LinkedList<Node> transitions(Node n) {
		LinkedList<Node> transitions = new LinkedList<Node>();
		for (Robot r : board.getOrderedRobots()) {
			if (r == board.getMainRobot()) {
				for (Position p : board.getAllMoves(r)) {
					Position oldPosition = new Position(r.getPositionRobot());
					board.moveRobotToPosition(r, p);
					Node move = new Node(p, r, n);
					board.moveRobotToPosition(r, oldPosition);
					transitions.add(move);
				}
			} else {
				for (Position p : board.getAllMoves(r)) {
					Node move = new Node(p, r, n);
					transitions.add(move);
				}
			}
		}

		return transitions;
	}

	class Node implements Comparable<Node> {

		private Position p;
		private Robot robotMoved;
		private Node ancestor;
		private int heuristique;
		private int cout;

		public Node(Position p, Robot r, Node ancestor) {
			this.p = p;
			this.robotMoved = r;
			this.ancestor = ancestor;
			this.heuristique = h();
			if (ancestor == null)
				this.cout = 0;
			else
				this.cout = board.distanceManhattan(p, ancestor.p);
		}

		public boolean isMainRobot() {
			return robotMoved == board.getMainRobot();
		}

		public boolean isMainGoal() {
			return robotMoved.getPositionRobot() == board.getMainGoal().getPositionGoal();
		}

		/**
		 * Fonction heuristique : Somme des distances de Manhattan entre la position des
		 * robots et la cible principale.
		 * 
		 * @return Distance de Manhattan entre la position du noeud et la cible
		 *         principale.
		 */
		public int h() {
			return board.heuristique();
		}

		/**
		 * Fonction d'évaluation du noeud, c'est la somme du chemin qui à permis
		 * d'arriver jusqu'au noeud plus son heuristique.
		 * 
		 * @return
		 */
		public int f() {
			return cout + heuristique;
		}

		public LinkedList<Node> path() {
			LinkedList<Node> path = new LinkedList<Node>();
			Node tmp = this;
			while (tmp.ancestor != null) {
				path.addFirst(tmp);
				tmp = tmp.ancestor;
			}
			return path;
		}

		public String toString() {
			String str = "";
			str += "Robot " + robotMoved.getId() + " déplacé en " + p + " objective : " + f();
			return str;
		}

		@Override
		public int compareTo(Node nodeCompared) {
			int value = f();
			int valueN = nodeCompared.f();
			if (valueN == value)
				if (isMainRobot())
					return -1;
				else
					return 0;
			else if (value > valueN)
				return value - valueN;
			else
				return -1;
		}

		private Solve getEnclosingInstance() {
			return Solve.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((p == null) ? 0 : p.hashCode());
			result = prime * result + ((robotMoved == null) ? 0 : robotMoved.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (p == null) {
				if (other.p != null)
					return false;
			} else if (!p.equals(other.p))
				return false;
			if (robotMoved == null) {
				if (other.robotMoved != null)
					return false;
			} else if (!robotMoved.equals(other.robotMoved))
				return false;
			return true;
		}
	}

}
