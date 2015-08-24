package controleurs;

import java.io.File;
import java.io.IOException;

import interfaces.ProgressTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Principale;
import vues.ChoixFichier;
import vues.ChoixFichiers;
import vues.ProgressBar;

public class Lancer implements EventHandler<ActionEvent>{
	public static int ALL = 0;
	public static int SELECTED_LINES = 1;


	private ProgressTask p;
	private int choix;
	private Principale principale;
	private Thread th;
	private Stage stage;
	ProgressBar bar;

	public Lancer(ProgressTask p, Principale principale){
		this.p = p;
		this.choix = ALL;
		this.principale = principale;
		this.th = new Thread(p);
		this.th.setDaemon(true);
		bar = new ProgressBar(p);
	}

	public Lancer(ProgressTask p, Principale principale, int choix){
		this.p = p;
		this.choix = SELECTED_LINES;
		this.principale = principale;
		bar = new ProgressBar(p);
		stage = new Stage();
	}

	@Override
	public void handle(ActionEvent event) {
		if (choix == ALL){
			th.start();
		}
		else if (choix == SELECTED_LINES){
			//@SuppressWarnings("unused")
			//ChoixFichiers cf = new ChoixFichiers(principale.getFiles(), p);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Principale.class.getResource("../vues/ChoixFichier.fxml"));
			AnchorPane choixfichier;
			try {
				choixfichier = (AnchorPane) loader.load();
				stage.setScene(new Scene(choixfichier));
				ChoixFichier choixFichierControlleur = loader.getController();
				ObservableList<String> list = FXCollections.observableArrayList();
				for (File file : principale.getFiles()) {
					if (file.getAbsolutePath().endsWith(".htm"))
						list.add(file.getAbsolutePath());
				}
				choixFichierControlleur.setFichiers(list);
				choixFichierControlleur.setLancer(this);
				choixFichierControlleur.setTask(p);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeChoixFichier() {
		System.out.println("coucou");
		bar.close();
		stage.close();
	}
}
