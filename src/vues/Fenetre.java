package vues;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import controleurs.Lancer;
import controleurs.ParametrerScript;
import interfaces.Action;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import main.Principale;

public class Fenetre extends Scene implements Observer{

	private Principale principale;
	private ArrayList<Label> labels;
	private ArrayList<Button> selects, alls;
	
	Label script, assocAuto, rechercheImg;
	Button rechercheImg_l;
	Button script_all, assocAuto_all, rechercheImg_all;
	
	ListeFichier listeFichier;
	
	public Fenetre(Principale principale, Parent root, int x, int y) {
		super(root, x, y);
		
		this.principale= principale;
		this.listeFichier = new ListeFichier(principale.getFiles());
		this.labels = new ArrayList<Label>();
		this.selects = new ArrayList<Button>();
		this.alls = new ArrayList<Button>();
		
		initWin();
		createWin();
	}
	
	private void initWin(){
		
		script = new Label("Paramétrer un script");
		script_all = new Button("Paramétrer");
		script_all.addEventFilter(ActionEvent.ACTION, new ParametrerScript(principale.getScript()));
		
		assocAuto = new Label("Association automatique de page principales");
		assocAuto_all = new Button("Lancer");
		assocAuto_all.addEventFilter(ActionEvent.ACTION, new Lancer(principale.getAssociationAuto(), principale));
		
		rechercheImg = new Label("Recherche des chemins des images");
		rechercheImg_l = new Button("Sélection");
		rechercheImg_l.addEventFilter(ActionEvent.ACTION, new Lancer(principale.getRechercheImage(), principale, Lancer.SELECTED_LINES));
		rechercheImg_all = new Button("Tous");
		rechercheImg_all.addEventFilter(ActionEvent.ACTION, new Lancer(principale.getRechercheImage(), principale));
	}
	

	private void createWin(){
		for (Action action : principale.getScripts()) {
			labels.add(new Label(action.getIntitule()));
			Button b = new Button("Sélection");
			b.addEventHandler(ActionEvent.ACTION, new Lancer(action, principale, Lancer.SELECTED_LINES));
			selects.add(b);
			Button a = new Button("Tous");
			a.addEventHandler(ActionEvent.ACTION, new Lancer(action, principale));
			alls.add(a);
		}
		
		VBox vb = new VBox(5);
		for (int i = 0; i < labels.size(); i++) {
			FlowPane fp = new FlowPane(10, 2);

			fp.getChildren().add(labels.get(i));
			fp.getChildren().add(selects.get(i));
			fp.getChildren().add(alls.get(i));
			vb.getChildren().add(fp);
			
		}
		
		
		FlowPane scr = new FlowPane(10, 2);
		scr.getChildren().add(script);
		scr.getChildren().add(script_all);
		
		FlowPane assoc = new FlowPane(10, 2);
		assoc.getChildren().add(assocAuto);
		assoc.getChildren().add(assocAuto_all);
		
		FlowPane rechImg = new FlowPane(10, 2);
		rechImg.getChildren().add(rechercheImg);
		rechImg.getChildren().add(rechercheImg_l);
		rechImg.getChildren().add(rechercheImg_all);
		
		vb.getChildren().add(rechImg);
		vb.getChildren().add(assoc);
		vb.getChildren().add(scr);
		
		((BorderPane) this.getRoot()).setCenter(vb);
		((BorderPane) this.getRoot()).setTop(new MenuBar(principale));
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
	}
}
