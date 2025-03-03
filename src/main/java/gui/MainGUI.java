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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MainGUI extends JFrame {
	
    private Rider rider;
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
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JPanel panel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton loginJButton;
	private JButton signUpJButton;
	private JLabel currentUserJLabel;
	private JButton jButtonShowRequests;
	
	/**
	 * This is the default constructor
	 */
	public MainGUI(Rider d) {
		super();

		rider=d;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// this.setSize(271, 295);
		this.setSize(656, 543);
		jLabelSelectOption = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jLabelSelectOption.setBounds(0, 0, 644, 97);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		rdbtnNewRadioButton = new JRadioButton("English");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("en"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Euskara");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Locale.setDefault(new Locale("eus"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();				}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("Castellano");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(new Locale("es"));
				System.out.println("Locale: "+Locale.getDefault());
				paintAgain();
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_2);
	
		panel = new JPanel();
		panel.setBounds(0, 410, 644, 97);
		panel.add(rdbtnNewRadioButton_1);
		panel.add(rdbtnNewRadioButton_2);
		panel.add(rdbtnNewRadioButton);
		
		jButtonCreateQuery = new JButton();
		jButtonCreateQuery.setVisible(false);
		jButtonCreateQuery.setBounds(0, 205, 644, 97);
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonCreateQuery.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new CreateRideGUI((Driver) rider);
				a.setVisible(true);
			}
		});
		
		jButtonQueryQueries = new JButton();
		jButtonQueryQueries.setBounds(0, 108, 644, 97);
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonQueryQueries.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame a = new FindRidesGUI(rider);

				a.setVisible(true);
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(null);
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonCreateQuery);
		jContentPane.add(jButtonQueryQueries);
		jContentPane.add(panel);
		
		
		setContentPane(jContentPane);
		
		loginJButton = new JButton("Log in");
		if(rider!=null) loginJButton.setVisible(false);
		loginJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame a = new LoginGUI();
				a.setVisible(true);
				dispose();
			}
		});
		loginJButton.setBounds(555, 0, 89, 23);
		jContentPane.add(loginJButton);
		
		signUpJButton = new JButton("Sign up");
		if(rider!=null) signUpJButton.setVisible(false);
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
		currentUserJLabel.setBounds(430, 4, 214, 19);
		if(rider!=null) currentUserJLabel.setText("Account: "+rider.getEmail());
		else currentUserJLabel.setVisible(false);
		jContentPane.add(currentUserJLabel);
		
		jButtonShowRequests = new JButton();
		jButtonShowRequests.setVisible(false);
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests")); //$NON-NLS-1$ //$NON-NLS-2$
		jButtonShowRequests.setBounds(0, 302, 644, 97);
		jContentPane.add(jButtonShowRequests);
		
		if(rider!=null) setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - driver :"+rider.getName());
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

		if(rider instanceof Driver) {
			jButtonCreateQuery.setVisible(true);
			jButtonShowRequests.setVisible(true);
		}
	}
	
	private void paintAgain() {
		jLabelSelectOption.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SelectOption"));
		jButtonQueryQueries.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.QueryRides"));
		jButtonCreateQuery.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.CreateRide"));
		jButtonShowRequests.setText(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.ShowRequests"));
		if(rider!=null) setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle") + " - driver :"+rider.getName());
		else setTitle(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.MainTitle"));
	}
} // @jve:decl-index=0:visual-constraint="0,0"

