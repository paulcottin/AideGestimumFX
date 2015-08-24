package vues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import exceptions.AideException;

public class Aide extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String aideHTML;
	private JEditorPane pane;
	private HTMLEditorKit kit;
	
	public Aide() throws AideException {
		aideHTML = "Erreur d'affichage...\nVérifiez que le fichier aide.html est présent dans le dossier du .jar et qu'il n'est pas vide.";
		try {
			lireAide();
		} catch (IOException e) {
			throw new AideException("Erreur lors de la lecture du fichier aide.html");
		}
		pane = new JEditorPane();
		kit = new HTMLEditorKit();
		initAide();
	}
	
	private void initAide(){
		this.setVisible(true);
		this.setSize(1200, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pane.setEditable(false);
		initCSS();
		pane.setEditorKit(kit);
		Document doc = kit.createDefaultDocument();
		pane.setDocument(doc);
		pane.setContentType("text/html");
		pane.setText(aideHTML);
		this.setContentPane(new JScrollPane(pane));
	}
	
	private void initCSS(){
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("p { margin-left: 5.75pt;"
				+ "margin-right: 6.50pt;"
				+ "margin-top: 4.00pt;"
				+ "margin-bottom: 0.00pt;"
				+ "text-align: left;"
				+ "font-family: \"Open Sans Light\";"
				+ "font-size: 13pt;"
				+ "color: #000000;"
				+ "}");
		styleSheet.addRule("H1 { margin-left: 5.75pt;"
				+ "margin-right: 6.50pt;"
				+ "margin-top: 6.00pt;"
				+ "margin-bottom: 2.00pt;"
				+ "text-align: left;"
				+ "font-weight: bold;"
				+ "font-family: \"Open Sans Light\";"
				+ "color: #f2771e;"
				+ "font-size: 18pt;"
				+ "}");
		styleSheet.addRule("H2 {margin-left: 5.75pt; margin-left: 5.75pt; "
				+ "margin-right: 6.50pt;"
				+ "margin-top: 6.00pt;"
				+ "margin-bottom: 2.00pt;"
				+ "text-align: left;"
				+ "font-size: 16pt;"
				+ "color: black;"
				+ "font-weight: bold;"
				+ "font-variant: small-caps;"
				+ "font-family: \"Open Sans Light\";"
				+ "}");
		kit.setStyleSheet(styleSheet);
	}
	
	private void lireAide() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("aide.html")), "utf-8"));
		String ligne = "";
		aideHTML = "";
		while ((ligne = br.readLine()) != null) {
			aideHTML += ligne;
		}
		
		br.close();
	}

}
