package modelo;

public class PrimaryKeyViolation extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -150516915731297907L;
	
	public PrimaryKeyViolation(String message) {
		super(message);
	}

}
