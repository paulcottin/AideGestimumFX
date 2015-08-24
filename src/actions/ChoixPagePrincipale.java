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

public class ChoixPagePrincipale extends Action {

	private String pagePath;

	public ChoixPagePrincipale(ArrayList<File> files) {
		super(files);
		messageFin = "Mise à jour de la page principale finie";
		intitule = "Choix de la page principale";
	}

	@Override
	public void parametrer() throws ParametrageError{
		pagePath = getPPPath();
	}

	@Override
	protected Document applyStyle(Document doc) throws IOException {
		Elements pp = doc.select("[name=template]");
		String html = doc.html();
		boolean isHeader = false, isFooter = false, isPP = false;
		isHeader = html.contains("placeholder type=\"header\"");
		isFooter = html.contains("placeholder type=\"footer\"");
		isPP = pp.size() > 0;
		if (isPP)
			pp.first().attr("content", pagePath);
		else {
			Elements meta = doc.select("meta");
			int index = meta.size();
			if (index > 0) {
				Element e = meta.first().clone();
				for (Attribute a: e.attributes()) {
					e.removeAttr(a.getKey());
				}
				e.attr("name", "template");
				e.attr("content", pagePath);
				Elements head = doc.select("head");
				Elements list = new Elements(e);
				head.first().insertChildren(index, list);
			}
		}
		if (!isFooter)
			doc.select("body").append("<!--?rh-placeholder type=\"footer\" ?--> ");
		if (!isHeader)
			doc.select("body").prepend("<!--?rh-placeholder type=\"header\" ?--> ");
		return doc;
	}

	public ArrayList<File> getHtmlFiles() {
		return htmlFiles;
	}

	public void setHtmlFiles(ArrayList<File> htmlFiles) {
		this.htmlFiles = htmlFiles;
	}

	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
}
