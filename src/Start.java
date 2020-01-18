
public class Start
{
	public static void main(String [] args)
	{
		//créé un plateau
		Plateau plateau = new Plateau(6,6);
		//crée quatre jetons (plus tard les positionner aléatoirement (vérifier qu'il n'y en a pas déjà un))
		//il est aussi possible de mettre les jetons dans un arrayList et de passer cet array list(ça fait moins de paramètres)
		Jeton jetonVert = new Jeton(1,1,"Vert");
		Jeton jetonRouge = new Jeton(1,2,"Rouge");
		Jeton jetonJaune = new Jeton(3,4,"Jaune");
		Jeton jetonBleu = new Jeton(4,3,"Bleu");
		//initialise le plateau
		plateau.initPlateau();
		//place les jetons sur le plateau
		plateau.putJetons(jetonVert,jetonRouge,jetonJaune,jetonBleu);
		//affichage du plateau
		plateau.afficherPlateau();
	}
}
