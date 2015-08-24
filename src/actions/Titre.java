package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import interfaces.Action;

public class Titre extends Action {
	
	public Titre(ArrayList<File> files) {
		super(files);
		intitule = "Création des titres";
		messageFin = "Titres créés";
	}

	@Override
	public void parametrer() {
		
	}
	
	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements titres = doc.select("[style*=border-bottom]");
		for (Element element : titres) {
			constructTitre(element);
		}
		
		Elements p = doc.select("p");
		for (Element element : p) {
			if (element.text().matches("^[0-9]-( )*( )*[A-Z].*"))
				constructTitre(element);
			else if (element.text().matches("^[0-9]\\) [A-Z].*") || element.text().matches("^[A-Z]\\. [A-Z].*"))
				constructTitre(element);
			else if (element.text().matches("^[0-9]\\. [A-Z].*") || element.text().matches("^[0-9]\\.[A-Z].*")) 
				constructTitre(element);
			else if (element.text().matches("([\\p{Lu}'’/\\(\\)]| )*")) 
				constructTitre(element);
		}
		
		Elements h1s = doc.select("h1");
		for (Element element : h1s) 
			constructTitre(element);
		return doc;
	}

	private void constructTitre(Element element){
		element.tagName("h1");
		for (Attribute a : element.attributes()) {
			element.removeAttr(a.getKey());
		}
		String text = element.text();
		if (text.length() > 1) {
			String s = text.substring(0,1);
			s = s.toUpperCase();
			s += text.substring(1, text.length()).toLowerCase();
			element.text(s);
		}
	}
}
