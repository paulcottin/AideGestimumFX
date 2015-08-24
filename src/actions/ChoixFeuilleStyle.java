package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ParametrageError;
import interfaces.Action;

public class ChoixFeuilleStyle extends Action {

	private String stylePath;
	public ChoixFeuilleStyle(ArrayList<File> files){
		super(files);
		intitule = "Choix d'une feuille de style";
		messageFin = "Feuille de style appliquée";
	}
	

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements style = doc.select("link[rel=StyleSheet]");
			for (Element element : style) {
				element.attr("href", stylePath);
			}
		return doc;
	}

	@Override
	public void parametrer() throws ParametrageError {
		stylePath = cssFile("Paramétrage", "Quelle feuille de style").getAbsolutePath();
	}
}
