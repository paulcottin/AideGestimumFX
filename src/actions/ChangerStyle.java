package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ParametrageError;
import interfaces.Action;

public class ChangerStyle extends Action {

	private String oldStyle, newStyle, oldStylePath, newStylePath;


	public ChangerStyle(ArrayList<File> files) {
		super(files);
		this.oldStyle = null;
		this.newStyle = null;
		intitule = "Changer d'un style à un autre";
		messageFin = "Changement de style effectué";
	}
	
	@Override
	public void parametrer() throws ParametrageError{
		correspondaceStyles();
	}
	
	@Override
	protected Document applyStyle(Document doc) throws IOException {	
			if (isBalise(oldStyle)) {
				Elements es = doc.select(oldStyle);
				if (isBalise(newStyle)) {
					for (Element element : es) {
						element.tagName(newStyle);
						for (Attribute a : element.attributes()) {
							element.removeAttr(a.getKey());
						}
						for (String s : element.classNames()) {
							element.removeClass(s);
						}
						if (isCleannable(element))
							element.text(element.text());
					}
				}
				else {
					
					for (Element element : es) {
						for (String s : element.classNames()) {
							element.removeClass(s);
						}
						element.addClass(newStyle);
						if (isCleannable(element))
							element.text(element.text());
					}
				}
			}
			else {
				Elements es = doc.select("."+oldStyle);
				if (isBalise(newStyle)) {
					for (Element element : es) {
						element.tagName(newStyle);
						for (Attribute a : element.attributes()) {
							element.removeAttr(a.getKey());
						}
						for (String s : element.classNames()) {
							element.removeClass(s);
						}
						if (isCleannable(element))
							element.text(element.text());
					}
				}
				else {
					
					for (Element element : es) {
						for (String s : element.classNames()) {
							element.removeClass(s);
						}
						element.addClass(newStyle);
						if (isCleannable(element))
							element.text(element.text());
					}
				}
			}
			
		return doc;
	}

	/**
	 * On demande à l'utilisateur quelles feuilles de styles il choisit et quelle classe dans ces feuilles
	 * @throws ParametrageError 
	 */
	private void correspondaceStyles() throws ParametrageError{
		oldStyle = cssClass(cssFile("Paramétrage", "Feuille de style de départ"), "Paramétrage", "Classe de départ");
		newStyle = cssClass(cssFile("Paramétrage", "Feuille de style d'arrivée"), "Paramétrage", "Classe d'arrivée");
	}

	/**
	 * 
	 * @param style
	 * @return vrai si le style est une balise, faux si c'est une classe CSS définie par l'utilisateur
	 */
	private boolean isBalise(String style){
		if (style.equals("p") || style.matches("(h|H)[0-9]"))
			return true;
		else return false;
	}

	public String getOldStyle() {
		return oldStyle;
	}

	public void setOldStyle(String oldStyle) {
		this.oldStyle = oldStyle;
	}

	public String getNewStyle() {
		return newStyle;
	}

	public void setNewStyle(String newStyle) {
		this.newStyle = newStyle;
	}

	public String getOldStylePath() {
		return oldStylePath;
	}

	public void setOldStylePath(String oldStylePath) {
		this.oldStylePath = oldStylePath;
	}

	public String getNewStylePath() {
		return newStylePath;
	}

	public void setNewStylePath(String newStylePath) {
		this.newStylePath = newStylePath;
	}
}
