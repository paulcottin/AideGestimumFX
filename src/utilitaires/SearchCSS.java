package utilitaires;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.Principale;

public class SearchCSS {
	
	private String nomClasse;
	private ArrayList<File> files, cssFiles;
	private ArrayList<String> resultList;
	
	public SearchCSS(ArrayList<File> files) {
		this.files = new ArrayList<File>();
		this.cssFiles = new ArrayList<File>();
		this.resultList = new ArrayList<String>();
		for (File file : files) {
			if (file.getAbsolutePath().endsWith(".css"))
				cssFiles.add(file);
			this.files.add(file);
		}
		
	}
	
	public void search(){
		resultList.clear();
		if (getClasse()) {
			for (File file : cssFiles) {
				resultList.addAll(SearchInCSS(file));
			}
			if (resultList.size() > 0) {
				printResults();
			}else
				Principale.messageFin("Aucun fichier CSS trouvé");
		}
	}
	
	private void printResults(){
		String s = "<html>Les pages CSS où sont présentes la classe\""+nomClasse+"<br/>";
		s += "<ul>";
		for (String string : resultList) {
			s += "<li>"+string+"</li>";
		}
		s += "</ul></html>";
		JOptionPane.showMessageDialog(null, s);
	}
	
	private boolean getClasse(){
		nomClasse = JOptionPane.showInputDialog(null, "Nom de la classe ?", 
				"Recherche de page CSS en fonction du nom de la classe", JOptionPane.QUESTION_MESSAGE);
		if (nomClasse != null) 
			return true;
		else
			return false;
	}
	
	private ArrayList<String> SearchInCSS(File f){
		ArrayList<String> reponse = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String ligne = "";
			while((ligne = br.readLine()) != null){
				if (ligne.contains("{")) {
					String classe = ligne.substring(0, ligne.indexOf("{")-1);
					if (classe.contains("."))
						classe = classe.split("\\.")[1];
					if (classe.equals(nomClasse)) {
						reponse.add(f.getName());
					}
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reponse;
	}

	public String getNomClasse() {
		return nomClasse;
	}

	public void setNomClasse(String nomClasse) {
		this.nomClasse = nomClasse;
	}

}
