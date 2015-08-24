package main;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import exceptions.ParametrageError;
import interfaces.Action;
import interfaces.NeedSelectionFichiers;
import interfaces.ProgressTask;
import vues.ChoixFichiers;
import vues.ProgressBar;
import vues.ScriptChooser;

public class Script extends ProgressTask implements NeedSelectionFichiers, Observer{

	private ArrayList<Action> actions;
	private ArrayList<File> files, ppFiles, cssFiles;
	private ScriptChooser scriptChooser;
	private Principale principale;
	private boolean running;
	private int index;
	private String fichierEnCours, actionEnCours;

	public Script(ArrayList<File> files, Principale principale) {
		this.actions = new ArrayList<Action>();
		this.files = new ArrayList<File>();
		this.cssFiles = new ArrayList<File>();
		this.ppFiles = new ArrayList<File>();
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".htt"))
				ppFiles.add(file);
			else if (file.getAbsolutePath().endsWith(".css"));
				cssFiles.add(file);
			this.files.add(file);
		}
		this.principale = principale;
		this.running = false;
		this.index = 0;
		this.fichierEnCours = "";
		this.actionEnCours = "";
	}
	
	public void initParam() {
		running= false;
		index = 0;
		fichierEnCours = "";
		actions.clear();
		actionEnCours = "";
	}
	
	@Override
	protected Boolean call() throws Exception {
		run();
		return true;
	}

	public void runActions(){
		running = true;
//		setChanged();
//		notifyObservers();
		
		for (Action lancerAction : actions) {
			lancerAction.fichiersSelectionnes(files);
			lancerAction.run();
			index++;
		}
		running = false;
//		setChanged();
//		notifyObservers();
		
	}
	
	@Override
	public void run(){
		runActions();
		Principale.messageFin("Script terminé");
	}

	@Override
	public void fichiersSelectionnes(ArrayList<File> files) {
		this.files.clear();
		this.files.addAll(files);
		choisirActions();
	}

	public boolean choisirFichiers(){
		String[] choixFichiers = {"Tous", "Sélection"};
		String choix = (String)JOptionPane.showInputDialog(null, 
				"Sur quels fichiers voulez-vous travailler",
				"Choix des fichiers",
				JOptionPane.QUESTION_MESSAGE,
				null,
				choixFichiers,
				choixFichiers[0]);

		if (choix != null) {
			if (choix.equals("Tous")) {
				choisirActions();
			}
			else if (choix.equals("Sélection")){
				new ChoixFichiers(files, this);
			}
			return true;
		}else
			return false;
	}

	public void choisirActions(){
		scriptChooser = new ScriptChooser(this);
	}

	@SuppressWarnings("unchecked")
	public void choisirActionHelper(){
		scriptChooser.dispose();
		ArrayList<File> fileEtCssEtHtt = new ArrayList<File>();
		fileEtCssEtHtt.addAll(files);
		fileEtCssEtHtt.addAll(cssFiles);
		fileEtCssEtHtt.addAll(ppFiles);
		for (String string : scriptChooser.getActionsChoisies()) {
			for (Action a : principale.getScripts()) {
				if (string.equals(a.getIntitule())) {
					Class<Action> action;
					try {
						action = (Class<Action>) Class.forName(a.getClass().getName());
						Constructor<Action> constructeur = action.getConstructor(new Class[] {
								Class.forName("java.util.ArrayList")
						});
						this.actions.add(constructeur.newInstance(fileEtCssEtHtt));
					} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
//		for (Action action : actions) {
//			action.addObserver(this);
//		}
		Thread th = new Thread(this);
		th.start();
		//new ProgressBar(this);
//		setChanged();
//		notifyObservers();
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	public Principale getPrincipale() {
		return principale;
	}

	public void setPrincipale(Principale principale) {
		this.principale = principale;
	}

	@Override
	public void update(Observable o, Object arg) {
		fichierEnCours = actions.get(index).getMessage();
		actionEnCours= actions.get(index).getTitle();
	}
}
