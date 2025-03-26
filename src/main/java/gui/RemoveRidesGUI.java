package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Driver;
import domain.ReservationRequest;
import domain.Ride;
import domain.Rider;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;

public class RemoveRidesGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private Driver driver;
	private BLFacade facade;
	private JPanel contentPane;
	private JTable ridesTable;
	private DefaultTableModel ridesTableModel;
	private JScrollPane ridesScrollPane;
	private String[] columnNamesTable = new String[] {
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.Date"),
		ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.LeavingFrom"), 
		ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.GoingTo"), 
		null
	};
	private JLabel ridesJLabel;
	private JButton removeRideJButton;
	private JLabel notRemovedJLabel;
	
	// ShowRequest GUI
	private Ride selectedRide;
	private JTable reservationsTable;
	private DefaultTableModel reservationsTableModel;
	private JScrollPane reservationsScrollPane;
	private String[] columnNamesReservationsTable = new String[] {
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.Date"),
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NumSeats"),
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderName"), 
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderEmail"), 
		ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RequestState") 
	};
	private JLabel jLabelFutureRides;
	private JLabel jLabelPastRides;
	
	private JScrollPane endedRidesScrollPane;
	private JTable endedRidesTable;
	private DefaultTableModel endedRidesTableModel;
	private JLabel jLabelReservations;

	/**
	 * Create the frame.
	 */
	public RemoveRidesGUI(Driver d) {
		
		this.driver = d;
		facade = MainGUI.getBusinessLogic();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(1012, 711));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		
		ridesJLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.NoRides"));
		ridesJLabel.setForeground(new Color(255, 0, 0));
		ridesJLabel.setBounds(473, 287, 453, 14);
		ridesJLabel.setVisible(false);
		contentPane.add(ridesJLabel);
		
		notRemovedJLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.NotRemoved"));
		notRemovedJLabel.setVisible(false);
		notRemovedJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		notRemovedJLabel.setForeground(new Color(255, 0, 0));
		notRemovedJLabel.setBounds(473, 332, 453, 28);
		contentPane.add(notRemovedJLabel);
		
		ridesScrollPane = new JScrollPane();
		ridesScrollPane.setBounds(474, 47, 452, 236);
		contentPane.add(ridesScrollPane);
		
		ridesTable = new JTable();
		ridesScrollPane.setViewportView(ridesTable);
		ridesTableModel = new DefaultTableModel(null, columnNamesTable) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ridesTable.setModel(ridesTableModel);
		
		try {
			ridesTableModel.setDataVector(null, columnNamesTable);
			ridesTableModel.setColumnCount(4);
	
			ridesTable.getColumnModel().getColumn(0).setPreferredWidth(70);
			ridesTable.getColumnModel().getColumn(1).setPreferredWidth(30);
			ridesTable.getColumnModel().getColumn(2).setPreferredWidth(100);

			ridesTable.getColumnModel().removeColumn(ridesTable.getColumnModel().getColumn(3)); // not shown in JTable
	
			updateRides();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// When selected a Ride, we'll display the accepted reservation requests associated to it
		ridesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				endedRidesTable.clearSelection();
				removeRideJButton.setEnabled(true);
				int selectedRow = ridesTable.getSelectedRow();
				if(selectedRow != -1) {
					Ride r = (Ride) ridesTableModel.getValueAt(selectedRow, 3);
					selectedRide = r;
					updateReservations();
				}
			}
		});
	
		removeRideJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.RemoveRide"));
		removeRideJButton.setEnabled(false);
		removeRideJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// Remove the ride and update the Ride's table
				int selectedRow = ridesTable.getSelectedRow();
				if(selectedRow != -1) {					
					Ride r = (Ride) ridesTableModel.getValueAt(selectedRow, 3);
					Boolean removed = facade.removeRide(r);
					notRemovedJLabel.setVisible(false);
					if(!removed) notRemovedJLabel.setVisible(true);
					updateRides();
					removeRideJButton.setEnabled(false);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		removeRideJButton.setBounds(631, 296, 134, 37);
		contentPane.add(removeRideJButton);
		
		
		/*
		 * 
		 * Code from ShowRequestsGUI
		 * 
		 */
		
		reservationsScrollPane = new JScrollPane();
		reservationsScrollPane.setBounds(197, 409, 580, 183);
		contentPane.add(reservationsScrollPane);
		
		reservationsTable = new JTable();

		reservationsScrollPane.setViewportView(reservationsTable);
		reservationsTableModel = new DefaultTableModel(null, columnNamesReservationsTable) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reservationsTable.setModel(reservationsTableModel);
		
		jLabelFutureRides = new JLabel("Active Rides");
		jLabelFutureRides.setForeground(Color.BLACK);
		jLabelFutureRides.setFont(new Font("Tahoma", Font.BOLD, 16));
		jLabelFutureRides.setBounds(473, 11, 453, 32);
		contentPane.add(jLabelFutureRides);
		
		jLabelPastRides = new JLabel("Ended Rides");
		jLabelPastRides.setForeground(Color.BLACK);
		jLabelPastRides.setFont(new Font("Tahoma", Font.BOLD, 16));
		jLabelPastRides.setBounds(10, 11, 453, 32);
		contentPane.add(jLabelPastRides);
		
		reservationsTableModel.setDataVector(null, columnNamesReservationsTable);
		reservationsTableModel.setColumnCount(6);

		reservationsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
		reservationsTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		reservationsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		reservationsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		reservationsTable.getColumnModel().getColumn(4).setPreferredWidth(30);
		
		reservationsTable.getColumnModel().removeColumn(reservationsTable.getColumnModel().getColumn(5)); // not shown in JTable

		
		endedRidesScrollPane = new JScrollPane();
		endedRidesScrollPane.setBounds(10, 47, 452, 236);
		contentPane.add(endedRidesScrollPane);
		
		endedRidesTable = new JTable();
		endedRidesScrollPane.setViewportView(endedRidesTable);
		endedRidesTableModel = new DefaultTableModel(null, columnNamesTable) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		endedRidesTable.setModel(endedRidesTableModel);
		
		jLabelReservations = new JLabel("Reservations of Selected Ride"); //$NON-NLS-1$ //$NON-NLS-2$
		jLabelReservations.setForeground(Color.BLACK);
		jLabelReservations.setFont(new Font("Tahoma", Font.BOLD, 16));
		jLabelReservations.setBounds(197, 371, 453, 32);
		contentPane.add(jLabelReservations);
		
		try {
			endedRidesTableModel.setDataVector(null, columnNamesTable);
			endedRidesTableModel.setColumnCount(4);
	
			endedRidesTable.getColumnModel().getColumn(0).setPreferredWidth(70);
			endedRidesTable.getColumnModel().getColumn(1).setPreferredWidth(30);
			endedRidesTable.getColumnModel().getColumn(2).setPreferredWidth(100);

			endedRidesTable.getColumnModel().removeColumn(endedRidesTable.getColumnModel().getColumn(3)); // not shown in JTable
			updateEndedRides();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// When selected a Ride, we'll display the accepted reservation requests associated to it
		endedRidesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				ridesTable.clearSelection();
				notRemovedJLabel.setVisible(false);
				int selectedRow = endedRidesTable.getSelectedRow();
				if(selectedRow != -1) {
					Ride r = (Ride) endedRidesTableModel.getValueAt(selectedRow, 3);
					selectedRide = r;
					updateReservations();
				}
			}
		});
	}
	
	
	public void updateReservations() {
		List<ReservationRequest> rrList=facade.getAcceptedReservationsOfRide(selectedRide);
		reservationsTableModel.setRowCount(0);
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
	
	
	/**
	 * This method updates the RidesTableModel with the Rides associated to the Driver that are posterior to the current date
	 */
	public void updateRides() {
		List<Ride> rideList=facade.getPosteriorRidesOfDriver(driver);
		ridesTableModel.setRowCount(0);
		
		if (rideList.isEmpty()) ridesJLabel.setVisible(true);
		for (Ride r : rideList){
			Vector<Object> row = new Vector<Object>();
			row.add(r.getStringDate());
			row.add(r.getFrom());
			row.add(r.getTo());
			row.add(r);
			ridesTableModel.addRow(row);		
		}
	}
		
	public void updateEndedRides() {
		List<Ride> rideList=facade.getEndedRidesOfDriver(driver);
		endedRidesTableModel.setRowCount(0);
		
		if (rideList.isEmpty()) ridesJLabel.setVisible(true);
		for (Ride r : rideList){
			Vector<Object> row = new Vector<Object>();
			row.add(r.getStringDate());
			row.add(r.getFrom());
			row.add(r.getTo());
			row.add(r);
			endedRidesTableModel.addRow(row);		
		}
	}
}
