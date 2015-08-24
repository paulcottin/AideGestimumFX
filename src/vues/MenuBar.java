package vues;

import java.util.Observable;
import java.util.Observer;

import controleurs.AfficheAide;
import controleurs.ConvertImg;
import controleurs.ExportTaille;
import controleurs.ExporterCSV;
import controleurs.ModifID;
import controleurs.ModifierNomFichiers;
import controleurs.NoHelp;
import controleurs.Quitter;
import controleurs.SearchCSS;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import main.Principale;



/**
 * Barre de menu
 * @author paul
 *
 */
public class MenuBar extends javafx.scene.control.MenuBar implements Observer{

	private Principale p;

	private Menu fichier, actions, aide;
	private MenuItem quitter, enregistrer, modifNomFichiers, rechercheCSS, voirAide, exportTaille, noHelp,
						modifID, convertImg;
	
	public MenuBar(Principale p) {
		super();
		this.p = p;
		init();
		construct();
	}
	
	private void init(){
		fichier = new Menu("Fichier");
		quitter = new MenuItem("Quitter");
		quitter.addEventHandler(ActionEvent.ACTION, new Quitter());
		
		enregistrer = new MenuItem("Exporter la liste des fichiers du projet RoboHelp");
		enregistrer.addEventHandler(ActionEvent.ACTION, new ExporterCSV(p));
		exportTaille = new MenuItem("Exporter la totalité des fichiers avec leurs tailles");
		exportTaille.addEventHandler(ActionEvent.ACTION, new ExportTaille(p.getFiles()));
		
		actions = new Menu("Actions");
		modifNomFichiers = new MenuItem("Modifier les noms de fichiers/dossiers");
		modifNomFichiers.addEventHandler(ActionEvent.ACTION, new ModifierNomFichiers(p));
		modifNomFichiers.setDisable(true);
		rechercheCSS = new MenuItem("Recherche d'un CSS en fonction d'un nom de classe");
		rechercheCSS.addEventHandler(ActionEvent.ACTION, new SearchCSS(p.getFiles()));
		
		noHelp = new MenuItem("Check des fenêtres qui n'ont pas de page d'aide");
		noHelp.addEventHandler(ActionEvent.ACTION, new NoHelp());
		
		modifID = new MenuItem("Modification des id d'aide des rubriques");
		modifID.addEventHandler(ActionEvent.ACTION, new ModifID(p.getFiles()));
		
		convertImg = new MenuItem("Convertir les images png en jpeg");
		convertImg.addEventHandler(ActionEvent.ACTION, new ConvertImg(p.getFiles()));
		
		aide = new Menu("?");
		voirAide = new MenuItem("Aide");
		voirAide.addEventHandler(ActionEvent.ACTION, new AfficheAide());
		
	}
	
	private void construct(){
		fichier.getItems().add(enregistrer);
		fichier.getItems().add(exportTaille);
		fichier.getItems().add(new SeparatorMenuItem());
		fichier.getItems().add(quitter);
		
		actions.getItems().add(modifNomFichiers);
		actions.getItems().add(rechercheCSS);
		actions.getItems().add(noHelp);
		actions.getItems().add(modifID);
		actions.getItems().add(convertImg);
		
		aide.getItems().add(voirAide);
		
		this.getMenus().add(fichier);
		this.getMenus().add(actions);
		this.getMenus().add(aide);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
	}

}