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

public class Copyright extends Action {
	
	private String copyright;

	public Copyright(ArrayList<File> files) {
		super(files);
		intitule = "Modification du Copyright";
		messageFin = "Copyright modifié";
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements cop = doc.select("meta[name=copyright]");
		if (cop.size() > 0) {
			Element c = cop.first();
			c.attr("value", copyright);
			return doc;
		}
		else {
			doc.head().append("<meta name=\"copyright\" value=\""+copyright+"\">");
			return doc;
		}
		
	}

	@Override
	public void parametrer() throws ParametrageError {
		copyright = JOptionPane.showInputDialog(null, "Quel copyright voulez-vous appliquer ?", "Paramétrage", JOptionPane.QUESTION_MESSAGE);
		if (copyright == null || copyright.equals(""))
			throw new ParametrageError("Il faut donner un copyright non vide !");
	}

}
