package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.text.SimpleDateFormat;

import javax.swing.*;

import util.ImageManagerUtil;

import javax.swing.border.EmptyBorder;

import businessLogic.*;
import domain.*;

public class ChatGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel bottomPanel;
	private JPanel otherUserPanel;
	private JPanel conversationPanel;
	
	private BLFacade facade;


	/**
	 * Create the frame.
	 */
	public ChatGUI(Chat chat, Rider currentUser) {
		
		this.facade = MainGUI.getBusinessLogic();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 815, 677);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
        /*
         * Panel at the top, where the other user's info will be shown
         */
		
        otherUserPanel = new JPanel();
        otherUserPanel.setBounds(0, 0, 799, 50);
        otherUserPanel.setLayout(new BoxLayout(otherUserPanel, BoxLayout.X_AXIS));
        contentPane.add(otherUserPanel);
        
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
        conversationPanel.setLayout(new BoxLayout(conversationPanel, BoxLayout.X_AXIS));
        updateMessages();
        JScrollPane msgScroll = new JScrollPane(conversationPanel);
        msgScroll.setBounds(0, 49, 799, 527);
        msgScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(msgScroll);
        
		/*
		 * Panel at the bottom, where user will be able to send a message.
		 */
		bottomPanel = new JPanel();
		bottomPanel.setBounds(10, 579, 779, 48);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		contentPane.add(bottomPanel);
		
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
        bottomPanel.add(sendMessage);
        
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
		msgArea.setPreferredSize(new Dimension(650, Short.MAX_VALUE));
		msgArea.setPreferredSize(msgArea.getPreferredSize());
		msgArea.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(msgArea);
		
		JLabel dateLabel = new JLabel(msg.getStringDate());
		dateLabel.setAlignmentX(RIGHT_ALIGNMENT);
		panel.add(dateLabel);
		return panel;		
	}
	
	// Auxiliar method to update chat's messages
	private void updateMessages() {
        /*
        List<Message> messages = facade.getMessagesFromChat(chat);
        ;
		String sDate = (new SimpleDateFormat("yyyy-MM-dd")).format(Date.from(Instant.MIN));
        LocalDate lastDate = LocalDate.parse(sDate);
        for(Message m : messages){
        	sDate = m.getStringDate();
        	LocalDate msgDate = LocalDate.parse(sDate, DateTimeFormatter.ISO_LOCAL_DATE);
        	if(isFutureDay(msgDate, lastDate)){
        		// Add a JLabel with the date sDate
        		JLabel dateLabel = new JLabel(sDate);
        		dateLabel.setAlignmentX(CENTER_ALIGNMENT);
        		conversationPanel.add(dateLabel);
        	}
        	
        	JPanel msgPanel = createMessagePanel(m.getMessage());
        	if(m.getAuthor().equals(currentUser)){
        		msgPanel.setAlignmentX(RIGHT_ALIGNMENT);
        	} else {
        		msgPanel.setAlignmentX(LEFT_ALIGNMENT);
        	}
        	conversationPanel.add(msgPanel);
        }
        */
	}
}
