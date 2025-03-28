package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;

import domain.Review;
import domain.Ride;
import domain.Rider;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;


public class FindRidesGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	public Rider rider;
	private JComboBox<String> jComboBoxOrigin = new JComboBox<String>();
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();

	private JComboBox<String> jComboBoxDestination = new JComboBox<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();

	private JLabel jLabelOrigin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.LeavingFrom"));
	private JLabel jLabelDestination = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.GoingTo"));
	private final JLabel jLabelEventDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideDate"));
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides")); 

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	// Code for JCalendar
	private JCalendar jCalendar1 = new JCalendar();
	private Calendar calendarAnt = null;
	private Calendar calendarAct = null;
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private List<Date> datesWithRidesCurrentMonth = new Vector<Date>();

	private JTable tableRides= new JTable();

	private DefaultTableModel tableModelRides;


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Driver"),
			"Reviews",
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.NPlaces"), 
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Price")
	};
	private final JButton jButtonRequestRide = new JButton(ResourceBundle.getBundle("Etiquetas").getString("MainGUI.RequestRides"));
	private final JLabel jLabelAlreadyReserved;

	private JSpinner spinner;
	private final JLabel jLabelNumSeats = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ShowRequestsGUI.NumSeats")); 

	@SuppressWarnings("serial")
	public FindRidesGUI(Rider rider)
	{
		/*
		 * Auxiliar clases
		 */
		BLFacade facade = MainGUI.getBusinessLogic();
	    class ButtonRenderer extends JButton implements TableCellRenderer {
	        public ButtonRenderer() {
	            setOpaque(true);  // Hacer opaco el botón
	        }

	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            // El texto del botón será el valor de la celda de la tabla
	            setText(value.toString());
	            return this;
	        }
	    }

	    class ButtonEditor extends DefaultCellEditor {
	        private JButton button;

	        public ButtonEditor(JCheckBox checkBox) {
	            super(checkBox);
	            button = new JButton();
	            button.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    // Mostrar mensaje con el texto del botón
	    				int selectedRow = tableRides.getSelectedRow();
	                    ShowReviewsDialog a = new ShowReviewsDialog(facade.getReviewsOfDriver(((Ride) tableModelRides.getValueAt(selectedRow, 4)).getDriver()));
	                    a.setVisible(true);
	                    fireEditingStopped(); 
	                }
	            });
	        }

	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	            button.setText(value.toString());
	            return button;
	        }

	        @Override
	        public Object getCellEditorValue() {
	            return button.getText();
	        }
	    }
	    
		this.rider = rider;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 546));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.FindRides"));
	

		jLabelAlreadyReserved = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.AlreadyReserved"));
		jLabelAlreadyReserved.setBounds(166, 480, 346, 14);
		jLabelAlreadyReserved.setForeground(new Color(255,0,0));
		jLabelAlreadyReserved.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelAlreadyReserved.setVisible(false);
		getContentPane().add(jLabelAlreadyReserved);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
		spinner.setEnabled(false);
		spinner.setBounds(300, 410, 42, 20);
		getContentPane().add(spinner);
		
		jLabelNumSeats.setBounds(166, 410, 124, 20);
		jLabelNumSeats.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(jLabelNumSeats);

		
		jLabelEventDate.setBounds(new Rectangle(457, 6, 140, 25));
		jLabelEvents.setBounds(166, 221, 259, 16);

		this.getContentPane().add(jLabelEventDate, null);
		this.getContentPane().add(jLabelEvents);

		jButtonClose.setBounds(new Rectangle(366, 441, 130, 30));

		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton2_actionPerformed(e);
			}
		});

		List<String> origins=facade.getDepartCities();
		
		for(String location:origins) originLocations.addElement(location);
		
		jLabelOrigin.setBounds(new Rectangle(6, 56, 92, 20));
		jLabelDestination.setBounds(6, 81, 61, 16);
		getContentPane().add(jLabelOrigin);

		getContentPane().add(jLabelDestination);

		jComboBoxOrigin.setModel(originLocations);
		jComboBoxOrigin.setBounds(new Rectangle(103, 50, 172, 20));
		

		List<String> aCities=facade.getDestinationCities((String)jComboBoxOrigin.getSelectedItem());
		for(String aciti:aCities) {
			destinationCities.addElement(aciti);
		}
		
		jComboBoxOrigin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				destinationCities.removeAllElements();
				BLFacade facade = MainGUI.getBusinessLogic();

				List<String> aCities=facade.getDestinationCities((String)jComboBoxOrigin.getSelectedItem());
				for(String aciti:aCities) {
					destinationCities.addElement(aciti);
				}
				tableModelRides.getDataVector().removeAllElements();
				tableModelRides.fireTableDataChanged();

				
			}
		});

		jComboBoxDestination.setModel(destinationCities);
		jComboBoxDestination.setBounds(new Rectangle(103, 80, 172, 20));
		jComboBoxDestination.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,	new Color(210,228,238));

				BLFacade facade = MainGUI.getBusinessLogic();

				datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
				paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);

			}
		});

		this.getContentPane().add(jButtonClose, null);
		this.getContentPane().add(jComboBoxOrigin, null);

		this.getContentPane().add(jComboBoxDestination, null);


		jCalendar1.setBounds(new Rectangle(300, 50, 225, 150));


		// Code for JCalendar
		jCalendar1.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent propertychangeevent)
			{

				if (propertychangeevent.getPropertyName().equals("locale"))
				{
					jCalendar1.setLocale((Locale) propertychangeevent.getNewValue());
				}
				else if (propertychangeevent.getPropertyName().equals("calendar"))
				{
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();
					

					
					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar1.getLocale());

					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);

					if (monthAct!=monthAnt) {
						if (monthAct==monthAnt+2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, devolvería 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							calendarAct.set(Calendar.MONTH, monthAnt+1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}						

						jCalendar1.setCalendar(calendarAct);

					}
					
					try {
						tableModelRides.setDataVector(null, columnNamesRides);
						tableModelRides.setColumnCount(5); // another column added to allocate ride objects

						BLFacade facade = MainGUI.getBusinessLogic();
						List<domain.Ride> rides=facade.getRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),UtilDate.trim(jCalendar1.getDate()));

						if (rides.isEmpty() ) jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.NoRides")+ ": "+dateformat1.format(calendarAct.getTime()));
						else jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Rides")+ ": "+dateformat1.format(calendarAct.getTime()));
						for (domain.Ride ride:rides){
							Vector<Object> row = new Vector<Object>();
							row.add(ride.getDriver().getName());
							row.add(facade.getDriverStars(ride.getDriver()));
							row.add(ride.getnPlaces());
							row.add(ride.getPrice());
							row.add(ride); // ev object added in order to obtain it with tableModelEvents.getValueAt(i,3)
							tableModelRides.addRow(row);		
						}
						datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
						paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);


					} catch (Exception e1) {

						e1.printStackTrace();
					}
					tableRides.getColumnModel().getColumn(0).setPreferredWidth(70);
					tableRides.getColumnModel().getColumn(1).setPreferredWidth(50);
					tableRides.getColumnModel().getColumn(2).setPreferredWidth(30);
					tableRides.getColumnModel().getColumn(3).setPreferredWidth(30);
					tableRides.getColumnModel().removeColumn(tableRides.getColumnModel().getColumn(4)); // not shown in JTable
				    
					tableRides.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
					tableRides.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor(new JCheckBox()));

				}
			} 
			
		});

		this.getContentPane().add(jCalendar1, null);

		scrollPaneEvents.setBounds(new Rectangle(166, 249, 346, 150));

		scrollPaneEvents.setViewportView(tableRides);
		/**
		 * We create the model for the JTable, not allowing the user to modify the content
		 */
		tableModelRides = new DefaultTableModel(null, columnNamesRides) {
			public boolean isCellEditable(int row, int column) {
				if(column == 1) return true;
				return false;
			}
		};

		tableRides.setModel(tableModelRides);

		tableModelRides.setDataVector(null, columnNamesRides);
		tableModelRides.setColumnCount(5); // another column added to allocate ride objects

		tableRides.getColumnModel().getColumn(0).setPreferredWidth(70);
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(50);
		tableRides.getColumnModel().getColumn(2).setPreferredWidth(30);
		tableRides.getColumnModel().getColumn(3).setPreferredWidth(30);
		tableRides.getColumnModel().removeColumn(tableRides.getColumnModel().getColumn(4)); // not shown in JTable

		this.getContentPane().add(scrollPaneEvents, null);
		datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
		paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);
		
		// Add a JButton to the driver's reviews column so that by clicking the user will see all the reviews of that driver

		
		/*
		 In order to perform a ride request, we will need to enable the following JButton only if a ride is selected from (JTabel) tableRides
		 */
		tableRides.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent l) {
				jButtonRequestRide.setEnabled(true);
				jLabelAlreadyReserved.setVisible(false);
				int selectedRow = tableRides.getSelectedRow();
				if(selectedRow != -1) {
					Ride ride = (Ride) tableModelRides.getValueAt(selectedRow, 4);
					spinner.setModel(new SpinnerNumberModel(1, 1, ride.getnPlaces(), 1));
					spinner.setEnabled(true);
				} else {
					// Tell the user something went wrong
				}

			}
		});
		/*
		 In case the user wants to request a ride:
		 	1. Get the Ride
		 	2. Create a new ReservationRequest -> facade.reserve(...)
		 	3. Let the user know the reservation has gone right (pending yet)
		 */
		jButtonRequestRide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = tableRides.getSelectedRow();
				if(selectedRow != -1) {
					Ride ride = (Ride) tableModelRides.getValueAt(selectedRow, 4);
					Boolean done = facade.makeReservationRequest(ride, rider, ((Double) spinner.getValue()).intValue());
					if(!done) jLabelAlreadyReserved.setVisible(true);
				} else {
					// Tell the user something went wrong
				}
			}
		});
		
		jButtonRequestRide.setBounds(new Rectangle(387, 420, 130, 30));
		jButtonRequestRide.setBounds(175, 441, 130, 30);
		jButtonRequestRide.setEnabled(false);
		// If the user is not a Rider or Driver, won't be able to request a ride
		if(rider == null) {
			jButtonRequestRide.setVisible(false);
			spinner.setVisible(false);
			jLabelNumSeats.setVisible(false);
			jButtonClose.setBounds(new Rectangle(278, 420, 130, 30));
		}
		
		getContentPane().add(jButtonRequestRide);

		
	}
	public static void paintDaysWithEvents(JCalendar jCalendar,List<Date> datesWithEventsCurrentMonth, Color color) {
		//		// For each day with events in current month, the background color for that day is changed to cyan.


		Calendar calendar = jCalendar.getCalendar();

		int month = calendar.get(Calendar.MONTH);
		int today=calendar.get(Calendar.DAY_OF_MONTH);
		int year=calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK);

		if (Locale.getDefault().equals(new Locale("es")))
			offset += 4;
		else
			offset += 5;


		for (Date d:datesWithEventsCurrentMonth){

			calendar.setTime(d);


			// Obtain the component of the day in the panel of the DayChooser of the
			// JCalendar.
			// The component is located after the decorator buttons of "Sun", "Mon",... or
			// "Lun", "Mar"...,
			// the empty days before day 1 of month, and all the days previous to each day.
			// That number of components is calculated with "offset" and is different in
			// English and Spanish
			//			    		  Component o=(Component) jCalendar.getDayChooser().getDayPanel().getComponent(i+offset);; 
			Component o = (Component) jCalendar.getDayChooser().getDayPanel()
					.getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(color);
		}

		calendar.set(Calendar.DAY_OF_MONTH, today);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);


	}
	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	


}
