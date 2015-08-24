package utilitaires;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import exceptions.BadFile;
import exceptions.ParametrageError;
import interfaces.LongTask;
import main.Principale;
import vues.ProgressBar;

public class Utils extends Observable implements LongTask {

	private boolean running;
	private String fichierTraitement;

	public Utils() {
		running = false;
		fichierTraitement = "";
	}

	public void generateFileSize(ArrayList<File> files) {
		String s = "";
		for (File file : files)
			s += file.getAbsolutePath()+";"+(file.length()/1024)+"K\r\n";

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(chooseSaveFile()));
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			Principale.messageFin("Erreur pendant l'exportation");
			e.printStackTrace();
		} catch (ParametrageError e) {
			e.printMessage();
		}

		Principale.messageFin("Fichier exporté !");
	}

	public File chooseSaveFile() throws ParametrageError{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Enregistrer");
		fileChooser.setDialogTitle("Sauvegarde du fichier");

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}else
			throw new ParametrageError("Il faut donner un nom de fichier pour l'enregistrement !");
	}

	public File chooseFolder(String titre){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(getCurrentDir());
		fileChooser.setDialogTitle(titre);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			return new File(f.getAbsolutePath());
		}
		else
			return null;
	}

	public void checkNoHelpWindows() {
		new Thread(){
			public void run() {
				try {
					checkNoHelpWindowsHelper();
				} catch (ParametrageError e) {
					e.printMessage();
				}
			}
		}.start();
		//new ProgressBar(this);
	}

	private void checkNoHelpWindowsHelper() throws ParametrageError{
		File folder = chooseFolder("Dossier contenant le code source Delphi de l'application");
		if (folder == null)
			throw new ParametrageError("Il faut donner un dossier !");
		running = true;
		ArrayList<File> list = listerRepertoire(folder);
		String msg = "Ce sont les pages : <br/><ul>";
		int cpt = 0;

		for (File file : list) {
			fichierTraitement = file.getName();
			setChanged();notifyObservers();
			//Si c'est une fenêtre
			if (file.getAbsolutePath().endsWith(".pas") && sameFileName(list, file.getName().substring(0, file.getName().length()-3)+"dfm")) {
				boolean find = false;
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String ligne = "";
					while ((ligne = br.readLine()) != null) {
						//Si on trouve la mention "HelpContext :=" alors il y a une page d'aide fixée, sinon non.
						if (ligne.contains("HelpContext :="))
							find = true;
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!find){
					msg += "<li>"+file.getPath()+"</li>";
					cpt++;
				}
			}
		}
		running = false;
		setChanged();notifyObservers();
		msg += "</ul>";
		msg = "Au total, "+cpt+" fenêtres n'ont pas d'id d'aide<br/>" + msg;

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(chooseSaveFile()));
			bw.write(msg);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Principale.messageFin("Génération du fichier résultat terminée");
	}

	private boolean sameFileName(ArrayList<File> fileList, String path) {
		for (File file : fileList) 
			if (file.getName().equals(path)) 
				return true;
		return false;
	}

	/**
	 * Génère une chaîne de caractère pour le nom d'un fichier temporaire.
	 * @param length : longueur de la chaine
	 * @param chars : Chaîne de caractère servant de base à la génération.
	 * @return
	 */
	public String generateString(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder  pass = new StringBuilder (chars.length());
		for (int x = 0; x < length; x++) {
			int i = (int) (Math.random() * chars.length());
			pass.append(chars.charAt(i));
		}
		return pass.toString();
	}

	public void pngToJpeg(ArrayList<File> pngFiles) throws ParametrageError {
		new Thread(){
			public void run() {
				running = true;
				setChanged();notifyObservers();
				pngToJpegHelper(pngFiles);
			}
		}.start();
		//new ProgressBar(this);
	}

	private void pngToJpegHelper(ArrayList<File> pngFiles) {
		for (File file : pngFiles) {
			fichierTraitement = file.getName();
			setChanged();notifyObservers();
			BufferedImage bufferedImage;
			try {
				//read image file
				bufferedImage = ImageIO.read(file);
				// create a blank, RGB, same width and height, and a white background
				BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
						bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
				newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

				// write to jpeg file
				ImageIO.write(newBufferedImage, "jpg", file);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		running = false;
		setChanged();notifyObservers();
		Principale.messageFin("Les images png ont bien été convertie en jpg");
	}


	public void changeId(ArrayList<File> files) throws ParametrageError, IOException, JDOMException, BadFile{
		ArrayList<String> olds = new ArrayList<String>(), news = new ArrayList<String>();
		//On récupère le fichier ALI et tous les fichiers HH pour le traitement
		File ali = null;
		ArrayList<File> hh = new ArrayList<File>();
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".ali"))
				ali = file;
			else if (file.getAbsolutePath().endsWith(".hh") || file.getAbsolutePath().endsWith("HH"))
				hh.add(file);

		}
		if (ali == null)
			throw new ParametrageError("Aucun fichier ALI présent dans le dossier de projet");
		else if (hh.size() == 0)
			throw new ParametrageError("Aucun fichier HH trouvé dans le dossier du projet");

		//Récupération du fichier CSV
		Principale.messageFin("Veuillez donner le chemin du fichier CSV contenant dans la première colonne les anciens ids, "
				+ "dans la seconde les nouveaux.");
		File csv = chooseFile("Sélection du fichier CSV");
		if (!csv.getAbsolutePath().endsWith(".csv"))
			throw new BadFile("Il faut donner un fichier CSV !");
		//Parsing du fichier CSV
		BufferedReader br = new BufferedReader(new FileReader(csv));
		String ligne = "";
		while ((ligne = br.readLine()) != null) {
			if (ligne.contains(";")) {
				String[] tab = ligne.split(";");
				olds.add(tab[0]);
				news.add(tab[1]);
			}
		}
		br.close();

		//Modifications du fichier ALI
		modificationDocumentALI(olds, news, ali);
		//Modification des fichiers HH
		for (File file : hh) {
			modificationDocumentHH(olds, news, file);
		}

		Principale.messageFin("Les ids d'aide ont bien été mis à jour");
	}

	private void modificationDocumentALI(ArrayList<String> olds, ArrayList<String> news, File file) throws BadFile, JDOMException, IOException {
		SAXBuilder sxb = new SAXBuilder();
		Document doc = sxb.build(file);
		if (!doc.hasRootElement())
			throw new BadFile("Le fichier ali n'est pas correctement formé !");
		if (!doc.getRootElement().getName().equals("aliaslist"))
			throw new BadFile("Le fichier ali n'est pas correctement formé !");
		for (Element element : doc.getRootElement().getChildren()) {
			if (olds.contains(element.getAttributeValue("name"))) {
				int index = olds.indexOf(element.getAttributeValue("name"));
				element.setAttribute("name", news.get(index));
			}
		}
		File tmp = new File(generateString(5)+".jlb");
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		try {
			FileOutputStream fos = new FileOutputStream(tmp);
			sortie.output(doc, fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Principale.fileMove(tmp, file);
		tmp.delete();
	}

	private void modificationDocumentHH (ArrayList<String> olds, ArrayList<String> news, File file) throws IOException, BadFile {
		BufferedReader br = new BufferedReader(new FileReader(file));
		File tmp = new File(generateString(5)+".jlb");
		BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
		String ligne = "";

		while ((ligne = br.readLine()) != null) {
			String[] tab = null;
			String id = "", num = "";
			try {
				if (!ligne.equals("")) {
					tab = ligne.split(" ");
					id = tab[1];
					num = tab[2];
				}
			} catch (IndexOutOfBoundsException | NullPointerException e) {
				br.close();
				bw.close();
				throw new BadFile("Le fichier HH est mal formé ! <br/>ligne : "+ligne);
			}
			if (olds.contains(id)){
				int index = olds.indexOf(id);
				bw.write("#define "+news.get(index)+" "+num+"\r\n");
			}
			else
				bw.write(ligne+"\r\n");
		}

		br.close();
		bw.close();

		Principale.fileMove(tmp, file);
		tmp.delete();
	}

	public File chooseFile(String titre) throws ParametrageError {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
			return fileChooser.getSelectedFile();
		else
			throw new ParametrageError("Il faut sélectionner un fichier !");
	}

	public File getCurrentDir(){
		String tmp = System.getProperty("user.dir");
		return new File(tmp);
	}

	public ArrayList<File> listerRepertoire(File topics){ 
		ArrayList<File> files = new ArrayList<File>();
		File[] listefichiers; 

		listefichiers=topics.listFiles();
		for(int i=0;i<listefichiers.length;i++){ 
			//Spécifie un fichier de base pour traiter les autres liens en relatif
			if (listefichiers[i].getAbsolutePath().endsWith(".xpj"))
				Principale.FILE_BASE = listefichiers[i];

			if (listefichiers[i].isDirectory() && !listefichiers[i].getAbsolutePath().contains("!"))
				files.addAll(listerRepertoire(listefichiers[i]));
			else if (!listefichiers[i].isDirectory())
				files.add(listefichiers[i]);
		}
		return files;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean b) {
		running = b;
	}

	@Override
	public void onProgressBarDispose() {
		//Ne rien faire
	}

	@Override
	public String getFichierTraitement() {
		return fichierTraitement;
	}

	@Override
	public String getTitre() {
		return "Recherche des fenêtres n'ayant pas d'id d'aide";
	}

}
