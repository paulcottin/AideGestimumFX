package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import exceptions.ParametrageError;
import interfaces.Action;

public class Style extends Action {

	private String mot, style;

	public Style(ArrayList<File> files){
		super(files);
		mot = "";
		style = "";
		intitule = "Application d'un style à un mot";
	}
	
	@Override
	public void parametrer() throws ParametrageError{
		getMot();
		style = cssClass(cssFile("Donner un style à un mot", "Quel style ?"), "Donner un style à un mot", "Quelle feuille de style");
		messageFin = "Le style \""+style+"\" a bien été appliqué sur le mot \""+mot+"\""; 
	}
	
	@Override
	protected Document applyStyle(Document doc) throws IOException {
		String html = doc.html();
		html = html.replace(mot, "<span class=\""+style+"\">"+mot+"</span>");
		doc = Jsoup.parse(html);
		return doc;
	}
	
	private void getMot() throws ParametrageError{
		mot = JOptionPane.showInputDialog(null, "<html>Quel mot ?<br/>(Attention à la casse + pas d'accent)</html>", 
				"Paramétrage", JOptionPane.QUESTION_MESSAGE);
		if (mot == null || mot.equals(""))
			throw new ParametrageError("Veuillez rensignez un mot non vide !");
	}
}
