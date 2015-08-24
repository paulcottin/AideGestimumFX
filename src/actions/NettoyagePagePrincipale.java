package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import interfaces.Action;

public class NettoyagePagePrincipale extends Action{

	public NettoyagePagePrincipale(ArrayList<File> files) {
		super(files);
		intitule = "Nettoyage des pages principales";
		messageFin = "Nettoyage des pages principales terminé";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		//Nettoie les pages principales
		Elements pps = doc.select("meta[name=template][content=null]");
		pps.addAll(doc.select("meta[name=template][content=0]"));
		
		for (Element element : pps) {
			element.remove();
		}
		
		//Nettoie les "breadcrumbs"
		String html = doc.outerHtml();
		if (html.contains("placeholder") && html.contains("breadcrumbs")) {
			int deb = html.indexOf("<!--?rh-placeholder type=\"breadcrumbs\"");
			int fin = html.substring(deb).indexOf("?-->") + deb + "?-->".length()+1;
			String s = html.substring(0, deb);
			s += html.substring(fin);
			doc.html(s);
		}
		
		return doc;
	}

	@Override
	public void parametrer() {

	}

}
