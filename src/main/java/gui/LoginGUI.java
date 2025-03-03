package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.*;
import domain.*;
import exceptions.*;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JTextArea passwordTextArea;
	private JTextArea emailTextArea;
	private JLabel jLabelPassword;
	private JLabel jLabelEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI frame = new LoginGUI();
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
	public LoginGUI() {
		
		BLFacade facade = MainGUI.getBusinessLogic();
		
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 276);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jLabelEmail = new JLabel("Email:");
		jLabelEmail.setBounds(45, 59, 65, 14);
		contentPane.add(jLabelEmail);
		
		jLabelPassword = new JLabel("Password:");
		jLabelPassword.setBounds(45, 111, 65, 14);
		contentPane.add(jLabelPassword);
		
		emailField = new JTextField();
		emailField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				emailTextArea.setText("");
			}
		});
		emailField.setBounds(120, 56, 278, 22);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(120, 107, 278, 22);
		contentPane.add(passwordField);
		
		JButton loginJButton = new JButton("Login");
		loginJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 If the login is successful, MainGUI will be launched with the new user
				 */
				Rider user = null;
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());
				try {
					user = facade.login(email, password);
					dispose();
					System.out.println("Login hehco");
					JFrame a = new MainGUI(user);
					a.setVisible(true);
					// if login successful, we'll close this window
					
				} catch(IncorrectCredentialsException exception1) {
					passwordTextArea.setText("Incorrect password");
					passwordTextArea.setForeground(Color.red);
				} catch(UserDoesNotExistException exception2) {
					emailTextArea.setText("Incorrect email");
					emailTextArea.setForeground(Color.RED);
				}
			}
		});
		loginJButton.setBounds(189, 140, 130, 23);
		contentPane.add(loginJButton);
		
		emailTextArea = new JTextArea();
		emailTextArea.setBackground(new Color(240, 240, 240));
		emailTextArea.setEditable(false);
		emailTextArea.setBounds(120, 37, 278, 14);
		contentPane.add(emailTextArea);
		
		passwordTextArea = new JTextArea();
		passwordTextArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				passwordTextArea.setText("");
			}
		});
		passwordTextArea.setBackground(new Color(240, 240, 240));
		passwordTextArea.setEditable(false);
		passwordTextArea.setBounds(120, 89, 278, 14);
		contentPane.add(passwordTextArea);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Consolas", Font.BOLD, 25));
		lblLogin.setBounds(195, 11, 124, 33);
		contentPane.add(lblLogin);
	}
}
