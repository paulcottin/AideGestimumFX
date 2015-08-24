package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.NoCSSDefine;
import exceptions.NoPPDefine;
import exceptions.ParametrageError;
import interfaces.Action;

public class ColorationPuces extends Action {

	ArrayList<String> sansPP;
	NoPPDefine noPPDefine;
	NoCSSDefine noCSSDefine;
	boolean isOrange;
	String couleurTexte;
	String puceClasse;

	public ColorationPuces(ArrayList<File> files) {
		super(files);
		intitule = "Colorer les puces";
		messageFin = "Coloration des puces finies";
		sansPP = new ArrayList<String>();	
		noPPDefine = new NoPPDefine();
		noCSSDefine = new NoCSSDefine();
	}

	@Override
	public void parametrer() throws ParametrageError {	
		String[] couleur = {"Classe CSS", "Thématique"};
		String nom = (String)JOptionPane.showInputDialog(null, "Choisissez la couleur:", "Choisir la couleur des puces", JOptionPane.QUESTION_MESSAGE,  null, couleur, couleur[0]);
		if (nom != null) {
			isOrange = nom.equals("Classe CSS");
			puceClasse = cssClass(cssFile("Paramétrage de la coloration des puces", "Quelle feuille de style ?"), "Paramétrage de la"
					+ "coloration des puces", "Quelle classe CSS ?");
		}
		else
			throw new ParametrageError("Il faut sélectionner une couleur !");
	}

	private Document applyThematique(Document doc) throws IOException, NullPointerException {
		String couleur = "#ffffff";
		try {
			couleur = getCouleur(doc);
		} catch (NullPointerException e) {
			noPPDefine.add(doc.title());
		}

		Elements puces = doc.select("li");

		for (Element element : puces) {
			for (Attribute a : element.attributes()) {
				element.removeAttr(a.getKey());
			}
			element.attr("style", "color: "+couleur);
			if (isCleannable(element)) {
				element.html("<p style=\"color: "+couleurTexte+";\">"+element.text()+"</p>");
			}
		}

		return doc;
	}

	private Document applyOrange(Document doc) throws IOException, NullPointerException {
		Elements puces = doc.select("li");
		for (Element element : puces) {
			for (Attribute a : element.attributes()) {
				element.removeAttr(a.getKey());
			}
			for (String string : element.classNames()) {
				element.removeClass(string);
			}
			element.addClass(puceClasse);
			if (isCleannable(element)) {
				element.html("<p style=\"color: "+couleurTexte+";\">"+element.text()+"</p>");
			}
		}

		return doc;
	}
	@Override
	protected Document applyStyle(Document doc) throws IOException, NullPointerException {
		couleurTexte = getCouleurTexte(doc);
		if (isOrange) {
			return applyOrange(doc);
		}else {
			return applyThematique(doc);
		}
	}

	private String getCouleur(Document doc) throws IOException, NullPointerException {
		Elements pp = doc.select("meta[name=template]");
		if (pp.size() > 0){
			String path = getAbsolutePathPP(pp.first().attr("content"));
			Document d = Jsoup.parse(new File(path), "utf-8");
			Elements couleur = d.select("div");
			Element c = couleur.first();
			String l = c.toString();
			int deb = l.indexOf("<div style=\"background-color: ") + "<div style=\"background-color: ".length();
			int fin = l.substring(deb+1).indexOf(";")+1+deb;
			return l.substring(deb, fin);
		}else
			return null;
	}

	/**
	 * Récupère le path d'une page principale en fonction de son nom ou un path
	 * @param path
	 * @return
	 */
	private String getAbsolutePathPP(String path){
		//Si un path est donné en paramètre on récupère le nom
		if (path.contains("\\")) {
			Pattern p = Pattern.compile("\\\\");
			String[] t = path.split(p.pattern());
			path = t[t.length-1];
		}
		//On renvoi le chemin absolu
		for (File file : ppFiles) {
			if (file.getName().equals(path)) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}

	private String getCouleurTexte(Document doc) {
		Elements c = doc.select("link[rel=StyleSheet]");
		if (c.size() == 0)
			return "black";
		String css = c.first().attr("href");
		if (css == null)
			noCSSDefine.add("Aucune feuille de style définie pour \""+doc.title()+"\"");
		css = getFullCSSPath(css);
		for (File file : cssFiles) {
			if (file.getName().equals(css))
				css = file.getAbsolutePath();
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(css)), "utf-8"));
			String ligne = "";
			while ((ligne = br.readLine()) != null) {
				if (ligne.contains("p {")) {
					while (!(ligne = br.readLine()).contains("}")) {
						if (ligne.contains("color: ")){
							br.close();
							return ligne.substring(ligne.indexOf(":")+2, ligne.length()-1);
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		noCSSDefine.add("L'attribut de couleur n'a pas été trouvé ! <br/> Veuillez le définir dans la feuille CSS (page \""+css+"\")");
		return "black";
	}

	public ArrayList<String> getSansPP() {
		return sansPP;
	}

	public void setSansPP(ArrayList<String> sansPP) {
		this.sansPP = sansPP;
	}

	public NoPPDefine getNoPPDefine() {
		return noPPDefine;
	}

	public void setNoPPDefine(NoPPDefine noPPDefine) {
		this.noPPDefine = noPPDefine;
	}

	public NoCSSDefine getNoCSSDefine() {
		return noCSSDefine;
	}

	public void setNoCSSDefine(NoCSSDefine noCSSDefine) {
		this.noCSSDefine = noCSSDefine;
	}
}
