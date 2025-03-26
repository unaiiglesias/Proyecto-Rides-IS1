package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.*;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class ShowReservationsHistoryGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private BLFacade facade;
	private Rider currentUser;
	
	private JPanel contentPane;
	private JScrollPane scrollPaneRidesDone, scrollPaneReservationRequests;
	private JLabel jLabelRidesDone, jLabelReservationRequests;
	private JTable ridesDoneTable, reservationRequestsTable;
	private DefaultTableModel ridesDoneTableModel, reservationRequestsTableModel;
	private String[] columnNamesTable1 = {
			"Ride's Date", "From", "To", "Seats", "Driver's Name"
	};
	private String[] columnNamesTable2 = {
			"Date", "Ride's Date", "From", "To", "Seats", "Driver's Name", "State"
	};
	private DefaultTableCellRenderer render;
	private JButton cancelReservationJButton;
	private JButton showReviewsButton;
	

	

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public ShowReservationsHistoryGUI(Rider user) {
		
		System.out.println("Displaying: ShowReservationsHistoryGUI");
		
		// Utility variables
		currentUser = user;
		facade = MainGUI.getBusinessLogic();
		
		// JFrame config
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 869, 731);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Rides & Reservation panel"); // TODO: Improve naming and add translations		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// ???
		render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Completed rides label
		jLabelRidesDone = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHistorGUI.RidesDone"));
		jLabelRidesDone.setFont(new Font("Tahoma", Font.BOLD, 16));
		jLabelRidesDone.setForeground(Color.BLACK);
		jLabelRidesDone.setBounds(20, 11, 807, 32);
		contentPane.add(jLabelRidesDone);
		
		// Completed rides scrollpane
		scrollPaneRidesDone = new JScrollPane();
		scrollPaneRidesDone.setBounds(20, 54, 807, 244);
		contentPane.add(scrollPaneRidesDone);
		
		// Completed rides table
		ridesDoneTable = new JTable();
		scrollPaneRidesDone.setViewportView(ridesDoneTable);
		// its model
		ridesDoneTableModel = new DefaultTableModel(null, columnNamesTable1) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ridesDoneTable.setModel(ridesDoneTableModel);
		
		// Fill completed rides with data
		try {
			ridesDoneTableModel.setDataVector(null, columnNamesTable1);
			ridesDoneTableModel.setColumnCount(6);

			ridesDoneTable.getColumnModel().getColumn(0).setPreferredWidth(30);
			ridesDoneTable.getColumnModel().getColumn(1).setPreferredWidth(30);
			ridesDoneTable.getColumnModel().getColumn(2).setPreferredWidth(30);
			ridesDoneTable.getColumnModel().getColumn(3).setPreferredWidth(15);
			ridesDoneTable.getColumnModel().getColumn(4).setPreferredWidth(30);
			
			for(int i=0; i<ridesDoneTableModel.getColumnCount(); i++) ridesDoneTable.getColumnModel().getColumn(i).setCellRenderer(render);
			ridesDoneTable.getColumnModel().removeColumn(ridesDoneTable.getColumnModel().getColumn(5)); // not shown in JTable
			
			updateRidesDone();
			
		} catch (Exception e1) {
			System.out.println("ERROR: Completed rides data insertion failed");
			e1.printStackTrace();
		}
		
		// Pending reservation requests label
		jLabelReservationRequests = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHistorGUI.PendingRequests"));
		jLabelReservationRequests.setFont(new Font("Tahoma", Font.BOLD, 16));
		jLabelReservationRequests.setForeground(Color.BLACK);
		jLabelReservationRequests.setBounds(20, 352, 571, 32);
		contentPane.add(jLabelReservationRequests);
		
		// Pending reservation requests scrollpane
		scrollPaneReservationRequests = new JScrollPane();
		scrollPaneReservationRequests.setBounds(20, 395, 807, 244);
		contentPane.add(scrollPaneReservationRequests);
		
		// Pending reservation requests table
		reservationRequestsTable = new JTable();
		reservationRequestsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				int selectedRow = reservationRequestsTable.getSelectedRow();
				if(selectedRow != -1) {
					cancelReservationJButton.setEnabled(true);
				}
			}
		});
		scrollPaneReservationRequests.setViewportView(reservationRequestsTable);
		// its model
		reservationRequestsTableModel = new DefaultTableModel(null, columnNamesTable2) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reservationRequestsTable.setModel(reservationRequestsTableModel);
		
		// Fill pending reservation requests table with data
		try {
			reservationRequestsTableModel.setDataVector(null, columnNamesTable2);
			reservationRequestsTableModel.setColumnCount(8);

			reservationRequestsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
			reservationRequestsTable.getColumnModel().getColumn(1).setPreferredWidth(30);
			reservationRequestsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
			reservationRequestsTable.getColumnModel().getColumn(3).setPreferredWidth(30);
			reservationRequestsTable.getColumnModel().getColumn(4).setPreferredWidth(15);
			reservationRequestsTable.getColumnModel().getColumn(5).setPreferredWidth(30);
			reservationRequestsTable.getColumnModel().getColumn(6).setPreferredWidth(20);
			
			for(int i=0; i<reservationRequestsTableModel.getColumnCount(); i++) reservationRequestsTable.getColumnModel().getColumn(i).setCellRenderer(render);
			reservationRequestsTable.getColumnModel().removeColumn(reservationRequestsTable.getColumnModel().getColumn(7)); // not shown in JTable
			
			updateReservationRequests();
			
		} catch (Exception e1) {
			System.out.println("ERROR: pending reservation requests data insertion failed");
			e1.printStackTrace();
		}
		
		// Cancel reservation button
		cancelReservationJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHistorGUI.CancelRequest"));
		cancelReservationJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Remove the ride and update the Ride's table
				int selectedRow = reservationRequestsTable.getSelectedRow();
				if(selectedRow != -1) {					
					ReservationRequest rr = (ReservationRequest) reservationRequestsTableModel.getValueAt(selectedRow, 7);
					facade.removeReservation(rr);
					updateReservationRequests();
					cancelReservationJButton.setEnabled(false);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		cancelReservationJButton.setBounds(284, 650, 223, 32);
		cancelReservationJButton.setEnabled(false);
		contentPane.add(cancelReservationJButton);
		
		// Add review button
		JButton addReviewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHisttoryGUI.addReviewButton"));
		addReviewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: Add functionality
			}
		});
		addReviewButton.setBounds(159, 309, 223, 32);
		contentPane.add(addReviewButton);
		
		// Show reviews button
		showReviewsButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHisttoryGUI.showReviews"));
		// TODO: Add functionality
		showReviewsButton.setBounds(392, 309, 223, 32);
		contentPane.add(showReviewsButton);
		
	}
	
	// Update completed rides table data
	public void updateRidesDone() {
		List<ReservationRequest> rrList=facade.getRidesDoneByRider(currentUser);
		
		ridesDoneTableModel.setRowCount(0);
		for (ReservationRequest rr : rrList){
			Vector<Object> row = new Vector<Object>();
			Ride ride = rr.getRide();
			row.add(ride.getStringDate());
			row.add(ride.getFrom());
			row.add(ride.getTo());
			row.add(rr.getNumSeats());
			row.add(ride.getDriver().getName());
			row.add(ride);
			ridesDoneTableModel.addRow(row);
		}
	}
	
	// Update pending reservation requests table data
	public void updateReservationRequests() {
		List<ReservationRequest> rrList=facade.getFutureRidesOfRider(currentUser);
		
		reservationRequestsTableModel.setRowCount(0);
		for (ReservationRequest rr : rrList){
			Vector<Object> row = new Vector<Object>();
			Ride ride = rr.getRide();
			row.add(rr.getStringDate());
			row.add(ride.getStringDate());
			row.add(ride.getFrom());
			row.add(ride.getTo());
			row.add(rr.getNumSeats());
			row.add(ride.getDriver().getName());
			row.add(rr.getReservationState());
			row.add(rr);
			reservationRequestsTableModel.addRow(row);
		}
		
	}
}
