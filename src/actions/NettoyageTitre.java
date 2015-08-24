package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import interfaces.Action;

public class NettoyageTitre extends Action {

	public NettoyageTitre(ArrayList<File> files) {
		super(files);
		intitule = "Correction des titres";
		messageFin = "Correction des titres terminée";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements h = doc.select("h1");
		for (Element element : h) {
			//Si le titre et la première phrase sont collés
			if (element.text().matches("(.* |^.*)[a-z]+[A-Z][a-z]*.*") && isCleannable(element)) {
				String[] tab = separeTitre(element.text());
				element.text(tab[0]);
				element.after("<p>"+tab[1]+"</p>");
			}
		}
		return doc;
	}

	@Override
	public void parametrer() {
		// TODO Auto-generated method stub
		
	}
	
	private String[] separeTitre(String s){
		String[] tab = s.split(" ");
		int sep = 0;
		for (int i = 0; i < tab.length; i++)
			if (tab[i].matches("[a-z]+[A-Z][a-z]*"))
				sep = i;
		String titre = "";
		for (int i = 0; i < sep; i++)
			titre += tab[i] + " ";
		int index = 0;
		for (int i = 0; i < tab[sep].length(); i++)
			if (tab[sep].substring(i, i+1).equals(tab[sep].toUpperCase().substring(i, i+1)))
				index = i;
		titre += tab[sep].substring(0, index);
		String texte = tab[sep].substring(index, tab[sep].length()) +" ";
		for (int i = sep+1; i < tab.length; i++)
			texte += tab[i] +" ";
		String[] result = {titre, texte};
		return result;
	}

}
