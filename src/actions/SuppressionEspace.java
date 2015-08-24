package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import exceptions.ParametrageError;
import interfaces.Action;

public class SuppressionEspace extends Action{

	public SuppressionEspace(ArrayList<File> files) {
		super(files);
		intitule = "Suppression des espaces";
		messageFin = "Suppression des espaces finie";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		for (Element element: doc.getAllElements()) {
			//Si il y a un espace "non-breakable"
			if (element.html().contains("&nbsp;")) {
				element.html(element.html().replace("&nbsp;", ""));
			}
			//Si il y a une balise p vide
			else if (element.tagName().equals("p") && element.text().equals("") && isCleannable(element))
				element.remove();
		}
		return doc;
	}

	@Override
	public void parametrer() throws ParametrageError {
		
	}

}
