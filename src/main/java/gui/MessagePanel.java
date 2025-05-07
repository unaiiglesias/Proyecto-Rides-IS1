package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.objectdb.o.AGN.t;
import com.objectdb.o.CLN.n;

import domain.Message;
import domain.Rider;

public class MessagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea msgArea;
	private JLabel dateLabel;
	private final int maxWidth = 650;

	/**
	 * Create the panel.
	 */
	public MessagePanel(Message msg) {

		setLayout(null);
		setBackground(Color.white);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		msgArea = new JTextArea();
		msgArea.setText(msg.getMessage());
		msgArea.setEditable(false);
		msgArea.setWrapStyleWord(true);
		msgArea.setLineWrap(true);
		msgArea.setOpaque(false);
		// Set its height automatically
		FontMetrics fontMetrics = msgArea.getFontMetrics(msgArea.getFont());
		int rows = (int) Math.floor(fontMetrics.stringWidth(msg.getMessage()) / (float) maxWidth);
		int height =  (rows + 1) * fontMetrics.getHeight(); // number of rows that the message will occupy * height of each row
		// Update the size of the panel
		int nWidth =  0;
		Dimension textDim = null;
		if(rows == 0) {
			msgArea.setSize(fontMetrics.stringWidth(msg.getMessage()), Short.MAX_VALUE);
			nWidth = fontMetrics.stringWidth(msg.getMessage());
			textDim = new Dimension(nWidth, height);
			
		}
		else {
			msgArea.setSize(maxWidth, Short.MAX_VALUE);
			textDim = msgArea.getPreferredSize();
		}
		
		msgArea.setBounds(0, 0, textDim.width, textDim.height);
		this.add(msgArea);
		
		nWidth = textDim.width;
		if(textDim.width < 50) nWidth = 50;
		
		dateLabel = new JLabel(msg.getStringDateHourMin());
		dateLabel.setBounds(nWidth - 50, textDim.height, 50, 20);
		this.add(dateLabel);
		
		setBounds(0, 0, nWidth, msgArea.getHeight() + dateLabel.getHeight() + 5);
		setPreferredSize(new Dimension(nWidth, msgArea.getHeight() + dateLabel.getHeight() + 5));
		setSize(nWidth, msgArea.getHeight() + dateLabel.getHeight() + 10);		
	}

}
