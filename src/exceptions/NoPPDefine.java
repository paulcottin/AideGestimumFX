package exceptions;

import java.util.ArrayList;

public class NoPPDefine extends MyException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> pages;
	
	public NoPPDefine() {
		super();
		this.pages = new ArrayList<String>();
		
	}
	
	public void display(){
		String s = "Les pages suivantes n'ont pas de pages principales, leurs listes n'ont pu être mises à jour : <br/>";
		s += "<ul>";
		for (String string : pages) {
			s += "<li>"+string+"</li>";
		}
		s += "</ul>";
		super.message = s;
		displayMessage();
	}

	public ArrayList<String> getPages() {
		return pages;
	}

	public void setPages(ArrayList<String> pages) {
		this.pages = pages;
	}
	
	public void add(String page){
		this.pages.add(page);
	}

}
