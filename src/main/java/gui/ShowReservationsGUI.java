package gui;

import java.awt.Color;
import java.awt.EventQueue;

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
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

public class ShowReservationsGUI extends JFrame {

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
	private JButton removeReservationJButton;
	

	

	/**
	 * Create the frame.
	 */
	public ShowReservationsGUI(Rider user) {
		
		currentUser = user;
		facade = MainGUI.getBusinessLogic();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 869, 705);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		
		jLabelRidesDone = new JLabel("Rides done");
		jLabelRidesDone.setBounds(20, 14, 571, 32);
		contentPane.add(jLabelRidesDone);
		
		scrollPaneRidesDone = new JScrollPane();
		scrollPaneRidesDone.setBounds(20, 57, 807, 244);
		contentPane.add(scrollPaneRidesDone);

		ridesDoneTable = new JTable();
		scrollPaneRidesDone.setViewportView(ridesDoneTable);
		ridesDoneTableModel = new DefaultTableModel(null, columnNamesTable1) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ridesDoneTable.setModel(ridesDoneTableModel);
		
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
			e1.printStackTrace();
		}
		
		jLabelReservationRequests = new JLabel("Reservation requests");
		jLabelReservationRequests.setBounds(20, 310, 571, 32);
		contentPane.add(jLabelReservationRequests);
		
		scrollPaneReservationRequests = new JScrollPane();
		scrollPaneReservationRequests.setBounds(20, 353, 807, 244);
		contentPane.add(scrollPaneReservationRequests);
		
		reservationRequestsTable = new JTable();
		reservationRequestsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				int selectedRow = reservationRequestsTable.getSelectedRow();
				if(selectedRow != -1) {
					removeReservationJButton.setEnabled(true);
				}
			}
		});
		scrollPaneReservationRequests.setViewportView(reservationRequestsTable);
		reservationRequestsTableModel = new DefaultTableModel(null, columnNamesTable2) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reservationRequestsTable.setModel(reservationRequestsTableModel);
		
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
			e1.printStackTrace();
		}
		
		removeReservationJButton = new JButton("Remove Reservation Request");
		removeReservationJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Remove the ride and update the Ride's table
				int selectedRow = reservationRequestsTable.getSelectedRow();
				if(selectedRow != -1) {					
					ReservationRequest rr = (ReservationRequest) reservationRequestsTableModel.getValueAt(selectedRow, 7);
					facade.removeReservation(rr);
					updateReservationRequests();
					removeReservationJButton.setEnabled(false);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		removeReservationJButton.setBounds(309, 608, 223, 32);
		removeReservationJButton.setEnabled(false);
		contentPane.add(removeReservationJButton);
		
	}
	
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
