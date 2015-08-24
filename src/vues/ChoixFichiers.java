package vues;

import java.io.File;
import java.util.ArrayList;

import interfaces.ProgressTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChoixFichiers extends Stage {

	private ListView<String> listeFichier;
	private Button ok, annuler;
	private ArrayList<File> files, principaleFiles;
	private ProgressTask lancerAction;
	private Thread th;
	private ProgressBar bar;

	public ChoixFichiers(ArrayList<File> files) {
		super();
		this.principaleFiles = new ArrayList<File>();
		this.principaleFiles.addAll(files);
		this.files = new ArrayList<File>();
		this.th = null;
		this.lancerAction = null;
		init();
	}
	
	public ChoixFichiers(ArrayList<File> files, ProgressTask lancerAction) {
		super();
		this.files = new ArrayList<File>();
		this.principaleFiles = new ArrayList<File>();
		this.principaleFiles.addAll(files);
		this.lancerAction = lancerAction;
		init();
		this.th = new Thread(lancerAction);
	}
	
	private void init(){
		this.setWidth(715);
		this.setHeight(800);
		this.setTitle("Choix des fichiers");
		bar = new ProgressBar(lancerAction);
		
		ObservableList<String> list = FXCollections.observableArrayList();
		for (File file : principaleFiles) {
			if (file.getAbsolutePath().endsWith(".htm")) {
				list.add(file.getAbsolutePath());
			}
		}
		listeFichier = new ListView<String>();
		
		listeFichier.getItems().addAll(list);
		listeFichier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		this.ok = new Button("Valider");
		ok.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<File> listFile = new ArrayList<File>();
				for (String string : listeFichier.getSelectionModel().getSelectedItems()) {
					listFile.add(new File(string));
				}
				lancerAction.fichiersSelectionnes(listFile);
				th.start();
				close();
			}
		});
		
		this.annuler = new Button("Annuler");
		annuler.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				bar.close();
				close();
			}
		});
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setPrefHeight(25);
		listeFichier.setPrefHeight(getHeight()-buttonBar.getHeight());
		listeFichier.setPrefWidth(getWidth());
		buttonBar.getButtons().addAll(ok, annuler);
		VBox box = new VBox(listeFichier, buttonBar);
		box.setCenterShape(true);
		box.setFillWidth(true);
		AnchorPane pane = new AnchorPane();
		this.setScene(new Scene(pane));
		this.show();
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

}
