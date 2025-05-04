package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import businessLogic.BLFacade;
import domain.Driver;
import domain.Review;
import domain.Rider;
import swingResources.StarRating;

public class ShowReviews extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Driver reviewedUser;
	private BLFacade facade;
	
	private JLabel reviewedUserLabel;
	private JPanel reviewsPanel;
	private JScrollPane scrollPane;
	
	/**
	 * JFrame that shows the reviews made to r
	 */
	public ShowReviews(Driver d) {
		
		// Generic variable
		this.reviewedUser = d;
		this.facade = MainGUI.getBusinessLogic();
		
		// Jframe config
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 565, 365);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		// Reviewed user label
		reviewedUserLabel = new JLabel("Reviews made to: " + d.getName() + " (" + d.getEmail() + ")");
		reviewedUserLabel.setBounds(10, 11, 412, 24);
		reviewedUserLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		reviewedUserLabel.setForeground(Color.BLACK);
		contentPane.add(reviewedUserLabel);
		
		// Panel that will contain all the reviews
		reviewsPanel = new JPanel();
		reviewsPanel.setLayout(new BoxLayout(reviewsPanel, BoxLayout.Y_AXIS));
		
		List<Review> reviewList = facade.getReviewsOfDriver(reviewedUser);
		// We will add each review as an individual panel
		for (Review rev : reviewList)
		{
			reviewsPanel.add(createReviewPanel(rev));
			System.out.println("Review: " + rev);
		}
		
		// Scroll pane that will contain reviews (so that the review panel is scrollable)
		scrollPane = new JScrollPane(reviewsPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 34, 529, 270);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		scrollPane.getVerticalScrollBar().setValue(0); // Always show reviews on top
	}
	
    private JPanel createReviewPanel(Review review) {
    	/**
    	 * Each review panel will be a 500x140 (500 x 240 if the driver answered) panel that must show
    	 * Review author
    	 * Date
    	 * Score
    	 * Message
    	 * Up/Down Vote buttons
    	 * Driver's answer [if exists]
    	 */
    	
    	Rider reviewer = review.getRider();
    	
    	// Panel that contains the review
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Borde visual
        panel.setPreferredSize(new Dimension(500, 140)); // Fixed size for every review
                
        // Author name and email
        JLabel authorLabel = new JLabel("Autor: " + reviewer.getName() + " (" + reviewer.getEmail() + ")");
        authorLabel.setBounds(10, 0, 500, 30);
        panel.add(authorLabel);
        
        // Star Rating 
        StarRating stars = new StarRating();
        stars.setBounds(10, 30, 100, 20);
        stars.setPreferredSize(new Dimension(100, 20));
        stars.setStar(review.getPoints());
        stars.setDisabled();
        panel.add(stars);
        
        // Date label
        JLabel dateLabel = new JLabel();
        dateLabel.setBounds(20 + stars.getWidth(), 30, 200, 20);
        dateLabel.setText(review.getDate().toString());
        panel.add(dateLabel);
        
        // Message text area
        JTextArea messageText = new JTextArea();
        messageText.setText(review.getMessage());
        messageText.setEditable(false);
        messageText.setWrapStyleWord(true);
        messageText.setLineWrap(true);
        messageText.setBounds(10, 60, 450, 70);
        messageText.setBorder(new LineBorder(new Color(0, 0, 0), 1)); // Add a thin black border
        panel.add(messageText);
        
        // Popularity buttons
        JButton upVote = new JButton("👍");
        upVote.setBounds(20 + messageText.getWidth(), 60, 35, 35);
        upVote.setBorder(new LineBorder(new Color(0, 0, 0), 1));
        panel.add(upVote);
        JButton downVote = new JButton("👎");
        downVote.setBounds(20 + messageText.getWidth(), 60 + upVote.getHeight(), upVote.getWidth(), upVote.getHeight());
        downVote.setBorder(new LineBorder(new Color(0, 0, 0), 1));
        panel.add(downVote);
        // We have to assign their action listeners after definition
		upVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facade.addReviewRating(review, reviewer, 1);
				upVote.setBackground(new Color(34, 139, 34));
				upVote.setEnabled(false);
				downVote.setEnabled(false);
			}
		});
		downVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				facade.addReviewRating(review, reviewer, -1);
				upVote.setEnabled(false);
				downVote.setBackground(new Color(255, 69, 58));
				downVote.setEnabled(false);
			}
		});
        
		// Driver's answer text area
		if(review.getDriverAnswer() != null) {
	        panel.setPreferredSize(new Dimension(500, 240));
	        
	        JLabel answerLabel = new JLabel();
	        answerLabel.setBounds(10 + messageText.getX(), 5 + messageText.getY() + messageText.getHeight(), 200, 20);
	        answerLabel.setText("⤷ \t" + review.getStringDriverAnswerDate());
	        panel.add(answerLabel);
	        
	        JTextArea answerMessage = new JTextArea();
	        answerMessage.setText(review.getDriverAnswer());
	        answerMessage.setEditable(false);
	        answerMessage.setWrapStyleWord(true);
	        answerMessage.setLineWrap(true);
	        JScrollPane msgScroll = new JScrollPane(answerMessage);
	        // Won't show the scroll bar
	        msgScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        msgScroll.setBounds(answerLabel.getX(), 3 + answerLabel.getY() + answerLabel.getHeight(), 440, 70);
	        panel.add(msgScroll);
	        
		}
		
        
        return panel;
    }
	
}