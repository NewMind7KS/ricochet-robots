package ricochet.modele;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {

	private ArrayList<Node> closedList;
	private ArrayList<Node> openList;
	private Board board;

	public Solver(Board board) {
		this.board = board;
		closedList = new ArrayList<Node>();
		openList = new ArrayList<Node>();
		Position startPosition = board.getMainRobot().getPositionRobot();
		Node start = new Node(startPosition.getX(), startPosition.getY(), board.distanceManhattan(), 0, null);
		openList.add(start);
	}

	public ArrayList<Position> solve() {
		ArrayList<Position> chemin = new ArrayList<Position>();
		Position arrivee = board.getMainGoal().getPositionGoal();
		boolean b = false;
		while (!openList.isEmpty()) {
//			System.out.println("\nOpen list " + openList);
//			System.out.println("Closed list " + closedList);
			Node f = Collections.min(openList);
//			System.out.println("Min Node Open : " + f);
			board.moveRobotToPosition(board.getMainRobot(), f.getPosition());
			System.out.println("Position du robot sur le plateau " + board.getMainRobot().getPositionRobot());
//			board.printBoard();
			if (f.getPosition().equals(arrivee)) {
				while (f.ancestor != null) {
					chemin.add(f.getPosition());
					f = f.ancestor;
					System.out.println(chemin);
				}
				return chemin;
			} else {
				for (Position v : board.getAllMoves(f.getPosition())) {
					System.out.println("\nEtude de la position " + v);
					if (v.equals(f.getPosition())) {
						continue;
					}
					Node vo = new Node(v, board.distanceManhattan(f.getPosition(), v), board.distanceManhattan(v), f);
//					System.out.println("Création du noeud pour openList : " + vo);
					b = false;
					Node tmp = null;
					for (Node cl : closedList) {
						if (cl.x == vo.x && cl.y == vo.y && vo.value() < cl.value()) {
							b = true;
							tmp = cl;
						}
					}
					if (b)
						closedList.remove(tmp);
					if (!b) {
						for (Node cl : openList) {
							if (cl.x == vo.x && cl.y == vo.y && vo.value() < cl.value()) {
								b = true;
								tmp = cl;
							}
						}
						if (b)
							openList.remove(tmp);
					}
					if (!b) {
						openList.add(vo);
					}
				}
				openList.remove(f);
				closedList.add(f);
			}
		}
		System.err.println("Solve error");
		return null;
	}

	@Override
	public String toString() {
		return "Solver [closedList=" + closedList + ", openList=" + openList + ", board=" + board + "]";
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
