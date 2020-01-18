
public class Plateau {
	protected Case [][]plateau;
	
	public Plateau(int m, int n){
		this.plateau = new Case[m][n];
	}
	
	public Case [][]initPlateau(){
		//deux int qui permettent de prendre les dimensions du tableau à deux dimensions
		int m = this.plateau[0].length;
		int n = this.plateau[1].length;
		for (int i = 0; i < m; i++){
			for (int j = 0; j < n; j++){
				//conditions to build the board
				//en [0][0]
				if (i == 0 && j == 0){
					this.plateau[i][j]= new Case(true, false, false, true, 0, false,null);
				}
				//en [0][n]
				else if (i == 0  && j == n-1){
					this.plateau[i][j]= new Case(true, true, false, false, 0, false,null);
				}
				//en [m][0]
				else if (i == m-1 && j == 0){
					this.plateau[i][j]= new Case(false, false, true, true, 0, false,null);
				}
				//en [m][n]
				else if (i == m-1 && j == n-1){
					this.plateau[i][j]= new Case(false, true, true, false, 0, false,null);
				}
				//Si la première colonne
				else if (j == 0 && i > 0){
					this.plateau[i][j]= new Case(false, false, false, true, 0, false,null);
				}
				//Si la dernière colonne
				else if (j == n-1 && i > 0){
					this.plateau[i][j]= new Case(false, true, false, false, 0, false,null);
				}
				//Si la première ligne
				else if (i == 0){
					this.plateau[i][j]= new Case(true, false, false, false, 0, false,null);
				}
				//Si la dernière ligne
				else if (i == m-1){
					this.plateau[i][j]= new Case(false, false, true, false, 0, false,null);
				}
				//uniquement pour tableau de taille 6 !!! 
				else if ((i == 2 && j == 2) || (i == 2 && j == 3) || (i == 3 && j == 2) || (i == 3 && j == 3)){
					this.plateau[i][j]= new Case(true, true, true, true, 0, false,null);
				}
				//ceci est pour tester, il faudra faire du random pour les murs ici ainsi que les jetons.
				else{
					this.plateau[i][j]= new Case(false, false, false, false, 0, false,null);
				}
			}
			//calcul et mise en place des 4 cases du centre
			
		}
		return this.plateau;
	}
	 public Case[][]putJetons(Jeton jetonVert, Jeton jetonRouge, Jeton jetonJaune, Jeton jetonBleu){
		 this.plateau[jetonVert.getX()][jetonVert.getY()].presenceJeton = true;
		 this.plateau[jetonRouge.getX()][jetonRouge.getY()].presenceJeton = true;
		 this.plateau[jetonJaune.getX()][jetonJaune.getY()].presenceJeton = true;
		 this.plateau[jetonBleu.getX()][jetonBleu.getY()].presenceJeton = true;
		 
		 this.plateau[jetonVert.getX()][jetonVert.getY()].jeton = jetonVert;
		 this.plateau[jetonRouge.getX()][jetonRouge.getY()].jeton = jetonRouge;
		 this.plateau[jetonJaune.getX()][jetonJaune.getY()].jeton = jetonJaune;
		 this.plateau[jetonBleu.getX()][jetonBleu.getY()].jeton = jetonBleu;
		 return this.plateau;
	 }
	
	public void afficherPlateau(){
		for (int i = 0; i < this.plateau[0].length; i++){
			for (int j = 0; j < this.plateau[1].length; j++){
				//System.out.print(this.plateau[i][j].murs);
				System.out.print("(");
				if (this.plateau[i][j].murs.get(3) == true){
					System.out.print(" left ");
				}
				else{
					System.out.print("      ");
				}
				if (this.plateau[i][j].murs.get(0) == true){
					System.out.print(" up ");
				}
				else{
					System.out.print("    ");
				}
				
				if (this.plateau[i][j].murs.get(2) == true){
					System.out.print(" down ");
				}
				else{
					System.out.print("      ");
				}
				if (this.plateau[i][j].murs.get(1) == true){
					System.out.print(" right ");
				}
				else{
					System.out.print("       ");
				}
				//Permet de noter la présence d'un jeton en donnant sa couleur
				if (this.plateau[i][j].presenceJeton == true){
					if (this.plateau[i][j].jeton.getColor() == "Vert"){
						System.out.print(" Vert  ");
					}
					else if (this.plateau[i][j].jeton.getColor() == "Rouge"){
						System.out.print(" Rouge ");
					}
					else if (this.plateau[i][j].jeton.getColor() == "Jaune"){
						System.out.print(" Jaune ");
					}
					else if (this.plateau[i][j].jeton.getColor() == "Bleu"){
						System.out.print(" Bleu  ");
					}
				}
				//affiche un espace vide dans le cas où il n'y a pas de jeton
				else{
					System.out.print("       ");
				}
				
				
				System.out.print(") | ");
			}
			System.out.print("\n");
			//System.out.print("-----------------");
			//System.out.print("\n");
		}
	}
	
	
}
