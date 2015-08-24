package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import interfaces.Action;

public class SupprimerTitre extends Action {

	public SupprimerTitre(ArrayList<File> files) {
		super(files);
		messageFin = "Titres supprimés";
		intitule = "Supprimer les titres";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements b = doc.select("body");
		Element body = b.first();
		
		if (body.children().size() > 1) 
			if (body.children().first().tagName().equals("h1") && body.child(1).tagName().equals("p")) 
				body.children().first().remove();
		return doc;
	}

	@Override
	public void parametrer() {
		// Ne rien faire
	}
}
