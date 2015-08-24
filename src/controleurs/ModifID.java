package controleurs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import exceptions.BadFile;
import exceptions.ParametrageError;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import utilitaires.Utils;

public class ModifID implements EventHandler<ActionEvent>{

	ArrayList<File> files;
	Utils utils;

	public ModifID(ArrayList<File> files) {
		this.files = files;
		utils = new Utils();
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			utils.changeId(files);
		} catch (ParametrageError e) {
			e.printMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadFile e) {
			e.printMessage();
		}
	}

}
