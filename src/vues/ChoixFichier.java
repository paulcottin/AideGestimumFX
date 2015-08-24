package vues;

import java.io.File;
import java.util.ArrayList;

import org.omg.CORBA.INITIALIZE;

import controleurs.Lancer;
import interfaces.ProgressTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ChoixFichier {
	
	@FXML
	private ListView<String> fichiers;
	@FXML
	private Button ok;
	@FXML 
	private Button annuler;
	
	private ProgressTask task;
	private Thread th;
	private Lancer lancer;
	
	public ChoixFichier() {
		fichiers = new ListView<String>();
		this.th = new Thread(task);
	}
	
	@FXML
	private void INITIALIZE() {
		
	}
	
	@FXML
	private void okHandle() {
		ArrayList<File> listFile = new ArrayList<File>();
		for (String string : fichiers.getSelectionModel().getSelectedItems()) {
			listFile.add(new File(string));
		}
		task.fichiersSelectionnes(listFile);
		th.start();
		lancer.closeChoixFichier();
	}
	
	@FXML
	private void annulerHandle() {
		lancer.closeChoixFichier();	
	}

	public ListView<String> getFichiers() {
		return fichiers;
	}

	public void setFichiers(ObservableList<String> fichiers) {
		this.fichiers.setItems(fichiers);
	}

	public void setLancer(Lancer lancer) {
		this.lancer = lancer;
	}

	public void setTask(ProgressTask task) {
		this.task = task;
	}

}
