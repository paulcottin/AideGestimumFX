package controleurs;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.Script;

public class ParametrerScript implements EventHandler<ActionEvent>{

	private Script s;
	
	public ParametrerScript(Script s) {
		this.s = s;
	}

	@Override
	public void handle(ActionEvent event) {
		s.initParam();
		s.choisirFichiers();
	}

}
