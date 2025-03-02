package exceptions;

public class IncorrectCredentialsException extends Exception {
	 private static final long serialVersionUID = 1L;
	 public IncorrectCredentialsException()
	  {
	    super();
	  }
	  /**This exception is triggered if the question already exists 
	  *@param s String of the exception
	  */
	  public IncorrectCredentialsException(String s)
	  {
	    super(s);
	  }
}
