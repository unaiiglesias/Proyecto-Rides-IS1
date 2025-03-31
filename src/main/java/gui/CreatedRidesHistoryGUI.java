package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import domain.Driver;
import domain.ReservationRequest;
import domain.Ride;
import java.awt.Font;

public class CreatedRidesHistoryGUI extends JFrame {

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
	private JLabel noRidesErrorLabel;
	private JButton removeRideButton;
	private JLabel rideNotRemovedErrorLabel;
	
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
	private JLabel ridesTableLabel;
	
	private JLabel selectedRideReservationsLabel;

	private List<Integer> rowsToPaint; // AKA rows that correspond to ended rides
	
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public CreatedRidesHistoryGUI(Driver d) {
		
		// DEBUG
		System.out.println("Openning: RemoveRidesGUI");
		
		// Generic class variables
		this.driver = d;
		facade = MainGUI.getBusinessLogic();
		
		// JFrame config
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(1012, 711));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	
		// This driver has no rides indicator
		noRidesErrorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.NoRides"));
		noRidesErrorLabel.setForeground(new Color(255, 0, 0));
		noRidesErrorLabel.setBounds(197, 287, 453, 14);
		noRidesErrorLabel.setVisible(false);
		contentPane.add(noRidesErrorLabel);
		
		// TODO: Right now a ride can't be removed if it has reservation requests associated with it
		rideNotRemovedErrorLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.NotRemoved"));
		rideNotRemovedErrorLabel.setVisible(false);
		rideNotRemovedErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rideNotRemovedErrorLabel.setForeground(new Color(255, 0, 0));
		rideNotRemovedErrorLabel.setBounds(241, 332, 453, 28);
		contentPane.add(rideNotRemovedErrorLabel);
		
		// Rides history top table label
		ridesTableLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.RidesTableLabel"));
		ridesTableLabel.setForeground(Color.BLACK);
		ridesTableLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		ridesTableLabel.setBounds(77, 11, 850, 32);
		contentPane.add(ridesTableLabel);
		
		// rides table scroll pane container
		ridesScrollPane = new JScrollPane();
		ridesScrollPane.setBounds(77, 49, 850, 236);
		contentPane.add(ridesScrollPane);
		
		// rides table
		ridesTable = new JTable();
		ridesScrollPane.setViewportView(ridesTable);
		ridesTableModel = new DefaultTableModel(null, columnNamesTable) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ridesTable.setModel(ridesTableModel);		
		// Set columns
		try {
			ridesTableModel.setDataVector(null, columnNamesTable);
			ridesTableModel.setColumnCount(4);
	
			ridesTable.getColumnModel().getColumn(0).setPreferredWidth(70);
			ridesTable.getColumnModel().getColumn(1).setPreferredWidth(30);
			ridesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
			
			// TODO: Add state column. Meanwhile, dont show it
			//ridesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
			ridesTable.getColumnModel().removeColumn(ridesTable.getColumnModel().getColumn(3)); // not shown in JTable
	
			updateRides();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// We want the ended rides to be highlighted in yellow, so we'll build a custom cell renderer that does that
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			// This code is called by the table on each cell and it tells it how to render the cell
			// We'll just keep the default behavior and add color to the rows in rowsToPaint (the ones that correspond
			// to finished rides)
			
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // If the state of the Ride is finished, mark it
                // TODO: Once the ride state is implemented, replace this with ride state check
                
                if (rowsToPaint.contains(row)) {
                    cell.setBackground(Color.YELLOW);
                    cell.setForeground(Color.BLACK);
                } else { // Default color
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(Color.BLACK);
                }

                return cell;
            }
        };
        // Apply renderer to all columns
        for (int i = 0; i < ridesTable.getColumnCount(); i++) {
            ridesTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
		// When selected a Ride, we'll display the accepted reservation requests associated to it
		ridesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				int selectedRow = ridesTable.getSelectedRow();
				// we'll only enable removal if the row is not expired
				removeRideButton.setEnabled(!rowsToPaint.contains(selectedRow));
				if(selectedRow != -1) {
					Ride r = (Ride) ridesTableModel.getValueAt(selectedRow, 3);
					selectedRide = r;
					updateReservations();
				}
			}
		});
	
		// Button to remove selected ride form DB
		removeRideButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.RemoveRide"));
		removeRideButton.setEnabled(false);
		removeRideButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				// Remove the ride and update the Ride's table
				int selectedRow = ridesTable.getSelectedRow();
				
				if (selectedRow == -1)
				{
					// TODO: Tell the user that somethign went wrong (shouldn't happen)
					return;
				}
				
				Ride r = (Ride) ridesTableModel.getValueAt(selectedRow, 3);
				boolean removed = facade.removeRide(r);
				rideNotRemovedErrorLabel.setVisible(false);
				if(!removed) rideNotRemovedErrorLabel.setVisible(true);
				updateRides();
				removeRideButton.setEnabled(false);

			}
		});
		removeRideButton.setBounds(395, 296, 134, 37);
		contentPane.add(removeRideButton);
		
		// Selected ride reservations label
		selectedRideReservationsLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.SelectedRideReservationsLabel"));
		selectedRideReservationsLabel.setForeground(Color.BLACK);
		selectedRideReservationsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		selectedRideReservationsLabel.setBounds(77, 371, 850, 32);
		contentPane.add(selectedRideReservationsLabel);
		
		// scrollPane container for reservations table
		reservationsScrollPane = new JScrollPane();
		reservationsScrollPane.setBounds(77, 409, 850, 183);
		contentPane.add(reservationsScrollPane);
		
		// reservations table
		reservationsTable = new JTable();
		reservationsScrollPane.setViewportView(reservationsTable);
		reservationsTableModel = new DefaultTableModel(null, columnNamesReservationsTable) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		reservationsTable.setModel(reservationsTableModel);
		// its model
		reservationsTableModel.setDataVector(null, columnNamesReservationsTable);
		reservationsTableModel.setColumnCount(6);

		reservationsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
		reservationsTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		reservationsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		reservationsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		reservationsTable.getColumnModel().getColumn(4).setPreferredWidth(30);
		
		reservationsTable.getColumnModel().removeColumn(reservationsTable.getColumnModel().getColumn(5)); // not shown in JTable

	}
	
	
	/**
	 * This method updates the RidesTableModel with the Rides associated to the Driver
	 */
	public void updateRides() {
		
		// We'll first add the pending rides and then the ended ones so that they are ordered
		
		ridesTableModel.setRowCount(0); // Wipe the current table content
		
		List<Ride> posteriorRideList=facade.getPosteriorRidesOfDriver(driver);		
		for (Ride r : posteriorRideList)
		{
			Vector<Object> row = new Vector<Object>();
			row.add(r.getStringDate());
			row.add(r.getFrom());
			row.add(r.getTo());
			row.add(r); // TODO: Add state (pending, finished)
			ridesTableModel.addRow(row);		
		}
		
		List<Ride> endedRideList = facade.getEndedRidesOfDriver(driver);
		for (Ride r : endedRideList)
		{
			Vector<Object> row = new Vector<Object>();
			row.add(r.getStringDate());
			row.add(r.getFrom());
			row.add(r.getTo());
			row.add(r); // TODO: Add state (pending, finished)
			ridesTableModel.addRow(row);
		}

		// If there are no rides, enable error label
		if (posteriorRideList.isEmpty() && endedRideList.isEmpty()) noRidesErrorLabel.setVisible(true);
		
		// TODO: replace this with state check
		this.rowsToPaint = new ArrayList<Integer>();
		// We'll note the row numbers that need to be painted
		for (int i = posteriorRideList.size(); i < posteriorRideList.size() + endedRideList.size(); i++)
			rowsToPaint.add(i);
	}
		
	/**
	 * Update current ride's reservation table info
	 */
	public void updateReservations() {
		List<ReservationRequest> rrList=facade.getReservationsOfRide(selectedRide, "accepted");
		// We are also interested in displaying paid rides
		rrList.addAll(facade.getReservationsOfRide(selectedRide, "paid"));
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
	
}
