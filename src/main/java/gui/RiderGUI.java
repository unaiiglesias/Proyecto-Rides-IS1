package gui;

import java.util.ResourceBundle;

import domain.Rider;

public class RiderGUI extends MainGUI {

	private static final long serialVersionUID = 1L;


	/**
	 * Create the frame.
	 */
	public RiderGUI (Rider d) {
		super(d);

		System.out.println("GUI overloading: RiderGUI");
		
		// Current Session label
		currentUserJLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
		currentUserJLabel.setVisible(true);
		
		// Disable login stuff
		signUpJButton.setVisible(false);
		loginJButton.setVisible(false);
		
		/*
		 * As of Iteration 1, RiderGUI is almost the same as MainGUI because the functionality of both
		 * unregistered users and registered riders is almost the same. The aim of this class is to provide
		 * solid foundation to expand the uses cases of the Rider role in the future.
		 * */
		
	}

	// Updated with all added in this GUI
		public void paintAgain() {
		super.paintAgain();
		
		currentUserJLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
	}
}
