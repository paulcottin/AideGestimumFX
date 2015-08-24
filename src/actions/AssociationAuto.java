package actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import exceptions.FichierNonTrouve;
import exceptions.FichierUtilise;
import exceptions.ParametrageError;
import interfaces.LancerAction;
import interfaces.LongTask;
import interfaces.ProgressTask;
import javafx.concurrent.Task;
import main.Principale;
import utilitaires.Utils;

public class AssociationAuto extends ProgressTask implements LancerAction, LongTask, Runnable{

	private File sourceFile;
	private ChoixPagePrincipale choixPP;
	private ArrayList<String> paths, PP;
	private ArrayList<File> ppFiles;
	private boolean running;
	private FichierNonTrouve fichierNonTrouve;
	private String fileProcessing;
	private Utils utils;

	public AssociationAuto(ArrayList<File> files) {
		utils = new Utils();
		choixPP = new ChoixPagePrincipale(files);
		this.PP = new ArrayList<String>();
		this.paths = new ArrayList<String>();
		this.ppFiles = new ArrayList<File>();
		this.running = false;
		fichierNonTrouve = new FichierNonTrouve();
	}
	
	@Override
	protected Boolean call() throws Exception {
		run();
		return true;
	}

	@Override
	public void run() {
		try {
			lancerActionAll();
		} catch (ParametrageError e) {
			e.printMessage();
		}	
	}

	@Override
	public void lancerActionAll() throws ParametrageError {
		getSourceFile();
		if (sourceFile != null) {
			try {
				checkEncodage();
				getPathAndPP();
				if (displayPP()) {
					applyStyle();
					if (fichierNonTrouve.getPages().size() > 0) {
						String msg = "Fichiers non trouvés : <br/><ul>";
						for (String string : fichierNonTrouve.getPages()) {
							msg += "<li>"+string+"</li>";
						}
						msg += "</ul>";
						Principale.messageFin(msg);
					}
					else
						Principale.messageFin("Application automatique effectuée avec succès");
				}
			} catch (FileSystemException e) {
				new FichierUtilise(sourceFile.getName());
			}
		}
		else
			Principale.messageFin("Veuillez renseigner un fichier");
		this.running = false;
		update();
	}

	@Override
	public void lancerAction(ArrayList<File> files) {

	}

	@Override
	public void fichiersSelectionnes(ArrayList<File> files) {
		lancerAction(files);
	}

	@Override
	public void parametrer(){

	}

	private void getSourceFile() throws ParametrageError {
		JOptionPane.showMessageDialog(null, "<html>Veuillez sélectionner le fichier csv (séparateur ';') contenant les liens<br/>"
				+ "Ce fichier doit être sans en-tête, les chemins à gauche, les pages principales à droite</html>");
		sourceFile = utils.chooseFile("Sélection du fichier CSV");
	}

	private void getPathAndPP(){		
		try {
			BufferedReader br = new BufferedReader(new FileReader(sourceFile));
			String ligne = "";

			while ((ligne = br.readLine()) != null) {
				String[] tab = ligne.split(";");
				if (!tab[tab.length-1].equals("0")) {
					paths.add(tab[0]);
					PP.add(tab[tab.length-1]);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean displayPP() throws ParametrageError{
		ArrayList<String> tmp = new ArrayList<String>();
		//On récupère toutes les pages principales du fichier source
		for (String string : PP) {
			if (!tmp.contains(string) && !string.equals("0")) {
				tmp.add(string);
			}
		}

		//On met dans un tableau les différentes pp (htt) qui existent dans le projet
		String[] tab = new String[ppFiles.size()];
		for (int i = 0; i < ppFiles.size(); i++) {
			tab[i] = ppFiles.get(i).getName();
		}

		//Si il y a des pages principales crées
		if (tab.length > 0) {
			//Pour chacune des pages du fichier source on demande de faire un match avec une page principale htt
			for (int i = 0; i < tmp.size(); i++) {
				String name =	(String) JOptionPane.showInputDialog(null, 
						tmp.get(i),
						"Correspondance",
						JOptionPane.QUESTION_MESSAGE,
						null,
						tab, tab[0]);
				if (name == null)
					throw new ParametrageError("Il faut faire correspondre toutes les pages principales");

				updatePath(tmp.get(i), name);
			}
			return true;
		}else {
			Principale.messageFin("Il faut d'abord définir une page principale !");
			return false;
		}

	}

	private void applyStyle(){
		this.running = true;
		update();
		for (int i = 0; i < PP.size(); i++) {
			if (!PP.get(i).equals(0)) {
				fileProcessing = paths.get(i);
				update(); 
				//on fixe la pp
				choixPP.setPagePath(PP.get(i));
				//On fixe les fichiers sur lesquels on applique le traitement
				String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
				File tmp = new File(generateString(5, chars)+".jlb");
				File file = new File(paths.get(i));
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp), "UTF-8"));
					String ligne = "", txt = "";

					while ((ligne = br.readLine()) != null){
						txt += ligne+"\r\n";
					}

					Document doc = Jsoup.parse(txt, "utf-8");
					doc = choixPP.applyStyle(doc);
					String html = doc.html();

					//Décommente les balises RoboHelp, commentée automatiquement par
					//la libraire JSoup.
					html = html.replace("<!--?", "<?");
					html = html.replace("?-->", "?>");

					bw.write(html);
					br.close();
					bw.close();
					Principale.fileMove(tmp, file);
					tmp.delete();

				} catch (IOException e) {
					fichierNonTrouve.add(file.getAbsolutePath());
				}				
			}
		}
	}

	/**
	 * Génère une chaîne de caractère pour le nom d'un fichier temporaire.
	 * @param length : longueur de la chaine
	 * @param chars : Chaîne de caractère servant de base à la génération.
	 * @return
	 */
	private String generateString(int length, String chars) {
		StringBuilder  pass = new StringBuilder (chars.length());
		for (int x = 0; x < length; x++) {
			int i = (int) (Math.random() * chars.length());
			pass.append(chars.charAt(i));
		}
		return pass.toString();
	}

	@Override
	public void reloadFiles(ArrayList<File> files) {
		ppFiles.clear();
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".htt"))
				ppFiles.add(file);
		}
	}

	private void updatePath(String pageP, String name){
		for (int i = 0; i < PP.size(); i++) {
			if (PP.get(i).equals(pageP)){
				PP.set(i, nameToPath(name));
			}

		}
	}

	private String nameToPath(String name){
		for (File file : ppFiles) {
			if (file.getName().equals(name)) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}

	private void checkEncodage() throws FileSystemException {
		BufferedReader br = null;
		File tmp = new File("tmp.csv");
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(sourceFile));
			bw = new BufferedWriter(new FileWriter(tmp));

			String ligne = "";

			while ((ligne = br.readLine()) != null){
				if (ligne.contains("?") || ligne.contains("‚") || ligne.contains("…") || 
						ligne.contains("‡") || ligne.contains("ˆ") || ligne.contains("‰") || ligne.contains("Š")  || ligne.contains("—")
						|| ligne.contains("“")  || ligne.contains("–")  || ligne.contains("Œ")  || ligne.contains("‹")) {
					String l = ligne;
					if (ligne.contains("?"))
						l = l.replaceAll("\\?", "’");
					if (ligne.contains("‚"))
						l = l.replaceAll("‚", "é");
					if (ligne.contains("…"))
						l = l.replaceAll("…", "à");
					if (l.contains("‡"))
						l = l.replaceAll("‡", "ç");
					if (l.contains("ˆ"))
						l = l.replaceAll("ˆ", "ê");
					if (l.contains("‰"))
						l = l.replaceAll("‰", "ë");
					if (l.contains("Š"))
						l = l.replaceAll("Š", "è");
					if (l.contains("—"))
						l = l.replaceAll("—", "ù");
					if (l.contains("“"))
						l = l.replaceAll("“", "ô");
					if (l.contains("–"))
						l = l.replaceAll("–", "û");
					if (l.contains("Œ"))
						l = l.replaceAll("Œ", "î");
					if (l.contains("‹"))
						l = l.replaceAll("‹", "ï");
					if (l.contains("ƒ"))
						l = l.replaceAll("ƒ", "â");
					if (l.contains("ø"))
						l = l.replaceAll("ø", "°");
					bw.write(l+"\r\n");
				}
				else
					bw.write(ligne+"\r\n");
			}
			bw.close();
			br.close();
		} catch (IOException e) {
			try {
				bw.close();
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();

		}
		try {
			Principale.fileMove(tmp, sourceFile);
		} catch (FileSystemException fse){
			throw fse;
		}
	}

	private void update(){
//		setChanged();
//		notifyObservers();
	}

//	@Override
//	public boolean isRunning() {
//		return running;
//	}

	@Override
	public void setRunning(boolean b) {
		this.running = b;
	}

	@Override
	public void onProgressBarDispose() {
		//Ne rien faire
	}

	@Override
	public String getFichierTraitement() {
		return fileProcessing;
	}
	
	@Override
	public String getTitre() {
		return "Association automatique des pages principales";
	}	
}
