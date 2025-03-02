package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Rider;
import exceptions.UserAlreadyExistException;
import javax.swing.JSpinner;
import java.awt.Font;
import javax.swing.JCheckBox;

public class RegisterGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JTextArea emailTextArea;
	private JLabel jLabelPassword;
	private JLabel jLabelEmail;
	private JLabel jLabelName;
	private JTextField nameTextField;
	private JLabel jLabelSurname;
	private JTextField surnameTextField;
	private JLabel jLabelAge;
	private JLabel lblSingUp;
	private JSpinner spinner;
	private JLabel jLabelLicensePlate;
	private JTextField vehicleModelField;
	private JTextField licensePlateField;
	private JLabel jLabelVehicleModel;
	private JTextArea resultTextArea;
	private JCheckBox driverCheckBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterGUI frame = new RegisterGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegisterGUI() {
		BLFacade facade = MainGUI.getBusinessLogic();
		
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 437);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jLabelEmail = new JLabel("Email:");
		jLabelEmail.setBounds(45, 77, 65, 14);
		contentPane.add(jLabelEmail);
		
		jLabelPassword = new JLabel("Password:");
		jLabelPassword.setBounds(45, 111, 65, 14);
		contentPane.add(jLabelPassword);
		
		emailField = new JTextField();
		emailField.setBounds(120, 74, 278, 22);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(120, 107, 278, 22);
		contentPane.add(passwordField);
		
		JButton loginJButton = new JButton("Sign Up");
		loginJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 If the Sign up is successful, MainGUI will be launched with the new user
				 */
				Rider user = null; 
				String email = null;
				try {
					email = emailField.getText();
					String password = new String(passwordField.getPassword());	
					String surname = surnameTextField.getText();
					Integer age = (Integer) spinner.getValue();
					if(driverCheckBox.isSelected()) {
						String licensePlate = licensePlateField.getText();
						String vehicleModel = vehicleModelField.getText();
						facade.addDriver(email, password, surname, surname, age, licensePlate, vehicleModel);
					} else {
						facade.addRider(email, password, surname, surname, age);
					}
					user = facade.getRider(email);
					dispose();
					JFrame a = new MainGUI(user); 
					a.setVisible(true);
				} catch(UserAlreadyExistException exception1) {
					emailTextArea.setText("Email already in use. Please select another one");
				} catch(Exception exception2) {
					resultTextArea.setText("Error: Please fill all the gaps correctly");
					resultTextArea.setForeground(new Color(255,0,0));
				}

			}
		});
		loginJButton.setBounds(176, 318, 130, 23);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane.add(loginJButton);
		
		emailTextArea = new JTextArea();
		emailTextArea.setEditable(false);
		emailTextArea.setBounds(120, 55, 278, 14);
		contentPane.add(emailTextArea);
		
		jLabelName = new JLabel("Name:");
		jLabelName.setBounds(45, 139, 65, 14);
		contentPane.add(jLabelName);
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		nameTextField.setBounds(120, 136, 172, 22);
		contentPane.add(nameTextField);
		
		jLabelSurname = new JLabel("Surname:");
		jLabelSurname.setBounds(45, 167, 65, 14);
		contentPane.add(jLabelSurname);
		
		surnameTextField = new JTextField();
		surnameTextField.setColumns(10);
		surnameTextField.setBounds(120, 164, 278, 22);
		contentPane.add(surnameTextField);
		
		jLabelAge = new JLabel("Age:");
		jLabelAge.setBounds(302, 140, 35, 14);
		contentPane.add(jLabelAge);
		
		lblSingUp = new JLabel("Sing Up");
		lblSingUp.setFont(new Font("Consolas", Font.BOLD, 25));
		lblSingUp.setBounds(182, 11, 124, 33);
		contentPane.add(lblSingUp);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		spinner.setBounds(347, 140, 30, 20);
		contentPane.add(spinner);
		
		jLabelLicensePlate = new JLabel("License plate");
		jLabelLicensePlate.setBounds(45, 240, 65, 14);
		contentPane.add(jLabelLicensePlate);
		jLabelLicensePlate.setVisible(false);
		
		vehicleModelField = new JTextField();
		vehicleModelField.setColumns(10);
		vehicleModelField.setBounds(120, 265, 278, 22);
		contentPane.add(vehicleModelField);
		vehicleModelField.setVisible(false);
		
		licensePlateField = new JTextField();
		licensePlateField.setColumns(10);
		licensePlateField.setBounds(120, 237, 278, 22);
		contentPane.add(licensePlateField);
		licensePlateField.setVisible(false);
		
		jLabelVehicleModel = new JLabel("Vehicle Model");
		jLabelVehicleModel.setBounds(45, 268, 65, 14);
		contentPane.add(jLabelVehicleModel);
		jLabelVehicleModel.setVisible(false);
		 
		resultTextArea = new JTextArea();
		resultTextArea.setEditable(false);
		resultTextArea.setBounds(176, 352, 130, 22);
		contentPane.add(resultTextArea);
		
		driverCheckBox = new JCheckBox("Select if wanted to be also a driver");
		driverCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = false;
				if(driverCheckBox.isSelected()) {
					selected = true;
				}
				jLabelLicensePlate.setVisible(selected);
				vehicleModelField.setVisible(selected);
				licensePlateField.setVisible(selected);
				jLabelVehicleModel.setVisible(selected);
			}
		});
		driverCheckBox.setBounds(44, 203, 209, 23);
		contentPane.add(driverCheckBox);
	}
}
