package exceptions;

import java.util.ArrayList;

public class NoCSSDefine extends MyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> pages;
	
	public NoCSSDefine(){
		super();
		pages = new ArrayList<String>();
	}
	
	public NoCSSDefine(String message) {
		super(message);
	}
	
	public void printMessage(){
		displayMessage();
	}
	
	public void add(String s){
		pages.add(s);
	}

	public ArrayList<String> getPages() {
		return pages;
	}

	public void setPages(ArrayList<String> pages) {
		this.pages = pages;
	}

}
