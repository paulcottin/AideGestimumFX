package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.ParametrageError;
import interfaces.Action;

public class SupprimerBalise extends Action{

	private String balise;

	public SupprimerBalise(ArrayList<File> files) {
		super(files);
	}
	
	@Override
	public void parametrer() throws ParametrageError{
		balise = JOptionPane.showInputDialog(null, "Nom de la balise", "Supprimer des balises", JOptionPane.QUESTION_MESSAGE);
		if (balise == null)
			throw new ParametrageError("Il faut donner un nom de balise à supprimer !");
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements supps = doc.select(balise);
		for (Element element : supps) {
			element.remove();
		}
		return doc;
	}
}


