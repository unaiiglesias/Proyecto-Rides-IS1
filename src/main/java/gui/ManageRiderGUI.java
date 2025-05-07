package gui;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;

import businessLogic.BLFacade;
import domain.Rider;
import util.ImageManagerUtil;
import util.UserDataValidator;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class ManageRiderGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private BLFacade bl;
	private JLabel profileImage;
	private JButton changeProfilePic;
	private JButton resetIcon;
	private JLabel emailLabel;
	private JTextField emailField;
	private JLabel nameLabel;
	private JTextField nameField;
	private JFileChooser selector;
	private JLabel errorLabel;
	private JLabel surnameLabel;
	private JTextField surnameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JCheckBox showPassword;
	private JButton applyButton;
	private JButton cancelButton;
	
	/**
	 * GUI to view full rider info and modify some of it
	 */
	public ManageRiderGUI(Rider r) {
		bl = MainGUI.getBusinessLogic();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 470, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		profileImage = new JLabel(r.getProfilePicIcon());
		profileImage.setBounds(14, 11, 64, 64);
		contentPane.add(profileImage);
		
		changeProfilePic = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.changeProfilePic"));
		changeProfilePic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				selector = new JFileChooser();
				selector.setFileSelectionMode(JFileChooser.FILES_ONLY);
				selector.setFileFilter(new FileNameExtensionFilter("PNG 64x64 image", "png"));
				
				
				int res = selector.showOpenDialog(null);
				
				if (res == JFileChooser.APPROVE_OPTION)
				{
					File selectedImage = selector.getSelectedFile();
					System.out.println("Directorio seleccionado: " + selectedImage.getAbsolutePath());
					// File chosen, check that it's correct and apply it
					ImageIcon icon = ImageManagerUtil.readImageFromFile(selectedImage.getAbsolutePath());
					
					String verification = verifyIcon(icon);
					errorLabel.setText("");
					
					if (verification == null)
					{
						bl.setRiderProfilePic(r, icon);
						updateProfilePic(r);
					}
					else
						errorLabel.setText(verification);
				}
				else
				{
					System.out.println("Icon selection cancelled"); // debug
				}
				
			}
		});
		changeProfilePic.setBounds(100, 30, 150, 24);
		contentPane.add(changeProfilePic);
		
		resetIcon = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.resetIcon"));
		resetIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				errorLabel.setText("");
				bl.setRiderProfilePic(r, ImageManagerUtil.getDefaultIcon());
				updateProfilePic(r);
			}
		});
		resetIcon.setBounds(260, 30, 150, 24);
		contentPane.add(resetIcon);
		
		emailLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Email"));
		emailLabel.setBounds(10, 90, 80, 20);
		contentPane.add(emailLabel);
		
		emailField = new JTextField();
		emailField.setEditable(false);
		emailField.setText(r.getEmail());
		emailField.setBounds(100, 90, 190, 20);
		contentPane.add(emailField);
		
		nameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Name"));
		nameLabel.setBounds(10, 120, 80, 20);
		contentPane.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setText(r.getName());
		nameField.setBounds(100, 120, 190, 20);
		contentPane.add(nameField);
		
		surnameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Surname"));
		surnameLabel.setBounds(10, 150, 80, 20);
		contentPane.add(surnameLabel);
		
		surnameField = new JTextField();
		surnameField.setText(r.getSurname());
		surnameField.setBounds(100, 150, 190, 20);
		contentPane.add(surnameField);
		surnameField.setColumns(10);
		
		passwordLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RegisterGUI.Password"));
		passwordLabel.setBounds(10, 180, 80, 20);
		contentPane.add(passwordLabel);
		passwordField = new JPasswordField();
		passwordField.setText(r.getPassword());
		
		passwordField.setBounds(100, 180, 190, 20);
		contentPane.add(passwordField);
		
		showPassword = new JCheckBox(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.showPassword"));
		showPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				char current = passwordField.getEchoChar();
				if (current == (char) 0)
					passwordField.setEchoChar('*');
				else
					passwordField.setEchoChar((char) 0);
			}
		});
		showPassword.setBounds(296, 179, 152, 23);
		contentPane.add(showPassword);
		
		applyButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.apply"));
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Update to new info and close
				String name = nameField.getText(), surname = surnameField.getText(), password = new String(passwordField.getPassword()), validation;
				
				if (!name.equals(r.getName()))
				{
					validation = UserDataValidator.validateName(name);
					if (validation != null)
					{
						errorLabel.setText("ERROR: " + validation);
						return;
					}
					bl.updateName(r, name);
					System.out.println("Updating name to: " + name);
				}
				if (!surname.equals(r.getSurname()))
				{
					validation = UserDataValidator.validateSurname(surname);
					if (validation != null)
					{
						errorLabel.setText("ERROR: " + validation);
						return;
					}
					bl.updateSurname(r, surname);
					System.out.println("Updating surname to: " + surname);
				}
				if (!password.equals(r.getPassword()))
				{
					validation = UserDataValidator.validatePassword(password);
					if (validation != null)
					{
						errorLabel.setText("ERROR: " + validation);
						return;
					}
					bl.updatePassword(r, password);
					System.out.println("Updating password to: " + password.replaceAll(".","*")); // Censore password to console log
				}
				dispose();
			}
		});
		applyButton.setBounds(100, 209, 100, 25);
		contentPane.add(applyButton);
		
		cancelButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.cancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		cancelButton.setBounds(260, 209, 100, 25);
		contentPane.add(cancelButton);
		
		errorLabel = new JLabel();
		errorLabel.setBounds(10, 230, 410, 20);
		errorLabel.setForeground(new Color(255,0,0));
		contentPane.add(errorLabel);
		
	}
	
	private void updateProfilePic (Rider r) {
		this.profileImage.setIcon(r.getProfilePicIcon());
	}
	
	private String verifyIcon (ImageIcon icon) {
		/**
		 * Verifies that icon is correct
		 * 
		 * Returns null if everything OK, string detailing error otherwise
		 */
		
		if (icon.getIconWidth() != 64 || icon.getIconHeight() != 64)
			return ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.iconSizeError");
		
		return null;
	}
}
