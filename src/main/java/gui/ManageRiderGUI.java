package gui;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import businessLogic.BLFacade;
import domain.Rider;
import util.ImageManagerUtil;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Color;
import java.awt.event.ActionEvent;

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

	/**
	 * GUI to view full rider info and modify some of it
	 */
	public ManageRiderGUI(Rider r) {
		bl = MainGUI.getBusinessLogic();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
		
		emailLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.emailLabel"));
		emailLabel.setBounds(10, 86, 80, 20);
		contentPane.add(emailLabel);
		
		emailField = new JTextField();
		emailField.setEditable(false);
		emailField.setText(r.getEmail());
		emailField.setBounds(100, 85, 190, 20);
		contentPane.add(emailField);
		
		nameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ManageRiderGUI.nameLabel"));
		nameLabel.setBounds(10, 119, 80, 20);
		contentPane.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setEditable(false);
		nameField.setText(r.getName() + " " + r.getSurname());
		nameField.setBounds(100, 118, 190, 20);
		contentPane.add(nameField);
		
		errorLabel = new JLabel();
		errorLabel.setBounds(14, 230, 410, 20);
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
