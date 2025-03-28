package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import domain.Review;
import swingResources.StarRating;

import javax.swing.JLabel;

public class ShowReviewsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JPanel reviewJPanel;

	/**
	 * Create the dialog.
	 */
	public ShowReviewsDialog(List<Review> reviews) {
		setModal(true);
		setBounds(100, 100, 565, 365);
		getContentPane().setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 293, 539, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		reviewJPanel = new JPanel();
		reviewJPanel.setLayout(new BoxLayout(reviewJPanel, BoxLayout.Y_AXIS));
		
		// Add all the reviews in reviewJPanel, as new JPanel
		for(Review r : reviews) {
			JPanel thisReview = new JPanel();
			thisReview.setLayout(new BoxLayout(thisReview, BoxLayout.Y_AXIS));
			thisReview.setPreferredSize(new Dimension(520, 100));
			{
				JLabel jLabelRiderName = new JLabel(r.getRider().getName());
				jLabelRiderName.setAlignmentX(Component.LEFT_ALIGNMENT);
				thisReview.add(jLabelRiderName);
				thisReview.add(Box.createVerticalStrut(3));
				
				JPanel panel = new JPanel();
				panel.setLayout(new FlowLayout(FlowLayout.LEFT));
				panel.setPreferredSize(new Dimension(450, 20));
				panel.setAlignmentX(Component.LEFT_ALIGNMENT);
				
				StarRating stars = new StarRating();
				stars.setPreferredSize(new Dimension(100, 20));
				stars.setMaximumSize(new Dimension(100, 20));
				stars.setStar(r.getPoints());
				stars.setDisabled();

				JLabel date = new JLabel();
				date.setText(r.getStringDate());
				
				panel.add(stars);
				panel.add(date);
				thisReview.add(panel);
				thisReview.add(Box.createVerticalStrut(3));
				
				JTextArea message = new JTextArea();
				message.setText(r.getMessage());
				message.setEditable(false);
				message.setWrapStyleWord(true);
				message.setLineWrap(true);
				
				JScrollPane sp = new JScrollPane(message);
				sp.setPreferredSize(new Dimension(450, 30));
				sp.setAlignmentX(Component.LEFT_ALIGNMENT);
				thisReview.add(sp);
			}
			thisReview.setAlignmentX(Component.LEFT_ALIGNMENT);
			reviewJPanel.add(thisReview);
			reviewJPanel.add(Box.createVerticalStrut(5));
		}

		scrollPane = new JScrollPane(reviewJPanel);
		scrollPane.setBounds(5, 0, 545, 282);
		scrollPane.setBorder(null);
		getContentPane().add(scrollPane);

	}
}
