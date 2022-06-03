package ricochet.modele;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;

public class consoleMain {
    private static int compteur = 0;
    private Board board;
    /*compteur*/
    public static int getCompteur() {
        return compteur;
    }
    public static void setCompteur(int compteur) {
        consoleMain.compteur = compteur;
    }

    public void keyPressed(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_LEFT) {
            if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.W)) {
                board.moveRobot(board.getMainRobot(), Direction.W);
                consoleMain.compteur++;
            }
        } else if (key.getKeyCode() == KeyEvent.VK_UP) {
            if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.N)) {
                board.moveRobot(board.getMainRobot(), Direction.N);
                consoleMain.compteur++;
            }
        } else if (key.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.E)) {
                board.moveRobot(board.getMainRobot(), Direction.E);
                consoleMain.compteur++;
            }
        } else if (key.getKeyCode() == KeyEvent.VK_DOWN) {
            if (board.canMoveInDir(board.getMainRobot().getPositionRobot(), Direction.S)) {
                board.moveRobot(board.getMainRobot(), Direction.S);
                consoleMain.compteur++;
            }
        }
        if (board.isFinished())
            System.out.println("Jeu Terminé en " + getCompteur() + " coups");
    }
    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
    public static void main(String[] args) {
        Board board = new Board("dist/plateau.txt");
        board.loadBoard();
        board.printBoard();
        board.takeGoal();
        board.takeRobot();
        board.getMainRobot();
        board.getMainGoal();
        System.out.println("votre robot est:"+ board.getMainRobot());
        System.out.println("votre cible est:"+ board.getMainGoal());
       // board.moveRobot(board.getMainRobot(), Direction.);
        Robot r = board.getMainRobot();

        // tant que la partie n'est pas terminée
      while (!board.isFinished()){
            Scanner scan = new Scanner(System.in);
            System.out.println("Quelle direction voulez-vous prendre : Haut H, Bas B, Gauche G, Droite D ?");
            String move = scan.nextLine();

            switch (move){
                case "H":
                    board.moveRobot(r, Direction.N);
                    break;
                case "B":
                    board.moveRobot(r, Direction.S);
                    break;
                case "D":
                    board.moveRobot(r, Direction.E);
                    break;
                case "G":
                    board.moveRobot(r, Direction.W);
                    break;
            }
            board.printBoard();
            System.out.println("votre robot est:"+ board.getMainRobot());
            System.out.println("votre cible est:"+ board.getMainGoal());
        }
        System.out.println("Partie terminée. Le robot est bien sur le goal principal " + board.getMainGoal());

       /*   ArrayList<Robot> r_list = board.getRobots();
        Robot main_r = board.takeRobot();
        Robot r1 = null;
        int player_turn = 1;
        for (Robot rr: r_list) {
            System.out.println(rr);
            if(!rr.equals(main_r)){
                r1  = new Robot(rr.getPositionRobot());
            }
        }
        while (!board.isFinished()){
            Scanner scan = new Scanner(System.in);


            if (player_turn == 1){
                System.out.println("Quelle direction voulez-vous prendre joueur 1 : Haut H, Bas B, Gauche G, Droite D ?");
                String move_1 = scan.nextLine();
                switch (move_1){
                    case "H":
                        board.moveRobot(main_r, Direction.N);
                        player_turn = 2;
                        break;
                    case "B":
                        board.moveRobot(main_r, Direction.S);
                        player_turn = 2;
                        break;
                    case "D":
                        board.moveRobot(main_r, Direction.E);
                        player_turn = 2;
                        break;
                    case "G":
                        board.moveRobot(main_r, Direction.W);
                        player_turn = 2;
                        break;
                }
            }else{
                System.out.println("Quelle direction voulez-vous prendre joueur 2 : Haut H, Bas B, Gauche G, Droite D ?");
                String move_2 = scan.nextLine();
                switch (move_2){
                    case "H":
                        board.moveRobot(r1, Direction.N);
                        player_turn = 1;
                        break;
                    case "B":
                        board.moveRobot(r1, Direction.S);
                        player_turn = 1;
                        break;
                    case "D":
                        board.moveRobot(r1, Direction.E);
                        player_turn = 1;
                        break;
                    case "G":
                        board.moveRobot(r1, Direction.W);
                        player_turn = 1;
                        break;
                }
            }
            board.printBoard();
            System.out.println("votre robot est:"+ board.getMainRobot());
            System.out.println("votre cible est:"+ board.getMainGoal());
        }
        System.out.println("Partie terminée par le joueur " + player_turn + "Le robot est bien sur le goal principal " + board.getMainGoal());


      System.out.println("\n");
        System.out.println("************---------------------------****************************");
        System.out.println("Robot principal : " + main_r.toString());
        System.out.println("Robot r1 :" + r1.toString());*/
       // System.out.println("Robot r2 :" + r2.toString());


    }

 }
