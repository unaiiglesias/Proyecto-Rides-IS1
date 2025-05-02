package util;

public class UserDataValidator {
	/**
	 * User data can be inserted while registering (RegisterGUI) or when updating it (ManageRiderGUI).
	 * So, it is a good idea to export data validation to a helper class in order to avoid redundancy.
	 * 
	 * All methods in this class work the same. If everything wen't ok, they return null.
	 * They return a string detailing the error otherwise.
	 */

	public static String validateEmail (String email)
	{
		// Chech for @
		if (email.split("@").length != 2) return"Missing @";
		// Check for domain (.com, .net, .org, .eus...)
		// The dot (".") needs to be escaped because split gets a regex
		else if (email.split("@")[1].split("\\.").length != 2 ) return "Missing domain";
		
		return null;
	}

	public static String validatePassword (String password) {
		if (password.equals("")) return "Password can't be empty";
		return null;
	}

	public static String validateName (String name) {
		if (name.equals("")) return "Name can't be empty";
		return null;
	}
	
	public static String validateSurname (String surname) {
		if (surname.equals("")) return "Surname can't be empty";
		return null;
	}
	
	public static String validateAge (Integer age) {
		if (age <= 0) return "Incorrect age";
		return null;
	}
	
	public static String validateLicensePlate (String licensePlate) {
		if (licensePlate.equals("")) return "Drivers need to register their license plate";
		return null;
	}
	
	public static String validateVehicleModel (String vehicleModel) {
		if (vehicleModel.equals("")) return "Drivers need to register their vehicle model";
		return null;
	}
}
