package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import domain.Driver;

public class DriverGUI extends RiderGUI {

	private static final long serialVersionUID = 1L;

	public JButton createRideButton;
	public JButton jButtonShowRequests;
	public JButton removeRideButton;
	
	/**
	 * Extend MainGUI to include Driver only options
	 */
	public DriverGUI (Driver d) {
		
		super(d);
		this.currentSession = d;
		
		// DriverGUI has plenty of functionality, so we need to extend the window for all the buttons to fit
		this.setSize(656, 628);

		System.out.println("GUI overloading: DriverGUI");
		
		// Current Session label
		currentUserLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
		currentUserLabel.setVisible(true);
		
		// The size is defined by MainGUI, only change if more space needed
		//this.setSize(656, 543);
		
		
		// Crete Ride button
		createRideButton = new JButton();
		createRideButton.setBounds(0, 298, 644, 97);
		getContentPane().add(createRideButton);
		createRideButton.setVisible(true);
		createRideButton.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.CreateRide"));
		createRideButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CreateRideGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		
		// Remove Ride button
		removeRideButton = new JButton();
		removeRideButton.setBounds(0, 393, 644, 97);
		getContentPane().add(removeRideButton);
		removeRideButton.setVisible(true);
		removeRideButton.setText(ResourceBundle.getBundle("Etiquetas").getString("DriverGUI.RemoveRides"));
		removeRideButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new RemoveRidesGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		
		// Show requests button
		jButtonShowRequests = new JButton();
		jButtonShowRequests.setBounds(0, 487, 644, 97);
		getContentPane().add(jButtonShowRequests);
		jButtonShowRequests.setVisible(true);
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
		jButtonShowRequests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowPendingRequestsGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});

		
		// Disable login stuff
		signUpJButton.setVisible(false);
		loginJButton.setVisible(false);

	}

	// Updated with all added in this GUI
	public void paintAgain() {
		super.paintAgain();
		
		currentUserLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
		
		createRideButton.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
		removeRideButton.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.RemoveRides"));
	}
	
}
