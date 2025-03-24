package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import domain.Driver;
import domain.Rider;
import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainGUI extends JFrame {
	
    public Rider currentSession; // Can be Driver, Rider or null (Refers to who the user is logged in as)
    public final String[] avaliableLanguages = new String[] {"en", "es", "eus"};
    
	private static final long serialVersionUID = 1L;

	public JPanel jContentPane = null;
	public JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	protected JLabel jLabelSelectOption;
	public JLabel languageSelectorLabel;
	public DefaultComboBoxModel<String> languageSelectorModel;
	public JComboBox<String> languageSelector;
	public JButton loginJButton;
	public JButton logoutJButton;
	public JButton signUpJButton;
	public JLabel currentUserJLabel;
	private JButton jButtonShowReservations;
	
	/**
	 * This is the default constructor
	 */
	public MainGUI(Rider d) {
		super();

		// Current session is received as parameter, depending on what kind of session is received (rider, driver or 
		// null) the GUI is adapted, showing only the allowed use cases
		currentSession=d;
		
		// Update the text that shows in the window's label
		String windowText = ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle");
		if (currentSession instanceof Driver) windowText += " - Driver : " + currentSession.getName();
		else if (currentSession instanceof Rider) windowText += " - Rider : " + currentSession.getName();
		setTitle(windowText);
		
		// When the window gets closed, terminate the app
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Panel that will contain everything, layout absolute
		jContentPane = new JPanel();
		jContentPane.setLayout(null);
		setContentPane(jContentPane);
		
		// this.setSize(271, 295);
		this.setSize(656, 543);
		
		// HEADER label
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setBounds(0, 11, 644, 97);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		jContentPane.add(jLabelSelectOption);
		
		// Query Rides button
		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setBounds(0, 108, 644, 97);
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new FindRidesGUI(currentSession);
				a.setVisible(true);
			}
		});
		jContentPane.add(jButtonQueryQueries);
		
		// Language selector combo box
		languageSelectorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.languageSelectorLabel.text"));
		languageSelectorLabel.setBounds(4, 15, 89, 14);
		jContentPane.add(languageSelectorLabel);
		languageSelectorModel = new DefaultComboBoxModel<String>(avaliableLanguages);
		languageSelector = new JComboBox<String>(languageSelectorModel);
		// Correct the selected language in the selector combo box
		String localeCode = Locale.getDefault().toString();
		languageSelector.setSelectedItem(localeCode);
		languageSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale((String) languageSelector.getSelectedItem() ));
				System.out.println("Locale changed to: " + Locale.getDefault());
				paintAgain();
			}
		});
		languageSelector.setBounds(85, 11, 60, 22);
		jContentPane.add(languageSelector);
		
		// Log in button
		loginJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogIn"));
		if(currentSession!=null) loginJButton.setVisible(false);
		loginJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new LoginGUI();
				a.setVisible(true);
				dispose();
			}
		});
		loginJButton.setBounds(524, 0, 120, 36);
		jContentPane.add(loginJButton);
		
		// Log out button
		logoutJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogOut"));
		if(currentSession==null) logoutJButton.setVisible(false);
		logoutJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new MainGUI(null);
				a.setVisible(true);
				dispose();
			}
		});
		logoutJButton.setBounds(524, 0, 120, 36);
		jContentPane.add(logoutJButton);
		
		// Sign up button
		signUpJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SignUp"));
		if(currentSession!=null) signUpJButton.setVisible(false);
		signUpJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new RegisterGUI();
				a.setVisible(true);
			}
		});
		signUpJButton.setBounds(524, 35, 120, 36);
		jContentPane.add(signUpJButton);
		
		// Current Session label
		currentUserJLabel = new JLabel("");
		currentUserJLabel.setBounds(165, 4, 319, 19);
		currentUserJLabel.setVisible(false);
		jContentPane.add(currentUserJLabel);
		currentUserJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		// Show Reservations Button
		jButtonShowReservations = new JButton();
		jButtonShowReservations.setText("Show Rides and Reservation Requests");
		jButtonShowReservations.setBounds(0, 204, 644, 97);
		jContentPane.add(jButtonShowReservations);
		
		if(currentSession==null) jButtonShowReservations.setVisible(false);
		jButtonShowReservations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowReservationsGUI(currentSession);
				a.setVisible(true);
			}
		});
		
		
		
		// The label is long, so that any email can fit, but aligns to the right
		
		/*
		 Depending on the type of user: Rider or Driver, some use cases will be available, others won't.
		 	Not logged: query rides
		 	Rider: query rides, request ride
		 	Driver: query rides, request ride, create ride, show requests
		 */
		
	}
	
	
	// Refresh translations text
	public void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		languageSelectorLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.languageSelectorLabel.text"));
		signUpJButton.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SignUp"));
		loginJButton.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogIn"));
		logoutJButton.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.LogOut"));
	}
} // @jve:decl-index=0:visual-constraint="0,0"

