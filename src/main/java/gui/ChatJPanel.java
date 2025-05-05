package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import util.ImageManagerUtil;

import javax.swing.border.EmptyBorder;

import businessLogic.*;
import domain.*;

public class ChatJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel bottomPanel;
	private JPanel otherUserPanel;
	private JPanel conversationPanel;
	
	private BLFacade facade;
	private Chat chat;
	private Rider currentUser;
	private Timer msgUpdateTimer;
	private int numMessages = 0;


	/**
	 * Create the frame.
	 */
	public ChatJPanel(Chat chat, Rider currentUser) {
		
		this.facade = MainGUI.getBusinessLogic();
		this.chat = chat;
		this.currentUser = currentUser;
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 800, 630);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);
		
        /*
         * Panel at the top, where the other user's info will be shown
         */
		
        otherUserPanel = new JPanel();
        otherUserPanel.setBounds(0, 0, 799, 50);
        otherUserPanel.setLayout(new BoxLayout(otherUserPanel, BoxLayout.X_AXIS));
        this.add(otherUserPanel);
        
        // JButton with the other user's account image
        JButton imageButton = new JButton();
        imageButton.setPreferredSize(new Dimension(50, 50));
        //imageButton.setIcon(); // TODO
        otherUserPanel.add(imageButton);
        
        otherUserPanel.add(Box.createHorizontalStrut(10));
        // JLabel with the name of the other user
        JLabel otherName = new JLabel("Nombre");
        otherUserPanel.add(otherName);
        
        
        /*
         * Panel at the middle, where all messages will be shown
         */
        
        conversationPanel = new JPanel();
        conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.Y_AXIS));
        conversationPanel.setSize(new Dimension(799, 527));
        JScrollPane msgScroll = new JScrollPane(conversationPanel);
        msgScroll.setBounds(0, 49, 799, 527);
        msgScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(msgScroll);
        
        msgUpdateTimer = new Timer(1000, e -> updateMessages());
        msgUpdateTimer.start();
        
		/*
		 * Panel at the bottom, where user will be able to send a message.
		 */
		bottomPanel = new JPanel();
		bottomPanel.setBounds(10, 579, 779, 48);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		this.add(bottomPanel);
		
		// New message input text area
        JTextArea messageInput = new JTextArea();
        messageInput.setText("");
        messageInput.setEditable(true);
        messageInput.setWrapStyleWord(true);
        messageInput.setLineWrap(true);
        JScrollPane inputScroll = new JScrollPane(messageInput);
        inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScroll.setPreferredSize(new Dimension());
        bottomPanel.add(inputScroll);
        
        // Send message button
        JButton sendMessage = new JButton();
        sendMessage.setBackground(Color.WHITE);
        sendMessage.setSize(new Dimension(50, 50));
        sendMessage.setPreferredSize(new Dimension(50, 50));
        sendMessage.setIcon(new ImageIcon(ImageManagerUtil.readImageFromFile("src/main/resources/sendMessageIcon.png").getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        sendMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = messageInput.getText();
				// TODO: improvement: only messages that contain at least one character can be sent
				if(msg != null) {
					facade.addMessageToChat(msg, currentUser, chat);
					messageInput.setText("");
					updateMessages(); // not necessary since messages update every second
				}
			}
		});
        bottomPanel.add(sendMessage);        
	}
	
	/*
	 * Method to stop the timer
	 */
	public void stopTimer() {
		this.msgUpdateTimer.stop();
	}
	
	/*
	 * Método auxiliar para generar el panel que encapsula a cada mensaje
	 */
	private JPanel createMessagePanel(Message msg) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JTextArea msgArea = new JTextArea();
		msgArea.setText(msg.getMessage());
		msgArea.setEditable(false);
		msgArea.setWrapStyleWord(true);
		msgArea.setLineWrap(true);
		// Set its height automatically
		FontMetrics fontMetrics = msgArea.getFontMetrics(msgArea.getFont());
		int rows = (int) Math.ceil(fontMetrics.stringWidth(msg.getMessage()) / 400.0);
		int height = rows * fontMetrics.getHeight(); // number of rows that the message will occupy * height of each row
		// Update the size of the panel
		int width =  400;
		if(rows == 1) width = fontMetrics.stringWidth(msg.getMessage());
		msgArea.setSize(new Dimension(width, height));
		msgArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		msgArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(msgArea);
		
		JLabel dateLabel = new JLabel(msg.getStringDateHourMin());
		dateLabel.setSize(new Dimension(30, 15));
		dateLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(dateLabel);
		
		// modifications
		panel.setBackground(Color.white);
		panel.setSize(new Dimension(msgArea.getWidth(), msgArea.getHeight() + dateLabel.getHeight()));
		return panel;		
	}
	
	// Auxiliary method to update chat's messages
	private void updateMessages() {
        
		// Update current chat
		this.chat = facade.findChat(chat); 
		// Get chat's messages
        List<Message> messages = chat.getMessages();
        if(messages.size() <= numMessages) return;
        
        numMessages = messages.size();
        // Remove all the previous content in the conversation panel
        conversationPanel.removeAll();
        String sDate = "1900-01-01";
        LocalDate lastDate = LocalDate.parse(sDate);
        for(Message m : messages){
        	sDate = m.getStringDate();
        	LocalDate msgDate = LocalDate.parse(sDate, DateTimeFormatter.ISO_LOCAL_DATE);
        	if(msgDate.until(lastDate).getDays() >= 1){
        		// Add a JLabel with the date sDate
        		JLabel dateLabel = new JLabel(sDate);
        		dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        		conversationPanel.add(dateLabel);
        	}
        	
        	JPanel msgPanel = createMessagePanel(m);
        	if(m.getAuthor().getEmail().equals(currentUser.getEmail())){
        		msgPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        	} else {
        		msgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        	}
    		msgPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    		
        	conversationPanel.add(msgPanel);
        	
        	// Add a separator
        	conversationPanel.add(Box.createVerticalStrut(5));
        	
        }
        // Force the graphics to update the components of the conversation panel
        conversationPanel.revalidate();
        conversationPanel.repaint();
	}
}
