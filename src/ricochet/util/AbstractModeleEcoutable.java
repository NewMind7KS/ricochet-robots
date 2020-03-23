package ricochet.util;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModeleEcoutable implements ModeleEcoutable {

	private List<EcouteurModele> ecouteurs;

	public AbstractModeleEcoutable() {
		ecouteurs = new ArrayList<EcouteurModele>();
	}

	public AbstractModeleEcoutable(List<EcouteurModele> list) {
		ecouteurs = list;
	}

	public void ajoutEcouteur(EcouteurModele e) {
		ecouteurs.add(e);
	}

	public void retraitEcouteur(EcouteurModele e) {
		ecouteurs.remove(e);
	}

	/**
	 * Notifie tous les écouteurs qu'il y a eu un changement sur le modèle.
	 */
	protected void notifyListener() {
		for (EcouteurModele e : ecouteurs) {
			e.modeleMisAJour(this);
		}
	}
}
