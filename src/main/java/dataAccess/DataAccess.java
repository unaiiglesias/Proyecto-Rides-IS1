package dataAccess;

import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Driver;
import domain.ReservationRequest;
import domain.Ride;
import domain.Rider;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyExistException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;


	ConfigXML c=ConfigXML.getInstance();

     public DataAccess()  {
    	 
    	// Initialize DB
		if (c.isDatabaseInitialized()) {
			String fileName=c.getDbFilename();

			File fileToDelete= new File(fileName);
			if(fileToDelete.delete()){
				File fileToDeleteTemp= new File(fileName+"$");
				fileToDeleteTemp.delete();

				  System.out.println("File deleted");
				} else {
				  System.out.println("Operation failed");
				}
		}
		
		open();
		
		// Fill the initialized DB with default data
		if  (c.isDatabaseInitialized())initializeDB();
		
		System.out.println("DataAccess created => isDatabaseLocal: "+c.isDatabaseLocal()+" isDatabaseInitialized: "+c.isDatabaseInitialized());

		close();

	}
     
    public DataAccess(EntityManager db) {
    	this.db=db;
    }

	
	
	/**
	 * This is the data access method that initializes the database with some events and questions.
	 * AKA: Fills the DB with some example data
	 * If the data already exists (some error occurred) prints exception on console
	 * This method is invoked by the business logic (constructor of BLFacadeImplementation) when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){

		System.out.println("Initializing DB...");
		db.getTransaction().begin();

		try {

		   Calendar today = Calendar.getInstance();
		   
		   int month=today.get(Calendar.MONTH);
		   int year=today.get(Calendar.YEAR);
		   if (month==12) { month=1; year+=1;}  
	    
		   // Create some Riders
		   Rider rider1 = new Rider("example1@rider.com", "rider", "Jon", "Pelaio", 15);
		   Rider rider2 = new Rider("example2@rider.com", "rider", "Matias", "Gutierrez", 30);
		   Rider rider3 = new Rider("example3@rider.com", "rider", "Javier", "Jimenez", 45);
		   
		   // Create some Drivers
			Driver driver1=new Driver("example1@driver.com", "driver", "Aitor", "Fernandez", 34, "00000X", "Peugeot 360");
			Driver driver2=new Driver("example2@driver.com", "driver", "Ane","Gaztañaga", 27,  "00001X", "Alfa Romeo");
			Driver driver3=new Driver("example3@driver.com", "driver", "Testillo", "Satisfactorio", 18, "00002X", "Tesla Model S");
			
			//Create some example rides
			// From To Date nPlaces Price
			
			Ride ride1 = driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,30), 1, 73);
			Ride ride2 = driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 82);
			Ride ride3 = driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 24);
			Ride ride4 = driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,24), 4, 33);
			Ride ride5 = driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 31);
			Ride ride6 = driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 75);
			Ride ride7 = driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 57);
			Ride ride8 = driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,14), 1, 30);

			db.persist(driver1);
			db.persist(driver2);
			db.persist(driver3);
			db.persist(rider1);
			db.persist(rider2);
			db.persist(rider3);

			db.getTransaction().commit();
			
			// Create some example Requests
			ReservationRequest reservation1 = new ReservationRequest(rider1, ride1);
			addReservationRequest(reservation1);
			ReservationRequest reservation2 = new ReservationRequest(rider2, ride1);
			addReservationRequest(reservation2);
			ReservationRequest reservation3 = new ReservationRequest(rider1, ride8);
			addReservationRequest(reservation3);
			ReservationRequest reservation4 = new ReservationRequest(rider3, ride1);
			addReservationRequest(reservation4);
			// This needs to be done out of the transaction because each of the method calls creates its own transaction
			
			System.out.println("SUCCESS: Db initialized with example data");
		}
		catch (Exception e){
			// If an error occurred, make sure changes are undone
			db.getTransaction().rollback();
			System.out.println("ERROR: Initializing the DB with example data failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * This method stores a new user (Rider or Driver) instance
	 * @throws UserAlreadyExistException 
	 */
	public void userRegistration(Rider rider) throws UserAlreadyExistException {
		/*
		  Note: due to Driver`s extending Riders, this method applies for both users: Rider and Driver
		 */
		// First, check if Rider already exists
		if(db.find(Rider.class, rider.getEmail())==null) {
			db.getTransaction().begin();
			db.persist(rider);
			db.getTransaction().commit();
		} else {
			throw new UserAlreadyExistException("Error: email is already being used");
		}
	}
	
	public Rider getRider(String email) {
		return db.find(Rider.class, email);
	}
	
	public void addReservationRequest(ReservationRequest rr) {
		db.getTransaction().begin();
		Ride ride = db.find(Ride.class, rr.getRide().getRideNumber());
		Rider rider = db.find(Rider.class, rr.getRider().getEmail());
		if(ride != null & rider != null) {
			ride.addReservationRequest(rr);
			rider.addReservationRequest(rr);
		}
		db.getTransaction().commit();
	}

	public List<ReservationRequest> getReservationsOfRide(Ride ride) {
		db.getTransaction().begin();
		Ride r = db.find(Ride.class, ride.getRideNumber());
		TypedQuery<ReservationRequest> query = db.createQuery("SELECT r FROM ReservationRequest r WHERE r.ride.rideNumber = ?1", ReservationRequest.class);
		query.setParameter(1, r.getRideNumber());
		List<ReservationRequest> l = query.getResultList();
		db.getTransaction().commit();
		return l;
	}
	
	public List<Ride> getRidesOfDriver(Driver driver){
		db.getTransaction().begin();
		Driver d = db.find(Driver.class, driver.getEmail());
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.driver.email= ?1", Ride.class);
		query.setParameter(1, d.getEmail());
		List<Ride> l = query.getResultList();
		db.getTransaction().commit();
		return l;
	}
	
	public List<Ride> getRidesOfDriver(Driver driver, Date date) {
		db.getTransaction().begin();
		Driver d = db.find(Driver.class, driver.getEmail());
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.driver.email= ?1 AND r.date > ?2", Ride.class);
		query.setParameter(1, d.getEmail());
		query.setParameter(2, date);
		List<Ride> l = query.getResultList();
		db.getTransaction().commit();
		return l;
	}
	
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	public List<String> getDepartCities(){
			TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
			List<String> cities = query.getResultList();
			return cities;
		
	}
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from){
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to",String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList(); 
		return arrivingCities;
		
	}
	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws  RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> DataAccess: createRide=> from= "+from+" to= "+to+" driver="+driverEmail+" date "+date);
		try {
			if(new Date().compareTo(date)>0) {
				throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();
			
			Driver driver = db.find(Driver.class, driverEmail);
			if (driver.doesRideExists(from, to, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			//next instruction can be obviated
			db.persist(driver); 
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}
		
		
	}
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= "+from+" to= "+to+" date "+date);

		List<Ride> res = new ArrayList<>();	
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3",Ride.class);   
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
	 	 for (Ride ride:rides){
		   res.add(ride);
		  }
	 	return res;
	}
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();	
		
		Date firstDayMonthDate= UtilDate.firstDayMonth(date);
		Date lastDayMonthDate= UtilDate.lastDayMonth(date);
				
		
		TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",Date.class);   
		
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
	 	 for (Date d:dates){
		   res.add(d);
		  }
	 	return res;
	}
	
	/**
	 * This method removes a Ride if there're no reservation requests associated to it
	 * @param ride The Ride to be removed
	 * @return true if the ride has been successfully deleted else false
	 */
	public boolean removeRide(Ride ride) {
		Ride r = db.find(Ride.class, ride.getRideNumber());
		if(r.numReservationRequest() != 0) return false;
		db.getTransaction().begin();
		// First we'll remove the ride from Driver
		Driver driver = db.find(Driver.class, r.getDriver().getEmail());
		driver.removeRide(r.getFrom(), r.getTo(), r.getDate());
		// Now we delete the ride from DB
		db.remove(r);
		db.getTransaction().commit();
		return true;
	}
	
	/**
	 * This method accepts a ReservationRequest if it's corresponding Ride has any seat left.
	 * @param rr The ReservationRequest to accept
	 * @return true if accepted else false
	 */
	public boolean acceptReservationRequest(ReservationRequest rr) {;
		Ride ride = db.find(Ride.class, rr.getRide().getRideNumber());
		// if no places available return false
		if(ride.getRemainingPlaces() == 0) return false;
		db.getTransaction().begin();
		ReservationRequest reservation = db.find(ReservationRequest.class, rr.getId());
		reservation.setReservationState("accepted");
		db.getTransaction().commit();
		return true;
	}
	
	public void open(){
		
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			  db = emf.createEntityManager();
    	   }
		System.out.println("DataAccess opened => isDatabaseLocal: "+c.isDatabaseLocal());

		
	}

	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}
	
}
