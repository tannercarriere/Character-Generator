package exceptions;

public class ArraySizeMismatchException extends Exception {
	/**
	 * If array is out of expected parameters use this
	 */
	private static final long serialVersionUID = 1L;

	public ArraySizeMismatchException(String message) {
		super(message);
	}
}
