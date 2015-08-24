package vues;

import interfaces.ProgressTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Barre de progression tâches un peu longues
 * @author paul
 *
 */
public class ProgressBar extends Stage {

	private String titre;
	private Label fichierEnCours;
	private javafx.scene.control.ProgressBar bar;


	public ProgressBar(ProgressTask task) {
		super();
		this.setWidth(715);
		this.setHeight(80);
		this.titleProperty().bind(task.titleProperty());

		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				close();
			}
		});
		task.setOnRunning(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				show();
			}
		});
		this.titre = "Un titre";
		this.fichierEnCours = new Label();
		fichierEnCours.textProperty().bind((task).messageProperty());
		this.bar = new javafx.scene.control.ProgressBar();
		bar.setPrefWidth(600);
		
		VBox box = new VBox(5);
		box.getChildren().addAll(fichierEnCours, bar);
		AnchorPane pane = new AnchorPane(box);
		
		this.setScene(new Scene(pane));	
	}

	public Label getFichierEnCours() {
		return fichierEnCours;
	}

	public void setFichierEnCours(Label fichierEnCours) {
		this.fichierEnCours = fichierEnCours;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
}