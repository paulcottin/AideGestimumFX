package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import actions.AssociationAuto;
import actions.ChangerStyle;
import actions.ChoixFeuilleStyle;
import actions.ChoixPagePrincipale;
import actions.ColorationPuces;
import actions.Copyright;
import actions.CreationPuce;
import actions.Lien;
import actions.Majuscule;
import actions.NettoyagePagePrincipale;
import actions.NettoyageTitre;
import actions.NoPP;
import actions.RechercheImage;
import actions.Style;
import actions.SupprStyleTitre;
import actions.SuppressionEspace;
import actions.SupprimerTitre;
import actions.Titre;
import exceptions.NoRoboHelpProject;
import exceptions.ParametrageError;
import interfaces.Action;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utilitaires.Utils;
import vues.Fenetre;

public class Principale extends Application {

	public static File FILE_BASE;

	private Script script;
	private AssociationAuto associationAuto;
	private RechercheImage rechercheImage;
	private ArrayList<Action> scripts;
	private BorderPane rootLayout;

	public File topics;
	public ArrayList<File> files;

	private Utils utils;

	public Principale() throws NoRoboHelpProject{
		files = new ArrayList<File>();
		topics = new File(getTopicsPath());

		files = listerRepertoire(topics);

		if (!isRobotHelpProject())
			throw new NoRoboHelpProject("Ce dossier ne contient pas de projet RoboHelp");

		script = new Script(files, this);
		associationAuto = new AssociationAuto(files);
		rechercheImage = new RechercheImage(files, topics.getAbsolutePath());
		scripts = new ArrayList<Action>();
		utils = new Utils();

//		int cptJPG = 0, cptPNG = 0, cptGIF = 0;
//		for (File file : files) {
//			if (file.getAbsolutePath().endsWith(".jpg"))
//				cptJPG++;
//			else if (file.getAbsolutePath().endsWith(".png"))
//				cptPNG++;
//			else if (file.getAbsolutePath().endsWith(".gif"))
//				cptGIF++;
//		}
//		
//		System.out.println("png : "+cptPNG+", jpg : "+cptJPG+", git : "+cptGIF+"\ntotal : "+(cptGIF+cptPNG+cptJPG));

		initActions();
	}

	private void initActions(){
		scripts.add(new Style(files));
		scripts.add(new ChoixFeuilleStyle(files));
		scripts.add(new ChoixPagePrincipale(files));
		scripts.add(new ColorationPuces(files));
		scripts.add(new ChangerStyle(files));
		scripts.add(new Titre(files));
		scripts.add(new CreationPuce(files));
		scripts.add(new Lien(files));
		scripts.add(new SupprimerTitre(files));
		scripts.add(new SupprStyleTitre(files));
		scripts.add(new NettoyagePagePrincipale(files));
		scripts.add(new NettoyageTitre(files));
		scripts.add(new Copyright(files));
		scripts.add(new NoPP(files));
		scripts.add(new SuppressionEspace(files));
		scripts.add(new Majuscule(files));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		rootLayout = new BorderPane();
		Scene scene = new Fenetre(this, rootLayout,500,800);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.show();
	}

	private String getTopicsPath(){
		JOptionPane.showMessageDialog(null, "<html><h3>Fermer RoboHelp avant toute utilisation de ce programme</h3>"
				+ "Veuillez donner le chemin vers <span style=\"color:red;\">le dossier</span> du projet<br/>Penser à mettre à jour Java"
				+ "(Version 1.8 ou supérieure)</html>");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(getCurrentDir());
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			return f.getAbsolutePath();
		}
		else
			return null;
	}

	public ArrayList<File> listerRepertoire(File topics){ 
		ArrayList<File> files = new ArrayList<File>();
		File[] listefichiers; 

		listefichiers=topics.listFiles();
		for(int i=0;i<listefichiers.length;i++){ 
			//Spécifie un fichier de base pour traiter les autres liens en relatif
			if (listefichiers[i].getAbsolutePath().endsWith(".xpj"))
				FILE_BASE = listefichiers[i];

			if (listefichiers[i].isDirectory() && !listefichiers[i].getAbsolutePath().contains("!")) {
				files.add(listefichiers[i]);
				files.addAll(listerRepertoire(listefichiers[i]));
			}
			else if (!listefichiers[i].isDirectory())
				files.add(listefichiers[i]);
		}
		return files;
	}

	private boolean isRobotHelpProject(){
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".hhp") || file.getAbsolutePath().endsWith(".xpj"))
				return true;
		}
		return false;
	}

	public void exportCSV() throws IOException, ParametrageError{
		File f = utils.chooseSaveFile();

		if (f != null) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));

			for (File file : files) {
				if (file.getAbsolutePath().endsWith(".htm")) {
					bw.write(file.getAbsolutePath()+"\r\n");
				}
			}
			bw.close();

			messageFin("Export des données ok !");
		}else
			messageFin("Veuillez donner un nom valide");

	}

	private File getCurrentDir(){
		String tmp = System.getProperty("user.dir");
		return new File(tmp);
	}

	public static void fileMove(File from, File to) throws FileSystemException{
		Path pathProject = FileSystems.getDefault().getPath(to.getAbsolutePath());
		Path pathTmp = FileSystems.getDefault().getPath(from.getAbsolutePath());
		try {
			Files.move(pathTmp, pathProject, StandardCopyOption.REPLACE_EXISTING);
		} catch (FileSystemException fse){
			throw fse;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fileCopy(File from, File to) throws FileSystemException{
		Path pathProject = FileSystems.getDefault().getPath(to.getAbsolutePath());
		Path pathTmp = FileSystems.getDefault().getPath(from.getAbsolutePath());
		try {
			Files.copy(pathTmp, pathProject, StandardCopyOption.REPLACE_EXISTING);
		} catch (FileSystemException fse){
			throw fse;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void messageFin(String message){
		JOptionPane.showMessageDialog(null, "<html>"+message+"</html>");
	}

	public static void main(String[] args){
		launch(args);
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public ArrayList<Action> getScripts() {
		return scripts;
	}

	public void setScripts(ArrayList<Action> scripts) {
		this.scripts = scripts;
	}

	public File getTopics() {
		return topics;
	}

	public void setTopics(File topics) {
		this.topics = topics;
	}

	public AssociationAuto getAssociationAuto() {
		return associationAuto;
	}

	public void setAssociationAuto(AssociationAuto associationAuto) {
		this.associationAuto = associationAuto;
	}

	public RechercheImage getRechercheImage() {
		return rechercheImage;
	}

	public void setRechercheImage(RechercheImage rechercheImage) {
		this.rechercheImage = rechercheImage;
	}

	public BorderPane getRootLayout() {
		return rootLayout;
	}

	public void setRootLayout(BorderPane rootLayout) {
		this.rootLayout = rootLayout;
	}
}
