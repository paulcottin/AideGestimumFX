package exceptions;

public class AideException extends MyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AideException(String message) {
		super(message);
	}
	
	public void printMessage(){
		displayMessage();
	}

}
