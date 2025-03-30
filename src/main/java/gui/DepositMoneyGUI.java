package gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.Rider;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DepositMoneyGUI extends JFrame {

	private BLFacade facade;
	private Rider user;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JLabel headerLabel;
	private JTextField amountField;
	private JButton depositButton;
	private JLabel errorLabel;
	private JLabel euroLabel;
	
	/**
	 * Create the frame.
	 */
	public DepositMoneyGUI(Rider d) {
		
		this.facade = MainGUI.getBusinessLogic();
		this.user = d;
		
		// JFrame config
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Header label
		headerLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("DepositMoneyGUI.headerLabel"));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		headerLabel.setForeground(Color.BLACK);
		headerLabel.setBounds(10, 11, 414, 25);
		contentPane.add(headerLabel);
		
		// Field to introduce the amount to add to balance
		amountField = new JTextField();
		amountField.setBounds(96, 50, 241, 35);
		contentPane.add(amountField);
		
		// Euro label €
		euroLabel = new JLabel("€");
		euroLabel.setBounds(338, 54, 55, 27);
		contentPane.add(euroLabel);
		
		// Button
		depositButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("DepositMoneyGUI.depositButton"));
		depositButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = amountField.getText();
				double amount;
				try 
				{
					amount = Double.parseDouble(input);
					if (amount < 0)
						throw new NumberFormatException();
				}
				catch (NumberFormatException err) 
				{
					errorLabel.setVisible(true);
					return;
				}
				
				// We need to update the balance of both the current user and it's DB instance
				facade.depositMoney(amount, user);
				d.getPaid(amount);
				
				// If everything went well, just close the window
				dispose();
			}
		});
		depositButton.setBounds(157, 100, 119, 35);
		contentPane.add(depositButton);
		
		// Wrong input label
		errorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("DepositMoneyGUI.errorLabel"));
		errorLabel.setBounds(10, 140, 414, 14);
		errorLabel.setForeground(new Color(255,0,0));
		errorLabel.setVisible(false);
		contentPane.add(errorLabel);
		
		
	}
}
