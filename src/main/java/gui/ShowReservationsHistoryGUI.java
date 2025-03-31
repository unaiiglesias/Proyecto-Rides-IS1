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
	private JButton cancelReservationButton;
	private JButton addReviewButton;
	private JButton payReservationButton;
	private JLabel paymentErrorLabel;
	private JButton rateReviewsButton;
	

	

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
		
		// Render to align to the center the elements of a table
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
		ridesDoneTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				addReviewButton.setEnabled(true);
				int selectedRow = ridesDoneTable.getSelectedRow();
				if(selectedRow != -1) {
					rateReviewsButton.setEnabled(true);
					Ride ride = (Ride) ridesDoneTableModel.getValueAt(selectedRow, 5);
					List<Review> reviews = ride.getReviews();
					for(Review rw : reviews) {
						if(rw.getRider().getEmail().equals(currentUser.getEmail())) {
							addReviewButton.setEnabled(false);
							break;
						}
					}
				}
				else {
					rateReviewsButton.setEnabled(false);
				}
			}
		});
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
					ReservationRequest selectedRide = (ReservationRequest) reservationRequestsTableModel.getValueAt(selectedRow, 7);
					cancelReservationButton.setEnabled(true);
					
					if (selectedRide.getReservationState().equals("accepted"))
						payReservationButton.setEnabled(true);
					else
						payReservationButton.setEnabled(false);
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
		cancelReservationButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHistorGUI.CancelRequest"));
		cancelReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Remove the ride and update the Ride's table
				int selectedRow = reservationRequestsTable.getSelectedRow();
				if(selectedRow != -1) {					
					ReservationRequest rr = (ReservationRequest) reservationRequestsTableModel.getValueAt(selectedRow, 7);
					facade.removeReservation(rr);
					updateReservationRequests();
					cancelReservationButton.setEnabled(false);
					payReservationButton.setEnabled(false);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		cancelReservationButton.setBounds(159, 650, 223, 32);
		cancelReservationButton.setEnabled(false);
		contentPane.add(cancelReservationButton);
		
		addReviewButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHisttoryGUI.addReviewButton"));
		addReviewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Get the selected Ride
				int selectedRow = ridesDoneTable.getSelectedRow();
				if(selectedRow != -1) {
					Ride ride = (Ride) ridesDoneTableModel.getValueAt(selectedRow, 5);				
					// Display user data input
					CreateReviewGUI a = new CreateReviewGUI();
					a.setVisible(true);
					// Check if review was done
					if(a.reviewDone) {
						facade.addReview(a.starsGiven, a.message, ride, currentUser, ride.getDriver());
						updateRidesDone();
						addReviewButton.setEnabled(false);
					}
				}
			}
		});
		addReviewButton.setBounds(159, 309, 223, 32);
		contentPane.add(addReviewButton);
		
		rateReviewsButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHisttoryGUI.showReviews"));
		rateReviewsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = ridesDoneTable.getSelectedRow();
				if(selectedRow != -1) {
	                ShowReviewsDialog a = new ShowReviewsDialog(facade.getReviewsOfDriver(((Ride) ridesDoneTableModel.getValueAt(selectedRow, 5)).getDriver()), currentUser);
	                a.setVisible(true);
				}
			}
		});
		rateReviewsButton.setBounds(392, 309, 223, 32);
		rateReviewsButton.setEnabled(false);
		contentPane.add(rateReviewsButton);
		
		// Pay reservation button
		payReservationButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHistoryGUI.payReservationButton"));
		payReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = reservationRequestsTable.getSelectedRow();
				if (selectedRow == -1)
				{
					// Wrong row selected, shouldn't happen
					return;
				}
				ReservationRequest rr = (ReservationRequest) reservationRequestsTableModel.getValueAt(selectedRow, 7);
				boolean payment = facade.payReservationRequest(rr, user);
				if (payment)
				{
					payReservationButton.setEnabled(false);
					cancelReservationButton.setEnabled(false);
				}
				else
					paymentErrorLabel.setVisible(true);
				
				updateReservationRequests();
			}
		});
		payReservationButton.setBounds(392, 650, 223, 31);
		payReservationButton.setEnabled(false);
		contentPane.add(payReservationButton);
		
		// Payment error label
		paymentErrorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowReservationsHistoryGUI.paymentErrorLabel"));
		paymentErrorLabel.setVerticalAlignment(SwingConstants.TOP);
		paymentErrorLabel.setBounds(625, 638, 218, 54);
		paymentErrorLabel.setForeground(new Color(255,0,0));
		paymentErrorLabel.setVisible(false);
		contentPane.add(paymentErrorLabel);
		
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
		reservationRequestsTableModel.setRowCount(0);
		List<ReservationRequest> rrList;
		
		// We also want to display accepted ones that still haven't been paid
		rrList = facade.getReservationRequestsOfRider(currentUser, false, "accepted");
		// We want to display all future pending reservation requests
		rrList.addAll(facade.getReservationRequestsOfRider(currentUser, false, "pending"));

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
