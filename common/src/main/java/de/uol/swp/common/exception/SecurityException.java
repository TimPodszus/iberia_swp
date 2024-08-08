package de.uol.swp.common.exception;

/**
 * Exception to state e.g. that a authorization is required
 *
 * @author Marco Grawunder
 * @since 2017-03-17
 */
public class SecurityException extends RuntimeException {

	private static final long serialVersionUID = -6908340347082873591L;

	/**
	 * Constructor
	 *
	 * @param message Text the Exception should contain
	 * @since 2017-03-17
	 */
	public SecurityException(String message){
		super(message);
	}

}
