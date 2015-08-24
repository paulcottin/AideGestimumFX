package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import interfaces.Action;

public class SupprStyleTitre extends Action{

	public SupprStyleTitre(ArrayList<File> files) {
		super(files);
		intitule = "Suppression des styles additionnels des titres";
		messageFin = "Fin de la suppression des styles pour les titres";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		doc = supprImgTitre(doc);
		Elements titres = doc.select("h1");
		titres.addAll(doc.select("h2"));
		titres.addAll(doc.select("h3"));
		titres.addAll(doc.select("h4"));
		titres.addAll(doc.select("h5"));
		
		for (Element element : titres) {
			for (Attribute a: element.attributes()) 
				element.removeAttr(a.getKey());
			for (String s : element.classNames())
				element.removeClass(s);
			if (isCleannable(element))
				element.text(element.text());
			
			//Si le titre commence par autre chose que par une lettre (ex : 1), 1-, 1., ...) on supprime pour qu'il
			//commence par une lettre
			if (!element.text().matches("^[a-zA-Z].*") && isCleannable(element)) {
				element.text(supprPuces(element.text()));
			}
		}
		//On permet l'affichage des puces définies dans le CSS pour les titres
		doc = createPuceTitre(doc);
		return doc;
	}
	
	/**
	 * Il arrive que dans certaines rubriques des images soient insérées dans une rubrique de titre (ex : <h1><img dgsrth/></h1>
	 * Pour que la numérotation des titres soit juste il faut les en enlever. C'est ce que fait cette fonction. 
	 * @param doc
	 * @return : le document modifié : sans les images dans les titres
	 */
	private Document supprImgTitre(Document doc){
		Elements titre = doc.select("h1");
		for (Element element : titre) {
			if (element.text().equals("")  && !isCleannable(element) && element.html().contains("<img")) {
				String txt = element.html().substring(element.html().indexOf("<img"));
				element.tagName("img");
				element.attr("src", supprImgTitreHelper(txt, "src"));
				element.attr("alt", supprImgTitreHelper(txt, "alt"));
				element.attr("width", supprImgTitreHelper(txt, "width"));
				element.attr("height", supprImgTitreHelper(txt, "height"));
				element.attr("border", supprImgTitreHelper(txt, "border"));		
				element.html("");
			}
		}
		return doc;
	}
	
	private String supprImgTitreHelper(String txt, String attr){
		if (txt.contains(attr)) {
			int deb = txt.indexOf(attr+"=\"") + (attr+"=\"").length();
			//Si l'attribut est vide
			if (txt.substring(deb, deb+1).equals("\""))
				return "";
			String src = txt.substring(deb, txt.substring(deb+1).indexOf("\"")+deb+1);
			return src;
		}
		return "";
	}
	
	private String supprPuces(String txt){
		if (txt.length() > 1) {
			int index = 1;
			//Tant qu'on ne tombe pas sur une lettre
			while (index < txt.length()-1 && !txt.substring(index, index+1).matches("\\p{Lu}|\\p{L}"))
				index++;
			if (index < txt.length())
				txt = txt.substring(index);
			txt = txt.substring(0, 1).toUpperCase()+txt.substring(1).toLowerCase();
		}
		return txt;
	}
	
	private Document createPuceTitre(Document doc){
		for (int i = 1; i < 6; i++) {
			Elements titre = doc.select("h"+i);
			for (Element element : titre) {
				if (element.previousSibling() != null && !element.previousSibling().outerHtml().contains("rh-list_start")) { 
					element.prepend("<?rh-list_start level=\""+i+"\" an=\""+i+"\" class=\"rl-H"+i+"\" style=\"list-style: rh-list;"
							+ "list-style: rh-list;\" ?>");
					element.append("<?rh-list_end ?>");
				}
				
			}
		}
		return doc;
	}

	@Override
	public void parametrer() {
		
	}

}
