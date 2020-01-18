import java.util.ArrayList;


public class Case {

	public ArrayList<Boolean> murs =  new ArrayList<Boolean>();
	//A revoir pour le type par ex 1 jeton, 2 joueur 3 cible
	public int type;
	//Présence d'un jeton ou pas
	public Boolean presenceJeton;
	//Informations sur le Jeton
	public Jeton jeton;
	
	//constructeur
	public Case(Boolean up, Boolean right, Boolean down, Boolean left, int type, Boolean presenceJeton, Jeton jeton){
		this.murs.add(up);
		this.murs.add(right);
		this.murs.add(down);
		this.murs.add(left);
		this.type = type;
		this.presenceJeton = presenceJeton;
		this.jeton = jeton;
		
	}
}
