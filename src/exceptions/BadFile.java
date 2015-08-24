package exceptions;

public class BadFile extends MyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BadFile(String message) {
		super(message);
	}
	
	public void printMessage() {
		displayMessage();
	}

}
