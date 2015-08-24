package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import utilitaires.Utils;

public class NoHelp implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent event) {
		(new Utils()).checkNoHelpWindows();
	}

}
