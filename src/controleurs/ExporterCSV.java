package controleurs;

import java.io.IOException;

import exceptions.ParametrageError;
import javafx.event.EventHandler;
import main.Principale;

public class ExporterCSV implements EventHandler<javafx.event.ActionEvent>{

	Principale p;
	
	public ExporterCSV(Principale p) {
		this.p = p;
	}
	@Override
	public void handle(javafx.event.ActionEvent event) {
		try {
			p.exportCSV();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParametrageError e1) {
			e1.printMessage();
		}
	}

}
