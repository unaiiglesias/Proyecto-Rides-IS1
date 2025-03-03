package exceptions;

public class IncorrectEmailException extends Exception {
	 private static final long serialVersionUID = 1L;
	 public IncorrectEmailException()
	  {
	    super();
	  }
	 
	  /** This exception is triggered if the given email is incorrect 
	  *@param s String of the exception
	  */
	  public IncorrectEmailException(String s)
	  {
	    super(s);
	  }
}
