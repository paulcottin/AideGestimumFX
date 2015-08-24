package controleurs;

import java.io.File;
import java.util.ArrayList;

import exceptions.ParametrageError;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import utilitaires.Utils;

public class ConvertImg implements EventHandler<ActionEvent>{

	ArrayList<File> pngFiles;
	
	public ConvertImg(ArrayList<File> files) {
		pngFiles = new ArrayList<File>();
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".png"))
				pngFiles.add(file);
		}
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			(new Utils()).pngToJpeg(pngFiles);
		} catch (ParametrageError e) {
			e.printStackTrace();
		}
	}

}
