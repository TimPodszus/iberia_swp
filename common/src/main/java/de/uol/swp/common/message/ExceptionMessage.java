package de.uol.swp.common.message;

import java.util.Objects;

/**
 * Encapsulates an Exception in a message object
 * 
 * @author Marco Grawunder
 * @since 2017-03-17
 */
public class ExceptionMessage extends AbstractResponseMessage{

	private static final long serialVersionUID = -7739395567707525535L;
	private final String exception;

	/**
	 * Constructor
	 *
	 * @param message String containing the cause of the exception
	 * @since 2017-03-17
	 */
	public ExceptionMessage(String message){
		this.exception = message;
	}

	/**
	 * Getter for the exception message
	 *
	 * @return String containing the cause of the exception
	 * @since 2017-03-17
	 */
	public String getException() {
		return exception;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		ExceptionMessage that = (ExceptionMessage) o;
		return Objects.equals(exception, that.exception);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), exception);
	}
}
