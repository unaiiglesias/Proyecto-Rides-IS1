package exceptions;

public class UserDoesNotExistException extends Exception {
	 private static final long serialVersionUID = 1L;
	 public UserDoesNotExistException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question already exists 
	  *@param s String of the exception
	  */
	  public UserDoesNotExistException(String s)
	  {
	    super(s);
	  }
}
