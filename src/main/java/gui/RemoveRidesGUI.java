package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import businessLogic.BLFacade;
import domain.Driver;
import domain.ReservationRequest;
import domain.Ride;
import domain.Rider;

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

	/**
	 * Create the frame.
	 */
	public RemoveRidesGUI(Driver d) {
		
		this.driver = d;
		facade = MainGUI.getBusinessLogic();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(604, 370));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		
		ridesScrollPane = new JScrollPane();
		ridesScrollPane.setBounds(56, 27, 452, 236);
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
		
		ridesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				removeRideJButton.setEnabled(true);
			}
		});
		
		ridesJLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.NoRides"));
		ridesJLabel.setForeground(new Color(255, 0, 0));
		ridesJLabel.setBounds(55, 11, 55, 14);
		ridesJLabel.setVisible(false);
		contentPane.add(ridesJLabel);
		
		removeRideJButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("RemoveRidesGUI.RemoveRide"));
		removeRideJButton.setEnabled(false);
		removeRideJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Show a new window informing the user about this action's consequences
				//TODO
				
				// Remove the ride and update the Ride's table
				int selectedRow = ridesTable.getSelectedRow();
				if(selectedRow != -1) {					
					Ride r = (Ride) ridesTableModel.getValueAt(selectedRow, 3);
					facade.removeRide(r);
					updateRides();
					removeRideJButton.setEnabled(false);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		removeRideJButton.setBounds(226, 274, 134, 37);
		contentPane.add(removeRideJButton);
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

}
