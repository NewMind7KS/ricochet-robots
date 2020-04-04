package ricochet.util;

/**
 * Interface implémentée par les modèles. L'ajout et le retrait d'écouteur
 * doit être redéfini.
 */
public interface ModeleEcoutable {

	public void ajoutEcouteur(EcouteurModele e);
	public void retraitEcouteur(EcouteurModele e);
}
