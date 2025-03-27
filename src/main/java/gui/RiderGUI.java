package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;

import domain.Rider;

public class RiderGUI extends MainGUI {

	private static final long serialVersionUID = 1L;

	private JButton showReservationsButton;
	
	/**
	 * Create the frame.
	 */
	public RiderGUI (Rider d) {
		super(d);

		this.currentSession = d;
		System.out.println("GUI overloading: RiderGUI");
		
		// Current Session label
		if (d != null) // WindowBuilder design view fix
			currentUserLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
		currentUserLabel.setVisible(true);
		
		// Disable login stuff
		signUpJButton.setVisible(false);
		loginJButton.setVisible(false);
		
		// Update Query rides button text
		queryRidesButton.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.QueryRides"));
		
		/*
		 * As of Iteration 1, RiderGUI is almost the same as MainGUI because the functionality of both
		 * unregistered users and registered riders is almost the same. The aim of this class is to provide
		 * solid foundation to expand the uses cases of the Rider role in the future.
		 * */
		
		// Show Reservations Button
		showReservationsButton = new JButton();
		showReservationsButton.setText("Show Rides and Reservation Requests");
		showReservationsButton.setBounds(0, 204, 644, 97);
		jContentPane.add(showReservationsButton);
		
		showReservationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowReservationsHistoryGUI(currentSession);
				a.setVisible(true);
			}
		});
		
	}

	// Updated with all added in this GUI
		public void paintAgain() {
		super.paintAgain();
		
		currentUserLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
	}
}
