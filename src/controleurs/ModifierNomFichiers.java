package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Principale;

public class ModifierNomFichiers implements EventHandler<ActionEvent> {

	Principale p;
	
	public ModifierNomFichiers(Principale p) {
		this.p = p;
	}

	@Override
	public void handle(javafx.event.ActionEvent event) {
//		p.modifNomFichiers();
	}

}
