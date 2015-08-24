package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import interfaces.Action;

public class CreationPuce extends Action {

	ArrayList<String> puces;

	public CreationPuce(ArrayList<File> files) {
		super(files);
		intitule = "Cr�ation de puces";
		messageFin = "Cr�ation des puces termin�e";
		puces = new ArrayList<String>();
		puces.add("§");
		puces.add("Ø");
		puces.add("•");
		puces.add("·");
		puces.add("-");
		puces.add("ð");
		puces.add("▼");
		puces.add("þ");
	}

	@Override
	public void parametrer() {

	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements style = doc.select("p");
		for (int i = 0; i < style.size(); i++) {
			Element element = style.get(i);
			if (containsPuce(element.text())){
				String puce = getPuce(element.text());
				while (element != null && (containsPuce(element.text())) ){
					element.tagName("li");
					String txt = element.text();
					if (puce.equals("è") || puce.equals("o "))
						txt = txt.replaceFirst(puce, "");
					else
						txt = txt.replace(puce, "");
					element.text(txt);
					i++;
					element = i < style.size() ? style.get(i) : null;
				}
			}
		}

		String html = doc.html();
		String[] lignes = html.split("\n");
		boolean isList = false, isUl = false;
		for (int i = 0; i < lignes.length; i++) {
			if (lignes[i].contains("<ul>"))
				isUl = true;
			if ((lignes[i].contains("<li>") || lignes[i].contains("<li ")) && !isList && !isUl) {
				lignes[i] = "<ul>"+lignes[i];
				isList = true;
			}
			else if (!(lignes[i].contains("<li>") || lignes[i].contains("<li ")) && isList && !isUl){
				lignes[i] = "</ul>" + lignes[i];
				isList = false;
			}

			if (lignes[i].contains("</ul>"))
				isUl = false;
		}
		html = "";
		for (String string : lignes) {
			html += string + "\n";
		}
		doc = Jsoup.parse(html);
		doc = nettoiePuces(doc);
		return doc;
	}
	
	private Document nettoiePuces(Document doc) {
		Elements lis = doc.select("li");
		for (Element element : lis) {
			if (element.children().size() > 0) {
				if (element.child(0).tagName().equals("p"))
					if (element.child(0).text().equals(""))
						element.remove();
			}
			else if (element.text().equals("")) {
				element.remove();
			}
		}
		
		Elements uls = doc.select("ul");
		for (Element element : uls) {
			for (Attribute a: element.attributes()) {
				element.removeAttr(a.getKey());
			}
		}
					
		return doc;
	}

	private String getPuce(String text){
		int index = -1;
		for (String string : puces) {
			if (text.contains(string))
				index = text.indexOf(string);
		}
		if (text.matches("^è[A-Z].*"))
			index = text.indexOf("è");
		else if (text.startsWith("o "))
			index = text.indexOf("o");
		return text.substring(index, index+1);
	}

	private boolean containsPuce(String text){
		for (String string : puces) {
			if (text.contains(string) && !string.equals("-") && !string.equals("o"))
				return true;
			else if (text.startsWith("-") || text.startsWith("&nbsp;-") ||
					text.startsWith("o ") || text.startsWith("&nbsp;o "))
				return true;
		}
		if (text.matches("^è[A-Z].*")){
			return true;
		}
		return false;
	}
}
