package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import swingResources.StarRating;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateReviewGUI extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel jLabelReviewMessage;
	private JScrollPane scrollPane;
	private JTextArea messageField;
	private StarRating stars;
	
	public Integer starsGiven;
	public String message;
	public boolean reviewDone = false;

	/**
	 * Create the dialog.
	 */
	public CreateReviewGUI() {
		setBounds(100, 100, 450, 300);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			jLabelReviewMessage = new JLabel("Introduce the message of the review:");
			jLabelReviewMessage.setBounds(10, 88, 414, 14);
			contentPanel.add(jLabelReviewMessage);
		}
		
		messageField = new JTextArea();
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        messageField.setBorder(border);
        messageField.setLineWrap(true);
        messageField.setWrapStyleWord(true);
		
		scrollPane = new JScrollPane(messageField);
		scrollPane.setBounds(5, 113, 424, 104);
		contentPanel.add(scrollPane);

		stars = new StarRating();
		stars.setLocation(10, 30);
		stars.setSize(235, 47);
		contentPanel.add(stars);
		
		JLabel jLabelStars = new JLabel("Rating for the ride:");
		jLabelStars.setBounds(10, 11, 414, 14);
		contentPanel.add(jLabelStars);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Confirm Review");
				okButton.setActionCommand("Confirm Review");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				starsGiven = stars.getStar();
				message = messageField.getText();
				reviewDone = true;
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}
