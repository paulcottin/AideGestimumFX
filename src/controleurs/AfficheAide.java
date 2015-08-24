package controleurs;


import exceptions.AideException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import vues.Aide;

public class AfficheAide implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent event) {
		try {
			new Aide();
		} catch (AideException e) {
			e.printMessage();
		}
	}

}
