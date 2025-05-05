package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import domain.*;
import util.ImageManagerUtil;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

public class ChatGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private BLFacade facade;
	private Rider currentUser;
	private Chat currentChat;
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private Timer chatUpdateTimer;
	private JScrollPane leftPanelScroll;
	private JScrollPane rightPanelScroll;
	

	/**
	 * Create the frame.
	 */
	public ChatGUI(Rider currentUser) {
		
		this.facade = MainGUI.getBusinessLogic();
		this.currentUser = currentUser;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				chatUpdateTimer.stop();
				
			}
		});
		setBounds(100, 100, 1050, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		/*
		 * Left Panel: shows all the available chats
		 */
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanelScroll = new JScrollPane(leftPanel);
		leftPanelScroll.setPreferredSize(new Dimension(200, 630));
		leftPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(leftPanelScroll);
		updateChats();

		/*
		 * Right Panel: shows all the messages and allows sending messages of the conversation selected in the leftPanel
		 */
        rightPanel = new JPanel();
		rightPanelScroll = new JScrollPane(rightPanel);
        rightPanelScroll.setPreferredSize(new Dimension(800, 630));
        rightPanelScroll.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		contentPane.add(rightPanelScroll);
		
		
        // Update in periodically the available chats
        chatUpdateTimer = new Timer(5000, e -> updateChats());
        chatUpdateTimer.start();
	}
	
	/*
	 * Auxiliary method to update the available chats 
	 */
	private void updateChats() {
		// Remove all the panels of other users
		leftPanel.removeAll();
		List<Chat> chats;
		// Obtain all the chats of current user (already sorted by last message's date)
		if(currentUser instanceof Driver) {
			chats = facade.getChatsOfUser((Driver) currentUser);
		} else {
			chats = facade.getChatsOfUser(currentUser);
		}
		
		// Add all the eligible chats (JPanel)
		for(Chat c : chats) {
			JPanel choosableChat = otherUserPanel(c);
			choosableChat.setPreferredSize(new Dimension(200, 50));
			leftPanel.add(choosableChat);
		}
		leftPanelScroll.setViewportView(leftPanel);
		leftPanel.revalidate();
		leftPanel.repaint();
		
	}
	
	/*
	 * Auxiliary method to create each JPanel that contains the info of the other user (Profile image + name)
	 * The JPanel will be used as a button, since JButton doesn't allow adding a JLabel into it.
	 */
	private JPanel otherUserPanel(Chat chat) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		// Add a Label with the profile picture
		Rider otherUser = chat.getOtherUser(currentUser);
		ImageIcon profilePic = new ImageIcon(otherUser.getProfilePicIcon().getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		JLabel profilePicLabel = new JLabel();
		profilePicLabel.setIcon(profilePic);
		profilePicLabel.setPreferredSize(new Dimension(50, 50));
		panel.add(profilePicLabel);
		// Add a Label with the name of the other user
		JLabel nameLabel = new JLabel(otherUser.getName());
		panel.add(nameLabel);
		// Add the button functionality
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// When a chat is selected, we'll create and show it's chat (ChatPanel) by setting it as rightPanel
				if(rightPanel instanceof ChatJPanel) {
					((ChatJPanel) rightPanel).stopTimer();
				}
				contentPane.removeAll();
				rightPanel = new ChatJPanel(chat, currentUser);
				rightPanelScroll.setViewportView(rightPanel);
				rightPanelScroll.setPreferredSize(new Dimension(800, 630)); // size of a ChatPanel
				
				contentPane.add(leftPanelScroll);
				contentPane.add(rightPanelScroll);
				rightPanel.revalidate();
				rightPanel.repaint();
				contentPane.revalidate();
				contentPane.repaint();
				revalidate();
				repaint();
			}
		});
		return panel;
	}

}
