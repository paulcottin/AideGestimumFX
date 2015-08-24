package controleurs;

import java.io.File;
import java.util.ArrayList;

import javafx.event.EventHandler;
import utilitaires.Utils;

public class ExportTaille implements EventHandler<javafx.event.ActionEvent>{

	ArrayList<File> files;
	
	public ExportTaille(ArrayList<File> files) {
		this.files = files;
	}

	@Override
	public void handle(javafx.event.ActionEvent event) {
		(new Utils()).generateFileSize(files);
	}

}
