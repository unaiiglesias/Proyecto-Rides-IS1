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
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainGUI extends JFrame {
	
    private Rider currentSession; // Can be Driver, Rider or null (Refers to who the user is logged in as)
	private final String[] avaliableLanguages = new String[] {"en", "es", "eus"};
    
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonCreateQuery = null;
	private JButton jButtonQueryQueries = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	protected JLabel jLabelSelectOption;
	private JLabel languageSelectorLabel;
	private DefaultComboBoxModel<String> languageSelectorModel;
	private JComboBox<String> languageSelector;
	private JButton loginJButton;
	private JButton signUpJButton;
	private JLabel currentUserJLabel;
	private JButton jButtonShowRequests;
	
	/**
	 * This is the default constructor
	 */
	public MainGUI(Rider d) {
		super();

		// Current session is received as parameter, depending on what kind of session is received (rider, driver or 
		// null) the GUI is adapted, showing only the allowed use cases
		currentSession=d;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// HEADER label
		// this.setSize(271, 295);
		this.setSize(656, 543);
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setBounds(0, 11, 644, 97);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Crete Ride button
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setVisible(false);
		jButtonCreateQuery.setBounds(0, 205, 644, 97);
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CreateRideGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		
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
		
		jContentPane = new JPanel();
		jContentPane.setLayout(null);
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonCreateQuery);
		jContentPane.add(jButtonQueryQueries);		
		
		setContentPane(jContentPane);
		
		// Language selector combo box
		languageSelectorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.languageSelectorLabel.text"));
		languageSelectorLabel.setBounds(4, 15, 89, 14);
		jContentPane.add(languageSelectorLabel);
		languageSelectorModel = new DefaultComboBoxModel<String>(avaliableLanguages);
		languageSelector = new JComboBox<String>(languageSelectorModel);
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
		loginJButton = new JButton("Log in");
		if(currentSession!=null) loginJButton.setVisible(false);
		loginJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new LoginGUI();
				a.setVisible(true);
				dispose();
			}
		});
		loginJButton.setBounds(555, 0, 89, 23);
		jContentPane.add(loginJButton);
		
		// Sign up button
		signUpJButton = new JButton("Sign up");
		if(currentSession!=null) signUpJButton.setVisible(false);
		signUpJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new RegisterGUI();
				a.setVisible(true);
				dispose();
			}
		});
		signUpJButton.setBounds(555, 24, 89, 23);
		jContentPane.add(signUpJButton);
		
		currentUserJLabel = new JLabel("");
		currentUserJLabel.setBounds(103, 4, 527, 19);
		if(currentSession!=null) currentUserJLabel.setText("Account: " + currentSession.getEmail());
		else currentUserJLabel.setVisible(false);
		jContentPane.add(currentUserJLabel);
		currentUserJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// The label is long, so that any email can fit, but aligns to the right
		
		jButtonShowRequests = new JButton();
		jButtonShowRequests.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new ShowRequestsGUI((Driver) currentSession);
				a.setVisible(true);
			}
		});
		jButtonShowRequests.setVisible(false);
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonShowRequests.setBounds(0, 302, 644, 97);
		jContentPane.add(jButtonShowRequests);
		
		
		if(currentSession != null) setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - driver :" + currentSession.getName());
		else setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle"));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
		
		/*
		 Depending on the type of user: Rider or Driver, some use cases will be available, others won't.
		 	Not logged: query rides
		 	Rider: query rides, request ride
		 	Driver: query rides, request ride, create ride, show requests
		 */

		if (currentSession instanceof Driver) {
			jButtonCreateQuery.setVisible(true);
			jButtonShowRequests.setVisible(true);
		}
		
		// Refresh some text, just in case
		paintAgain();
	}
	
	
	// Refresh translations text
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
		languageSelectorLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.languageSelectorLabel.text"));

		
		// Update the selected language in the selector combo box
		String localeCode = Locale.getDefault().toString();
		languageSelector.setSelectedItem(localeCode);
		
		
		// Update the text that shows in the window's label
		String windowText = ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle");
		if (currentSession instanceof Driver) windowText += " - Driver : " + currentSession.getName();
		else if (currentSession instanceof Rider) windowText += " - Rider : " + currentSession.getName();
		setTitle(windowText);
	}
} // @jve:decl-index=0:visual-constraint="0,0"

