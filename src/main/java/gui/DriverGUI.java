package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import domain.Driver;

public class DriverGUI extends MainGUI {

	private static final long serialVersionUID = 1L;

	public JButton jButtonCreateQuery;
	public JButton jButtonShowRequests;
	public JButton jButtonRemoveRide;

	/**
	 * Extend MainGUI to include Driver only options
	 */
	public DriverGUI (Driver d) {
		
		super(d);

		System.out.println("GUI overloading: DriverGUI");
		
		// Current Session label
		currentUserJLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
		currentUserJLabel.setVisible(true);
		
		// The size is defined by MainGUI, only change if more space needed
		//this.setSize(656, 543);
		
		
		// Crete Ride button
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setBounds(0, 301, 644, 97);
		getContentPane().add(jButtonCreateQuery);
		jButtonCreateQuery.setVisible(true);
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		
		//Remove query button
		jButtonRemoveRide = new JButton();
		jButtonRemoveRide.setBounds(0, 398, 644, 97);
		getContentPane().add(jButtonRemoveRide);
		jButtonRemoveRide.setVisible(true);
		jButtonRemoveRide.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.RemoveRides"));
		
		// Show requests button
		jButtonShowRequests = new JButton();
		jButtonShowRequests.setBounds(0, 494, 644, 97);
		getContentPane().add(jButtonShowRequests);
		jButtonShowRequests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowRequestsGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		jButtonShowRequests.setVisible(true);
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
		jButtonRemoveRide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new RemoveRidesGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CreateRideGUI((Driver) currentSession);
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
		
		currentUserJLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.account") + ": " + currentSession.getEmail());
		
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
		jButtonRemoveRide.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.RemoveRides"));
	}
	
}
