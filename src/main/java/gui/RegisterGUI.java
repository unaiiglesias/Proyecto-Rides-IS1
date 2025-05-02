package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Rider;
import exceptions.IncorrectEmailException;
import exceptions.UserAlreadyExistException;
import javax.swing.JSpinner;
import java.awt.Font;
import javax.swing.JCheckBox;
import util.UserDataValidator;

public class RegisterGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JLabel emailTextArea;
	private JLabel jLabelPassword;
	private JLabel jLabelEmail;
	private JLabel jLabelName;
	private JTextField nameTextField;
	private JLabel jLabelSurname;
	private JTextField surnameTextField;
	private JLabel jLabelAge;
	private JLabel lblSignUp;
	private JSpinner spinner;
	private JLabel jLabelLicensePlate;
	private JTextField vehicleModelField;
	private JTextField licensePlateField;
	private JLabel jLabelVehicleModel;
	private JLabel resultTextArea;
	private JCheckBox driverCheckBox;


	/**
	 * Create the frame.
	 */
	public RegisterGUI() {
		BLFacade facade = MainGUI.getBusinessLogic();
		
		setTitle("Login");
		// If the window gets closed, don't just close the application. Go back to the MainGUI instead
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				JFrame a = new MainGUI(null);
				a.setVisible(true);
				dispose();
			}
		});
		setBounds(100, 100, 490, 437);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jLabelEmail = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Email"));
		jLabelEmail.setBounds(26, 78, 84, 14);
		contentPane.add(jLabelEmail);
		
		jLabelPassword = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Password"));
		jLabelPassword.setBounds(26, 108, 84, 14);
		contentPane.add(jLabelPassword);
		
		emailField = new JTextField();
		emailField.setBounds(120, 74, 278, 22);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(120, 107, 278, 22);
		contentPane.add(passwordField);
		
		JButton loginJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SignUp"));
		loginJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 If the Sign up is successful, MainGUI will be launched with the new user
				 */
				
				// Clear error messages
				emailTextArea.setText("");
				resultTextArea.setText("");
				
				Rider user = null; 
				String email = null;
				try {
					String validation;
					
					email = emailField.getText();
					validation = UserDataValidator.validateEmail(email);
					if (validation != null)
						throw new Exception(validation);
					
					String password = new String(passwordField.getPassword());
					validation = UserDataValidator.validatePassword(password);
					if (validation != null) 
						throw new Exception(validation);
					
					String name = nameTextField.getText();
					validation = UserDataValidator.validateName(name);
					if (validation != null)
						throw new Exception(validation);
					
					String surname = surnameTextField.getText();
					validation = UserDataValidator.validateSurname(surname);
					if (validation != null)
						throw new Exception(validation);
					
					Integer age = (Integer) spinner.getValue();
					validation = UserDataValidator.validateAge(age);
					if (validation != null)
						throw new Exception(validation);
					if (driverCheckBox.isSelected() && age < 18) throw new Exception("Minors can't be drivers");
					
					
					if(driverCheckBox.isSelected()) {
						String licensePlate = licensePlateField.getText();
						validation = UserDataValidator.validateLicensePlate(licensePlate);
						if (validation != null)
							throw new Exception(validation);
						
						String vehicleModel = vehicleModelField.getText();
						validation = UserDataValidator.validateVehicleModel(vehicleModel);
						if (validation != null)
							throw new Exception(validation);
						
						facade.addDriver(email, password, name, surname, age, licensePlate, vehicleModel);
					} else {
						facade.addRider(email, password, name, surname, age);
					}
					
					user = facade.getRider(email);
					dispose();
					JFrame a;
					if (user instanceof Driver)
						a = new DriverGUI((Driver) user);
					else if (user instanceof Rider)
						a = new RiderGUI(user);
					else
						a = new MainGUI(user);
					a.setVisible(true);
					
				// Specific exceptions
				} catch(UserAlreadyExistException exception1) {
					emailTextArea.setText("Email already in use. Please select another one");
					emailTextArea.setForeground(Color.RED);
				} catch(IncorrectEmailException exception2) {
					emailTextArea.setText("Email format is not correct. " + exception2.getMessage());
					emailTextArea.setForeground(Color.RED);
					
				// Generic exception, error message will be shown next to result
				} catch(Exception exception3) {
					String m = exception3.getMessage();
					if (m.equals("")) resultTextArea.setText("Error: Please fill all the gaps correctly");
					else resultTextArea.setText(m);
					resultTextArea.setForeground(Color.RED);
				}

			}
		});
		loginJButton.setBounds(176, 318, 130, 23);
		contentPane.add(loginJButton);
		
		emailTextArea = new JLabel();
		emailTextArea.setBackground(new Color(240, 240, 240));
		emailTextArea.setBounds(120, 55, 278, 14);
		contentPane.add(emailTextArea);
		
		jLabelName = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Name"));
		jLabelName.setBounds(26, 136, 84, 14);
		contentPane.add(jLabelName);
		
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		nameTextField.setBounds(120, 136, 172, 22);
		contentPane.add(nameTextField);
		
		jLabelSurname = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Surname"));
		jLabelSurname.setBounds(26, 164, 84, 14);
		contentPane.add(jLabelSurname);
		
		surnameTextField = new JTextField();
		surnameTextField.setColumns(10);
		surnameTextField.setBounds(120, 164, 278, 22);
		contentPane.add(surnameTextField);
		
		jLabelAge = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Age"));
		jLabelAge.setBounds(302, 140, 56, 14);
		contentPane.add(jLabelAge);
		
		lblSignUp = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.SignUp"));
		lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblSignUp.setFont(new Font("Consolas", Font.BOLD, 25));
		lblSignUp.setBounds(99, 21, 278, 33);
		contentPane.add(lblSignUp);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		spinner.setBounds(356, 136, 42, 20);
		contentPane.add(spinner);
		
		jLabelLicensePlate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.LP"));
		jLabelLicensePlate.setBounds(26, 240, 84, 14);
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
		
		jLabelVehicleModel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.VM"));
		jLabelVehicleModel.setBounds(26, 268, 84, 14);
		contentPane.add(jLabelVehicleModel);
		jLabelVehicleModel.setVisible(false);
		 
		resultTextArea = new JLabel();
		resultTextArea.setBackground(new Color(240, 240, 240));
		resultTextArea.setBounds(123, 353, 254, 22);
		contentPane.add(resultTextArea);
		
		driverCheckBox = new JCheckBox(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Driver"));
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
