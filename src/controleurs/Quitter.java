package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Quitter implements EventHandler<ActionEvent>{

	public Quitter() {
		
	}

	@Override
	public void handle(ActionEvent event) {
		System.exit(0);
	}

}