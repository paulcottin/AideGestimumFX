package exceptions;

public class NoRoboHelpProject extends MyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoRoboHelpProject(String message) {
		super(message);
		displayMessage();
		System.exit(0);
	}
}
