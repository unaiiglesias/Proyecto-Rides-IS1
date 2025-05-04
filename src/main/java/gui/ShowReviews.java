package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import domain.Ride;
import domain.Rider;
import swingResources.StarRating;
import util.ImageManagerUtil;

public class ShowReviews extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Driver reviewedUser;
	private BLFacade facade;
	private List<Review> reviewList;
	private boolean answerAvailable;
	private Supplier<List<Review>> getReviewsMethod;
	
	private JLabel reviewedUserLabel;
	private JPanel reviewsPanel;
	private JScrollPane scrollPane;
	
	/**
	 * JFrame that shows the reviews made to r
	 */
	
	public ShowReviews(Driver d) {
		this.facade = MainGUI.getBusinessLogic();
		this.reviewedUser = d;
		this.getReviewsMethod = () -> facade.getReviewsOfDriver(reviewedUser);
		this.answerAvailable = false;
		init();
	}
	
	public ShowReviews(Ride r) {
		this.facade = MainGUI.getBusinessLogic();
		this.reviewedUser = r.getDriver();
		this.getReviewsMethod = () -> facade.getReviewsOfRide(r);
		this.answerAvailable = true;
		init();
	}
	
	private void init() {
		
		// Generic variable
		Driver d = this.reviewedUser;
		reviewList = getReviewsMethod.get();
		
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
		
		// We will add each review as an individual panel
		for (Review rev : this.reviewList)
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
	        answerLabel.setBounds(10 + messageText.getX(), 5 + messageText.getY() + messageText.getHeight(), messageText.getWidth() - 10, 20);
	        answerLabel.setText("⤷   " + reviewedUser.getName() + "  " + ResourceBundle.getBundle("Etiquetas").getString("ShowReviews.answerLabel") + "     " + review.getStringDriverAnswerDate());
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
	        
		} else if(this.answerAvailable) {
	        panel.setPreferredSize(new Dimension(500, 170)); // Fixed size for every review
			JButton answerButton = new JButton();
			answerButton.setText(ResourceBundle.getBundle("Etiquetas").getString("ShowReviews.reply"));
			answerButton.setFont(new Font("Arial", Font.BOLD, 11));
			answerButton.setBounds(215, 132, 100, 30);
			panel.add(answerButton);
			answerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel.remove(answerButton);
			        panel.setPreferredSize(new Dimension(500, 240));
			        JLabel auxLabel = new JLabel();
			        auxLabel.setBounds(10 + messageText.getX(), 5 + messageText.getY() + messageText.getHeight(), 200, 20);
			        auxLabel.setText("⤷");
			        panel.add(auxLabel);
			        
			        JTextArea answer = new JTextArea();
			        answer.setText(review.getDriverAnswer());
			        answer.setEditable(true);
			        answer.setWrapStyleWord(true);
			        answer.setLineWrap(true);
			        JScrollPane auxScroll = new JScrollPane(answer);
			        // Won't show the scroll bar
			        auxScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			        auxScroll.setBounds(auxLabel.getX(), 3 + auxLabel.getY() + auxLabel.getHeight(), 440, 70);
			        panel.add(auxScroll);
			        
			        // JButton to confirm the answer to the review
			        JButton confirmAnswer = new JButton();
			        confirmAnswer.setIcon(new ImageIcon(ImageManagerUtil.readImageFromFile("src/main/resources/sendMessageIcon.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
			        confirmAnswer.setBounds(auxScroll.getX() + auxScroll.getWidth() + 2, auxScroll.getY() + (auxScroll.getHeight() - 25),
			        	25, 25);
			        confirmAnswer.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							facade.addDriverAnswer(review, answer.getText());
							refresh();
						}
					});
			        panel.add(confirmAnswer);	
			        revalidate();
			        repaint();
				}
			});

		}
		
        
        return panel;
    }
    
    // Refresh the frame
    private void refresh() {
    	this.contentPane.removeAll();
    	init();
    	this.repaint();
    	this.revalidate();
    }
	
}