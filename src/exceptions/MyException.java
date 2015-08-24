package exceptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

public abstract class MyException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message;
	protected File log;
	
	public MyException() {
		super();
		this.message = "";
		this.log = new File("log.html");
	}
	
	public MyException(String message){
		super(message);
		this.message = message;
		this.log = new File("log.html");
	}
	
	protected void displayMessage(){
		writeLog();
		JOptionPane.showMessageDialog(null, "<html>"+message+"<br/> "
				+ "(Fichier \"log.html\" créé dans le répertoire du jar pour l'historique des erreurs)</html>", "Erreur", JOptionPane.ERROR_MESSAGE);
//		super.printStackTrace();
	}
	
	private void writeLog(){
		BufferedWriter bw;
		BufferedReader br;
		String fichier = "";
		try {
			try {
				br = new BufferedReader(new FileReader(log));
				String ligne = "";
				
				while ((ligne = br.readLine()) != null)
					fichier += ligne;
				
				br.close();
			} catch (IOException e) {}
			
			bw = new BufferedWriter(new FileWriter(log));
			bw.write(fichier);
			
			bw.write(GregorianCalendar.getInstance().getTime().toString()+" : "+message+"<br/>");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
