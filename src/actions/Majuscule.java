package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ParametrageError;
import interfaces.Action;

public class Majuscule extends Action {

	public Majuscule(ArrayList<File> files) {
		super(files);
		intitule = "Mise en majuscule";
		messageFin = "Mise en majuscule appliquée";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		//Gestion des titres
		Elements titres = doc.select("h1");
		for (int i = 2; i < 6; i++) {
			titres.addAll(doc.select("h"+i));
		}
		for (Element element : titres) {
			if (isCleannable(element))
				element.text(metEnMajuscule(element.text()));
		}
		
		//Gestion des puces
		Elements puces = doc.select("li");
		for (Element element : puces) {
			if (isCleannable(element))
				element.text(metEnMajuscule(element.text()));
		}
		//Gestion du texte
		Elements txts = doc.select("p");
		for (Element element : txts) {
			if (isCleannable(element))
				element.text(metEnMajuscule(element.text()));
		}
		return doc;
	}

	@Override
	public void parametrer() throws ParametrageError {
		
	}
	
	/**
	 * Met une majuscule la première lettre du texte de l'élément
	 * Si il y a un point (dans une phrase), vérifie qu'il y a un espace entre le point et la lettre suivante et met en maj celle-ci.
	 * @param element
	 */
	private String metEnMajuscule(String txt){
		if (txt.length() > 1) {
			//Maj du premier caractère
			//Si le premier caractère est une lettre, on met en maj
			if (txt.substring(0, 1).matches("[a-zA-Z]"))
				txt = txt.substring(0,1).toUpperCase()+txt.substring(1);
			//Sinon, on recherche la première lettre
			else {
				int index = 1;
				//Tant qu'on ne tombe pas sur une lettre
				while (index < txt.length()-1 && !txt.substring(index, index+1).matches("[A-Za-z]"))
					index++;
				if (index < txt.length())
					txt = txt.substring(0, index) + txt.substring(index,index+1).toUpperCase()+txt.substring(index+1);
			}
			
			//Maj du caractère après un point
			if (txt.contains(".")) {
				String[] tab = txt.split("\\.");
				for (int i = 0; i < tab.length; i++) {
					tab[i] = metEnMajuscule(tab[i]);
					//Check l'existence d'un espace après un .
					if (i > 0 && tab[i].length() > 1 && !tab[i].substring(0, 1).equals(" "))
						tab[i] = " " + tab[i];
				}
				txt = String.join(".", tab);
			}		
		}
		return txt;
	}

}
