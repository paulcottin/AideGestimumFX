package vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import interfaces.Action;
import main.Script;

public class ScriptChooser extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JList<String> actions, choix;
	ArrayList<String> actionsChoisies;
	JButton ajouter, retirer, ok, annuler;
	DefaultListModel<String> actionsModel, choixModel;
	Script script;
	
	public ScriptChooser(Script script) {
		this.setTitle("Choix du script");
		this.setSize(800, 500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.actionsChoisies = new ArrayList<String>();
		this.script = script;
		this.getContentPane().setLayout(new BorderLayout());
		
		init();
	}
	
	private void init(){
		actionsModel = new DefaultListModel<String>();
		for (Action a : script.getPrincipale().getScripts()) {
			actionsModel.addElement(a.getIntitule());
		}
		actions = new JList<String>(actionsModel);
		actions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		actions.setBackground(Color.white);
		actions.setSelectedIndex(0);
		choixModel = new DefaultListModel<String>();
		choix = new JList<String>(choixModel);
		choix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		choix.setBackground(Color.white);
		
		ajouter = new JButton("Ajouter");
		ajouter.addActionListener(new AjouterListener());
		retirer = new JButton("Retirer");
		retirer.addActionListener(new RetirerListener());
		ok = new JButton("OK");
		ok.addActionListener(new OKListener());
		annuler = new JButton("Annuler");
		annuler.addActionListener(new AnnulerListener());
		
		JPanel boutonsChoix = new JPanel();
		boutonsChoix.setLayout(new BoxLayout(boutonsChoix, BoxLayout.PAGE_AXIS));
		boutonsChoix.add(ajouter);
		boutonsChoix.add(retirer);
		
		JPanel okButton = new JPanel();
		okButton.add(ok);
		okButton.add(annuler);
		
		this.getContentPane().add(actions, BorderLayout.WEST);
		this.getContentPane().add(choix, BorderLayout.EAST);
		this.getContentPane().add(boutonsChoix, BorderLayout.CENTER);
		this.getContentPane().add(okButton, BorderLayout.SOUTH);
	}
	
	class AjouterListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!actions.isSelectionEmpty()) {
				int index = actions.getSelectedIndex();
				choixModel.addElement(actionsModel.getElementAt(index));
				choix.updateUI();
			}
		}
	}
	
	class RetirerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!choix.isSelectionEmpty()) {
				int index = choix.getSelectedIndex();
				choixModel.removeElementAt(index);
				choix.updateUI();
			}
		}
	}
	
	class OKListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (choixModel.size() > 0) {
				for (int i = 0; i < choixModel.size(); i++) {
					actionsChoisies.add(choixModel.getElementAt(i));
				}
				script.choisirActionHelper();
			}else
				JOptionPane.showMessageDialog(null, "Veuillez sélectionner au moins une action");
		}
	}
	
	class AnnulerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			actionsChoisies = null;
			dispose();
		}
	}

	public ArrayList<String> getActionsChoisies() {
		return actionsChoisies;
	}

	public void setActionsChoisies(ArrayList<String> actionsChoisies) {
		this.actionsChoisies = actionsChoisies;
	}

}
