package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Driver;
import domain.ReservationRequest;
import domain.Ride;

import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class ShowRequestsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Driver driver;
	
	private JLabel jLabelSelectRide;
	private JComboBox<Ride> ridesComboBox;
	private DefaultComboBoxModel<Ride> ridesModel = new DefaultComboBoxModel<Ride>();
	private JLabel jLabelRides;
	
	private Ride selectedRide;
	private JButton showReservationsJButton;
	private JTable reservationsTable;
	private DefaultTableModel reservationsTableModel;
	private JScrollPane reservationsScrollPane;
	private String[] columnNamesTable = new String[] {
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.Date"),
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderName"), 
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderEmail"), 
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RequestState") 
	};
	private JLabel reservationsJLabel;


	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public ShowRequestsGUI(Driver d) {
		
		this.driver = d;
		BLFacade facade = MainGUI.getBusinessLogic();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(604, 370));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jLabelSelectRide = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.Ride"));
		jLabelSelectRide.setHorizontalAlignment(SwingConstants.LEFT);
		jLabelSelectRide.setVerticalAlignment(SwingConstants.TOP);
		jLabelSelectRide.setBounds(10, 51, 119, 34);
		contentPane.add(jLabelSelectRide);
		
		ridesComboBox = new JComboBox<Ride>();
		ridesComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedRide = (Ride) ridesComboBox.getSelectedItem();
				reservationsJLabel.setVisible(false);
				showReservationsJButton.setEnabled(true);
			}
		});
		
		// Set the model for the ComboBox
		for (Ride r: facade.getPosteriorRidesOfDriver(driver))
			ridesModel.addElement(r);
		ridesComboBox.setModel(ridesModel);
		ridesComboBox.setBounds(141, 52, 317, 22);
		contentPane.add(ridesComboBox);
		
		jLabelRides = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NoRides"));
		jLabelRides.setForeground(Color.red);
		jLabelRides.setBounds(141, 33, 339, 14);
		if(ridesModel.getSize()!=0) jLabelRides.setVisible(false);
		contentPane.add(jLabelRides);
		
		showReservationsJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.JButton"));
		showReservationsJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					reservationsTableModel.setDataVector(null, columnNamesTable);
					reservationsTableModel.setColumnCount(4);

					reservationsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
					reservationsTable.getColumnModel().getColumn(1).setPreferredWidth(30);
					reservationsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
					reservationsTable.getColumnModel().getColumn(3).setPreferredWidth(30);

					List<ReservationRequest> rrList=facade.getReservationsOfRide(selectedRide);

					if (rrList.isEmpty()) reservationsJLabel.setVisible(true);
					for (ReservationRequest rr : rrList){
						Vector<Object> row = new Vector<Object>();
						row.add(rr.getStringDate());
						row.add(rr.getRider().getName());
						row.add(rr.getRider().getEmail());
						row.add(rr.getReservationState());
						reservationsTableModel.addRow(row);		
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		showReservationsJButton.setBounds(218, 85, 161, 34);
		showReservationsJButton.setEnabled(false);
		contentPane.add(showReservationsJButton);
		
		reservationsScrollPane = new JScrollPane();
		reservationsScrollPane.setBounds(54, 148, 489, 183);
		contentPane.add(reservationsScrollPane);
		
		reservationsTable = new JTable();
		reservationsScrollPane.setViewportView(reservationsTable);
		reservationsTableModel = new DefaultTableModel(null, columnNamesTable) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reservationsTable.setModel(reservationsTableModel);
		
		reservationsJLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NoReservations"));
		reservationsJLabel.setForeground(new Color(255, 0, 0));
		reservationsJLabel.setBounds(54, 130, 418, 14);
		reservationsJLabel.setVisible(false);
		contentPane.add(reservationsJLabel);
		
		// Make default selection (if possible)
		if (ridesModel.getSize() > 0) 
			ridesComboBox.setSelectedIndex(0);
		
	}
}
