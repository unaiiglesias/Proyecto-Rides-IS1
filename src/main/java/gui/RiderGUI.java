package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;

import domain.Rider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RiderGUI extends MainGUI {

	private static final long serialVersionUID = 1L;

	private JLabel balanceLabel;
	private JLabel balanceAmount;
	
	private JButton depositMoneyButton;
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
			currentUserLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.account") + ": " + currentSession.getEmail());
		currentUserLabel.setVisible(true);
		
		// Disable login stuff
		signUpJButton.setVisible(false);
		loginJButton.setVisible(false);
		
		// Balance label
		balanceLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.balanceLabel"));
		balanceLabel.setBounds(4, 45, 89, 14);
		jContentPane.add(balanceLabel);
		
		// Balance number
		balanceAmount = new JLabel("" + this.currentSession.getBalance());
		balanceAmount.setHorizontalAlignment(SwingConstants.LEFT);
		balanceAmount.setBounds(85, 43, 80, 22);
		jContentPane.add(balanceAmount);
		
		// Deposit money (to balance) button
		depositMoneyButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.depositMoneyButton"));
		depositMoneyButton.setBounds(12, 65, 180, 24);
		jContentPane.add(depositMoneyButton);
		depositMoneyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame a = new DepositMoneyGUI(currentSession);
				// We want to update balance after this window closes, as it might have been modified
				a.addWindowListener(new WindowAdapter() {
				    @Override
				    public void windowClosed(WindowEvent e) {
				    	balanceAmount.setText("" + currentSession.getBalance());
				    }
				});
				a.setVisible(true);
			}
		});
		
		
		// Update Query rides button text
		queryRidesButton.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.QueryRides"));
		
		/*
		 * As of Iteration 1, RiderGUI is almost the same as MainGUI because the functionality of both
		 * unregistered users and registered riders is almost the same. The aim of this class is to provide
		 * solid foundation to expand the uses cases of the Rider role in the future.
		 * */
		
		// Show Reservations Button
		showReservationsButton = new JButton();
		showReservationsButton.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.ShowReservationsButton"));
		showReservationsButton.setBounds(0, 204, 644, 97);
		jContentPane.add(showReservationsButton);
		showReservationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowReservationsHistoryGUI(currentSession);
				// We want to update balance after this window closes, as it might have been modified
				a.addWindowListener(new WindowAdapter() {
				    @Override
				    public void windowClosed(WindowEvent e) {
				    	balanceAmount.setText("" + currentSession.getBalance());
				    }
				});
				a.setVisible(true);
			}
		});
		
	}

	// Updated with all added in this GUI
		public void paintAgain() {
		super.paintAgain();
		
		balanceAmount.setText("" + this.currentSession.getBalance());
		currentUserLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.account") + ": " + currentSession.getEmail());
		depositMoneyButton.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.depositMoneyButton"));
		showReservationsButton.setText(ResourceBundle.getBundle("Etiquetas").getString("RiderGUI.ShowReservationsButton"));
		
	}
}
