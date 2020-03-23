package ricochet.util;

public interface EcouteurModele {

	/**
	 * Les vues qui implémente cette interface doivent donner un comportement si le
	 * modèle change. Le modèle que j'écoute à changé.
	 */
	public void modeleMisAJour(ModeleEcoutable source);
}
