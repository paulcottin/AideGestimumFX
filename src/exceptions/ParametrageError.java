package exceptions;

public class ParametrageError extends MyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ParametrageError(String message) {
		super(message);
	}
	
	public void printMessage(){
		displayMessage();
	}

}
