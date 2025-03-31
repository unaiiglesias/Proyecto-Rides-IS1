package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Driver;
import domain.ReservationRequest;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PendingInboundRequestsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private Driver driver;
	private BLFacade facade;
	
	private JPanel contentPane;
	
	private JScrollPane tableScrollPane;
	private JTable table;
	private DefaultTableModel tableModel;
	private String[] columnNamesTable = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.Date"),
			ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NumSeats"),
			ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderName"), 
			ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RiderEmail"), 
			ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.RequestState") 
		};
	private JButton acceptButton;
	private JLabel noSeatsAvaliableErrorLabel;
	private JButton rejectButton;
	private JLabel noPendingReservationRequestsLabel;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public PendingInboundRequestsGUI(Driver d) {
		
		// Utility variables
		this.driver = d;
		facade = MainGUI.getBusinessLogic();
		
		// JFrame Config
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(604, 505);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Top label
		JLabel headerLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowPendingRequestsGUI.headerLabel"));
		headerLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setBounds(10, 11, 568, 30);
		contentPane.add(headerLabel);
		
		// Table scroll pane renderer
		tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(10, 52, 568, 301);
		contentPane.add(tableScrollPane);
		
		// Table
		table = new JTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				int selectedRow = table.getSelectedRow();
				if(selectedRow != -1) {
					ReservationRequest rr = (ReservationRequest) tableModel.getValueAt(selectedRow, 5);
					boolean rideSelected = true;
					if(rr == null) 
						rideSelected = false;
					acceptButton.setEnabled(rideSelected);
					rejectButton.setEnabled(rideSelected);
				}
			}
		});
		tableScrollPane.setViewportView(table); // set renderer scroll pane
		tableModel = new DefaultTableModel(null, columnNamesTable) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModel);
		
		// Initialize table stuff
		try {
			tableModel.setDataVector(null, columnNamesTable);
			tableModel.setColumnCount(6);

			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(20);
			table.getColumnModel().getColumn(2).setPreferredWidth(30);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(30);
			
			table.getColumnModel().removeColumn(table.getColumnModel().getColumn(5)); // not shown in JTable (will contain the reservation itself)
									
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// No seats avaliable error label (triggered when trying to accept a requests that asks for too many seats
		noSeatsAvaliableErrorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowPendingRequestsGUI.NoSeatsAvaliableError"));
		noSeatsAvaliableErrorLabel.setBounds(10, 364, 568, 45);
		noSeatsAvaliableErrorLabel.setForeground(new Color(255,0,0));
		noSeatsAvaliableErrorLabel.setVisible(false);
		contentPane.add(noSeatsAvaliableErrorLabel);
		
		// No pending reservation requests label
		noPendingReservationRequestsLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowPendingRequestsGUI.noPendingReservationRequestsLabel"));
		noPendingReservationRequestsLabel.setBounds(10, 364, 568, 45);
		noPendingReservationRequestsLabel.setForeground(new Color(255,0,0));
		noPendingReservationRequestsLabel.setVisible(false);
		contentPane.add(noPendingReservationRequestsLabel);
		
		// Reject request button
		rejectButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowPendingRequestsGUI.rejectButton"));
		rejectButton.setBounds(129, 410, 150, 45);
		contentPane.add(rejectButton);
		rejectButton.setEnabled(false);
		rejectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1)
					return; // Some kind of error happened (shouldn't happen)
				ReservationRequest rr = (ReservationRequest) tableModel.getValueAt(selectedRow, 5);
				facade.modifyReservationRequestState(rr, "rejected");
				updateReservations();
				rejectButton.setEnabled(false);
				acceptButton.setEnabled(false);
			}
		});

		// Accept request button
		acceptButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("ShowPendingRequestsGUI.acceptButton"));
		acceptButton.setBounds(309, 410, 150, 45);
		contentPane.add(acceptButton);
		acceptButton.setEnabled(false);
		acceptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1)
					return; // Some kind of error happened (shouldn't happen)
				ReservationRequest rr = (ReservationRequest) tableModel.getValueAt(selectedRow, 5);
				Boolean accepted = facade.modifyReservationRequestState(rr, "accepted");
				if(!accepted) noSeatsAvaliableErrorLabel.setVisible(true);
				else noSeatsAvaliableErrorLabel.setVisible(false);
				updateReservations();
				acceptButton.setEnabled(false);
				rejectButton.setEnabled(false);
			}
		});
		
		// Fill the table with data
		updateReservations();

	}
	
	
	public void updateReservations() {
		
		List<ReservationRequest> rrList = facade.getReservationRequestsOfDriver(driver, false, "pending");
		rrList.addAll(facade.getReservationRequestsOfDriver(driver, false, "accepted"));
		
		tableModel.setRowCount(0);
		if (rrList.isEmpty()) noPendingReservationRequestsLabel.setVisible(true);
		for (ReservationRequest rr : rrList){
			Vector<Object> row = new Vector<Object>();
			row.add(rr.getStringDate());
			row.add(rr.getNumSeats());
			row.add(rr.getRider().getName());
			row.add(rr.getRider().getEmail());
			row.add(rr.getReservationState());
			row.add(rr);
			tableModel.addRow(row);
		}
	}

}
