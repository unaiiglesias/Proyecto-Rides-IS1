package util;

import java.util.ResourceBundle;

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
		if (email.split("@").length < 2) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.missing@");
		// Check for domain (.com, .net, .org, .eus...)
		// The dot (".") needs to be escaped because split gets a regex
		else if (email.split("@")[1].split("\\.").length != 2 ) 
			return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.missingDomain");
		
		return null;
	}

	public static String validatePassword (String password) {
		if (password.equals("")) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.emptyPassword");
		return null;
	}

	public static String validateName (String name) {
		if (name.equals("")) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.emptyName");
		return null;
	}
	
	public static String validateSurname (String surname) {
		if (surname.equals("")) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.emptySurname");
		return null;
	}
	
	public static String validateAge (Integer age) {
		if (age <= 0) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.incorrectAge");
		return null;
	}
	
	public static String validateLicensePlate (String licensePlate) {
		if (licensePlate.equals("")) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.emptyLicensePlate");
		return null;
	}
	
	public static String validateVehicleModel (String vehicleModel) {
		if (vehicleModel.equals("")) return ResourceBundle.getBundle("Etiquetas").getString("UserDataValidator.emptyVehicleModel");
		return null;
	}
}
