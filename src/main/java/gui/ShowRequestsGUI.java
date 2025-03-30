package gui;

import java.awt.Color;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
	private BLFacade facade;
	
	private JLabel rideSelectionLabel;
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
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NumSeats"),
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderName"), 
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderEmail"), 
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RequestState") 
	};
	private JLabel reservationsJLabel;
	private JButton acceptReservationJButton;
	private JLabel jLabelError;


	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public ShowRequestsGUI(Driver d) {
		
		/**
		 * WARNING: This class is currently unused as it has been replaced by ShowPendingRequestsGUI.
		 * 			It will likely be deleted in the future.
		 */
		
		// Utility variables
		this.driver = d;
		facade = MainGUI.getBusinessLogic();
		
		// JFrame config
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(604, 459);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Ride selection label
		rideSelectionLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RideSelectionLabel"));
		rideSelectionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		rideSelectionLabel.setVerticalAlignment(SwingConstants.TOP);
		rideSelectionLabel.setBounds(10, 51, 119, 34);
		contentPane.add(rideSelectionLabel);
		
		// No seats avaliable error label (triggered when trying to accept a requests that asks for too many seats
		jLabelError = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NoSeatsAvaliableError"));
		jLabelError.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelError.setVerticalAlignment(SwingConstants.TOP);
		jLabelError.setBounds(54, 387, 500, 22);
		jLabelError.setForeground(new Color(255,0,0));
		jLabelError.setVisible(false);
		contentPane.add(jLabelError);
		
		// Ride selection comboBox (Choose ride for which to show 
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
					reservationsTableModel.setColumnCount(6);

					reservationsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
					reservationsTable.getColumnModel().getColumn(1).setPreferredWidth(20);
					reservationsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
					reservationsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
					reservationsTable.getColumnModel().getColumn(4).setPreferredWidth(30);
					
					reservationsTable.getColumnModel().removeColumn(reservationsTable.getColumnModel().getColumn(5)); // not shown in JTable
					
					updateReservations();
					
					acceptReservationJButton.setEnabled(false);
					jLabelError.setVisible(false);
					
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
		reservationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				int selectedRow = reservationsTable.getSelectedRow();
				if(selectedRow != -1) {
					ReservationRequest rr = (ReservationRequest) reservationsTableModel.getValueAt(selectedRow, 5);
					if(!rr.getReservationState().equalsIgnoreCase("accepted")) acceptReservationJButton.setEnabled(true);
					else acceptReservationJButton.setEnabled(false);
				}
			}
		});
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
		
		acceptReservationJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.Accept"));
		acceptReservationJButton.setEnabled(false);
		acceptReservationJButton.setBounds(218, 342, 161, 34);
		contentPane.add(acceptReservationJButton);
		
		acceptReservationJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// Remove the ride and update the Ride's table
				int selectedRow = reservationsTable.getSelectedRow();
				if(selectedRow != -1) {					
					ReservationRequest rr = (ReservationRequest) reservationsTableModel.getValueAt(selectedRow, 5);
					Boolean accepted = facade.modifyReservationRequestState(rr, "accepted");
					if(!accepted) jLabelError.setVisible(true);
					else jLabelError.setVisible(false);
					updateReservations();
					acceptReservationJButton.setEnabled(false);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		
		// Make default selection (if possible)
		if (ridesModel.getSize() > 0) 
			ridesComboBox.setSelectedIndex(0);
		
	}
	
	public void updateReservations() {
		List<ReservationRequest> rrList=facade.getReservationsOfRide(selectedRide, null);
		reservationsTableModel.setRowCount(0);
		if (rrList.isEmpty()) reservationsJLabel.setVisible(true);
		for (ReservationRequest rr : rrList){
			Vector<Object> row = new Vector<Object>();
			row.add(rr.getStringDate());
			row.add(rr.getNumSeats());
			row.add(rr.getRider().getName());
			row.add(rr.getRider().getEmail());
			row.add(rr.getReservationState());
			row.add(rr);
			reservationsTableModel.addRow(row);
		}
	}
}
