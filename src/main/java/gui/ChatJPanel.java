package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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

import org.omg.CORBA.CTX_RESTRICT_SCOPE;

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
        	
        	MessagePanel msgPanel = new MessagePanel(m);
        	// Introducimos el panel con el mensaje en otro contenedor, para poder ajustar la alineación según el emisor
        	JPanel contenedor = new JPanel();
        	contenedor.setOpaque(false);
        	if(m.getAuthor().getEmail().equals(currentUser.getEmail())){
        		contenedor.setLayout(new FlowLayout(FlowLayout.RIGHT));
        		contenedor.add(msgPanel);
        	} else {
        		contenedor.setLayout(new FlowLayout(FlowLayout.LEFT));
        		contenedor.add(msgPanel);
        	}
    		contenedor.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    		//contenedor.setPreferredSize(new Dimension(650, msgPanel.getHeight()));
        	conversationPanel.add(contenedor);
        	
        	// Add a separator
        	conversationPanel.add(Box.createVerticalStrut(5));
        	
        }
        // Force the graphics to update the components of the conversation panel
        conversationPanel.revalidate();
        conversationPanel.repaint();
	}
}
