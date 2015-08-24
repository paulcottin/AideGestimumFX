package controleurs;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchCSS implements EventHandler<ActionEvent>{

	utilitaires.SearchCSS s;
	
	public SearchCSS(ArrayList<File> files) {
		this.s = new utilitaires.SearchCSS(files);
	}

	@Override
	public void handle(ActionEvent event) {
		s.search();
	}

}
