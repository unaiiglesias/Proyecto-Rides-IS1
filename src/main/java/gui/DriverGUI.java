package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import domain.Driver;

public class DriverGUI extends MainGUI {

	private static final long serialVersionUID = 1L;

	public JButton jButtonCreateQuery;
	public JButton jButtonShowRequests;

	/**
	 * Extend MainGUI to include Driver only options
	 */
	public DriverGUI (Driver d) {
		
		super(d);

		System.out.println("GUI loading: DriverGUI");
		
		// The size is defined by MainGUI, only change if more space needed
		//this.setSize(656, 543);
		
		
		// Crete Ride button
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setVisible(true);
		jButtonCreateQuery.setBounds(0, 205, 644, 97);
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CreateRideGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		System.out.println(jContentPane);
		jContentPane.add(jButtonCreateQuery);
		
		// Current Session label
		currentUserJLabel = new JLabel("");
		currentUserJLabel.setBounds(103, 4, 527, 19);
		currentUserJLabel.setText("Account: " + currentSession.getEmail());
		jContentPane.add(currentUserJLabel);
		currentUserJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// The label is long, so that any email can fit, but aligns to the right
		
		// Show requests button
		jButtonShowRequests = new JButton();
		jButtonShowRequests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowRequestsGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		jButtonShowRequests.setVisible(true);
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonShowRequests.setBounds(0, 302, 644, 97);
		jContentPane.add(jButtonShowRequests);
		
		// Disable login stuff
		signUpJButton.setVisible(false);
		loginJButton.setVisible(false);

	}

	// Updated with all added in this GUI
	public void paintAgain() {
		super.paintAgain();
		
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
	}
	
}
