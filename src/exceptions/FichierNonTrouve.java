package exceptions;

import java.util.ArrayList;

public class FichierNonTrouve extends MyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<String> pages;
	
	public FichierNonTrouve(){
		super();
		pages = new ArrayList<String>();
	}
	
	public FichierNonTrouve(String fileName) {
		super("Fichier \""+fileName+"\" non trouvé");
		pages = new ArrayList<String>();
		
	}
	
	public void printMessage(){
		displayMessage();
	}
	
	public void add(String page){
		pages.add(page);
	}
	public ArrayList<String> getPages() {
		return pages;
	}
	public void setPages(ArrayList<String> pages) {
		this.pages = pages;
	}
}
