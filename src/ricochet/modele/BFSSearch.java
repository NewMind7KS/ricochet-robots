package ricochet.modele;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Breadth First Search algorithme Test de toutes les combinaisons possible à
 * partir de l'état initial du plateau de jeu. Il est garanti de trouver une
 * solution au problème. Mais gourmand en ressource et en temps suivant la
 * profondeur de recherche. Chaque noeud possède 2 ou 3 mouvements possibles. Ce
 * type de recherche permet de donner à coup sûr le chemin le plus court pour se
 * rendre sur la cible. La première solution trouvée est forcément le chemin le
 * plus court. La recherche s'effectue à partir du root (état initial) puis tous
 * ses "children" sont testés. Si aucun ne termine le jeu, les enfants de chaque
 * "children" sont générés puis testés. Le temps de résolution est proportionnel
 * à la complexité de la solution (le nombre de mouvements requis).
 */
public class BFSSearch implements Runnable {

	/** Compteur de noeuds parcourus */
	private static long compteurNode = 0;

	/** Plateau de jeu initial */
	private Board board;

	private HashMap<Robot, Position> originalPosition = new HashMap<Robot, Position>();

	/** Profondeur maximale de recherche */
	public byte profondeur = 6;

	/**
	 * Constructeur de la recherche suivant un plateau initial.
	 * 
	 * @param b Plateau de base du jeu.
	 */
	public BFSSearch(Board b) {
		this.board = b;
		compteurNode = 0;
		for (Robot r : board.getRobots()) {
			originalPosition.put(r, new Position(r.getPositionRobot()));
		}
	}

	/**
	 * Méthode lancée depuis l'appel d'un Thread
	 */
	@Override
	public void run() {
		search();
	}

	/**
	 * Résolution du jeu. Le robot qui doit atteindre la cible est le mainRobot du
	 * board. De même la cible est la mainGoal du Board. L'état initial donne toutes
	 * les possibilités pour les profondeurs de recherche suivantes. La recherche
	 * s'arrête à la profondeur maximum en attribut. Sur chaque profondeur, tous les
	 * états sont testés pour déterminer si la partie est terminée. Chaque état est
	 * une classe interne State.
	 * 
	 * @return Liste des mouvements pour arriver au but
	 */
	public LinkedList<Move> search() {
		System.out.println("Démarrage résolution pour robot : " + board.getMainRobot().getName());
		System.out.println("Cible principale : " + board.getMainGoal().getName());
		byte prof = profondeur;
		long begin = System.nanoTime();
		boolean finished = false;
		State root = new State();
		ArrayList<State> nodes = new ArrayList<State>();
		nodes.add(root);
		while (!finished) {
			if (prof-- == 0) {
				System.out.println("Pas de chemin pour profondeur " + profondeur);
				break;
			}
			ArrayList<State> childs = new ArrayList<State>();
			for (State s : nodes) {
				try {
					childs.addAll(s.childs());
				} catch(java.lang.OutOfMemoryError e) {
					System.err.println("Error : not enought memory");
					return new LinkedList<Move>();
				}
			}
			System.out.println("Deep : " + (profondeur - prof) + "\t\tChilds :" + childs.size());
			for (State sprime : childs) {
				compteurNode++;
				if (sprime.isFinished()) {
					System.out.println("----------- Optimal Solution ------------");
					System.out.println(sprime.getPath());
					System.out.println("-----------------------------------------");
					System.out.println("Noeuds parcourus : \t\t" + compteurNode);
					finished = true;
					long end = System.nanoTime() - begin;
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(3);
					System.out.println("Temps execution : \t\t" + df.format(end / 1e9) + "s");
					return sprime.getPath();
				}
			}
			nodes.clear();
			nodes = childs;
		}
		long end = System.nanoTime() - begin;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		System.out.println("Noeuds parcourus : \t\t" + compteurNode);
		System.out.println("Temps execution : \t\t" + df.format(end / 1e9) + "s");
		return new LinkedList<Move>();
	}

	/**
	 * Classe représentant un état du jeu. Le board de base en attribut de la classe
	 * BFSSearch sert de terrain pour bouger les robots. Chaque état possède une
	 * direction de mouvement pour un robot donné ainsi qu'un State qui l'a généré.
	 * Ce principe permet de consommer peu de mémoire en ne stockant pas le board
	 * entier modifié, mais plus gourmand en ressources de calcul.
	 */
	class State {

		/** Mouvement supplémentaire par rapport aux mouvements des ancètres du State */
		private Direction currentMove;

		/** Robot concerné par le mouvement */
		private Robot robotMoved;

		/** State parent générateur */
		private State ancestor;

		/** Profondeur de recherche actuelle */
		private byte deep;

		/**
		 * Construction du State initial. Aucun parent et aucun mouvement en cours.
		 */
		public State() {
			this.currentMove = null;
			this.robotMoved = board.getMainRobot();
			this.ancestor = null;
			this.deep = 0;
		}

		/**
		 * Construction d'un State (état) généré par un State parent. Il contient une
		 * action à réaliser sur le plateau représentée par le currentMove et le
		 * robotMoved concerné. Le coup joué sur ce State est supplémentaire à ceux
		 * joués par ses ancètres. Il faut remonter le chemin des parents pour jouer
		 * chaque coup de jeu et arriver à l'état du plateau demandé.
		 * 
		 * @param move     Direction du mouvement pour le robot concerné
		 * @param r        Robot concerné par le mouvement dans une direction donnée
		 * @param ancestor State parent générateur de l'état actuel
		 */
		public State(Direction move, Robot r, State ancestor) {
			this.currentMove = move;
			this.robotMoved = r;
			this.ancestor = ancestor;
			this.deep = (byte) (this.ancestor.deep + 1);
		}

		/**
		 * Méthode permettant de jouer tous les coups depuis le premier noeud parent en
		 * suivant le chemin parcouru pour arriver à l'état actuel. Chaque état du
		 * chemin comporte un robot à jouer et une direction pour ce dernier.
		 */
		public void playPath() {
			LinkedList<Move> path = getPath();
			for (Move m : path) {
				m.r.setPosition(board.move(m.r.getPositionRobot(), m.dir));
			}
		}

		/**
		 * Réinitialisation de la position de tous les robots.
		 */
		public void playInvertPath() {
			for (Robot r : board.getRobots()) {
				r.setPosition(originalPosition.get(r));
			}
		}

		/**
		 * Indique si pour l'état courant State, le jeu est considéré comme terminé.
		 * C'est à dire que le robot principal est positionné sur la cible principale.
		 * Pour arriver à l'état du jeu concerné, la méthode joue tous les coups
		 * permettant d'y arriver, test si le jeu est terminé, et enfin rejoue les coups
		 * en arrière.
		 * 
		 * @return Indique si pour l'état courant State, le jeu est considéré comme
		 *         terminé.
		 */
		public boolean isFinished() {
			playPath();
			boolean finish = board.isFinished();
			playInvertPath();
			return finish;
		}

		/**
		 * Renvoi une liste de State générés par l'état actuel du jeu. Tous les coups
		 * sont joués pour arriver au state actuel. Puis pour chaque robot présent sur
		 * le plateau et pour chaque direction, on teste si il lui est possible de se
		 * déplacer dans cette direction. Si oui, un nouveau State est crée avec pour
		 * générateur l'état présent. Enfin, la position de tous les robots est
		 * réinitialisée en jouant les coups inverses.
		 * 
		 * @return Renvoi une liste de State générés par l'état actuel du jeu.
		 */
		public ArrayList<State> childs() {
			playPath();
			ArrayList<State> childs = new ArrayList<State>();
			for (Robot r : board.getRobots()) {
				for (Direction d : Direction.values()) {
					if (ancestor != null && ancestor.currentMove != null && ancestor.robotMoved == r
							&& ancestor.currentMove == Direction.opposite(d))
						continue;
					if (board.canMoveInDir(r.getPositionRobot(), d)) {
						State s = new State(d, r, this);
						childs.add(s);
					}
				}
			}
			playInvertPath();
			return childs;
		}

		/**
		 * Liste chainée de Move, ce sont des mouvements pour le jeu représentés par un
		 * robot concerné et une direction donnée depuis le chemin des ancètres de
		 * l'état actuel.
		 * 
		 * @return Liste chainée de Move, mouvements effectuées depuis la racine
		 *         jusqu'au noeud actuel.
		 */
		public LinkedList<Move> getPath() {
			LinkedList<Move> path = new LinkedList<Move>();
			State tmp = this;
			while (tmp.ancestor != null) {
				path.addFirst(new Move(tmp.robotMoved, tmp.currentMove));
				tmp = tmp.ancestor;
			}
			return path;
		}

		public String toString() {
			return currentMove + " " + robotMoved + " " + deep + " " + isFinished() + getPath();
		}
	}

	/**
	 * Mouvement effectué sur le plateau représenté par un robot concerné et une
	 * direction.
	 */
	class Move {

		/** Robot conerné par le mouvement */
		Robot r;

		/** Direction du mouvement */
		Direction dir;

		/**
		 * Construction d'un mouvement sur le plateau de jeu.
		 * 
		 * @param r   Robot converné par le mouvement
		 * @param dir Direction du mouvement
		 */
		public Move(Robot r, Direction dir) {
			this.r = r;
			this.dir = dir;
		}

		/**
		 * Description textuelle d'un move pour afficher la série de mouvements
		 * nécessaires.
		 */
		public String toString() {
			return "R" + r.getId() + " : " + Direction.symbol(dir);
		}
	}
}
