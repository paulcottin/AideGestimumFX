package exceptions;

public class FichierUtilise extends MyException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FichierUtilise(String fileName) {
		super("Fichier \""+fileName+"\" utilis� par un autre processus, veuillez fermer ce programme");
		displayMessage();
	}

}
