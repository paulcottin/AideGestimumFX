package actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import exceptions.NoPPDefine;
import exceptions.ParametrageError;
import interfaces.Action;

public class NoPP extends Action {

	private NoPPDefine exception;
	
	public NoPP(ArrayList<File> files) {
		super(files);
		intitule = "Rechercher les pages qui n'ont pas de page principale";
		messageFin = "Recherche terminée";
		exception = new NoPPDefine();
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements pp = doc.select("meta[name=template]");
		if (pp.size() == 0)
			exception.add(doc.title());
		return doc;
	}

	@Override
	public void parametrer() throws ParametrageError {
		
	}

	public NoPPDefine getPPException() {
		return exception;
	}

	public void setException(NoPPDefine exception) {
		this.exception = exception;
	}

}
