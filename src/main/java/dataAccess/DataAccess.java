package dataAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.ImageIcon;
import configuration.ConfigXML;
import configuration.UtilDate;
import domain.*;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.UserAlreadyExistException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;

	private String[] validStatesPrim = {"accepted", "rejected", "pending", "paid"};
	private List<String> validStates = new ArrayList<String>(Arrays.asList(validStatesPrim));

	
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
	    
		   // TODO
		   Date past = UtilDate.newDate(year, month, year - 1),
				future = UtilDate.newDate(year, month, year + 1);
		   
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
			
			Ride ride1 = driver1.addRide("Donostia", "Bilbo", future, 3, 73);
			Ride ride2 = driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year,month,6), 4, 82);
			Ride ride3 = driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 4, 24);
			Ride ride4 = driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year,month,24), 4, 33);
			Ride ride5 = driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year,month,15), 3, 31);
			Ride ride6 = driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year,month,25), 2, 75);
			Ride ride7 = driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year,month,6), 2, 57);
			Ride ride8 = driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(2019,01,14), 3, 30);
			Ride ride9 = driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(2020,10,14), 4, 30);
			Ride ride10 = driver3.addRide("Santander", "Donostia", UtilDate.newDate(2010,4,14), 3, 30);
			Ride ride11 = driver3.addRide("Donostia", "Santander", UtilDate.newDate(year,month,25), 4, 30);
			Ride ride11b = driver3.addRide("Donostia", "Santander", future, 4, 30);
			Ride ride12 = driver3.addRide("Santander", "Donostia", UtilDate.newDate(2012,4,14), 3, 30);
			Ride ride13 = driver3.addRide("Santander", "Donostia", UtilDate.newDate(2013,4,14), 3, 30);
			Ride ride14 = driver3.addRide("Santander", "Donostia", UtilDate.newDate(2014,4,14), 3, 30);

			db.persist(driver1);
			db.persist(driver2);
			db.persist(driver3);
			db.persist(rider1);
			db.persist(rider2);
			db.persist(rider3);

			db.getTransaction().commit();
			
			// Create some example Requests
			ReservationRequest reservation1 = new ReservationRequest(rider1, ride1, 2);
			addReservationRequest(reservation1);
			ReservationRequest reservation2 = new ReservationRequest(rider2, ride1, 1);
			addReservationRequest(reservation2);
			ReservationRequest reservation3 = new ReservationRequest(rider1, ride8, 1);
			addReservationRequest(reservation3);
			modifyReservationRequest(reservation3, "paid");
			ReservationRequest reservation4 = new ReservationRequest(rider3, ride1, 16);
			addReservationRequest(reservation4);
			ReservationRequest reservation5 = new ReservationRequest(rider1, ride9, 1);
			addReservationRequest(reservation5);
			modifyReservationRequest(reservation5, "paid");
			ReservationRequest reservation6 = new ReservationRequest(rider1, ride10, 1);
			addReservationRequest(reservation6);
			modifyReservationRequest(reservation6, "paid");
			ReservationRequest reservation7 = new ReservationRequest(rider1, ride11, 1);
			addReservationRequest(reservation7);
			modifyReservationRequest(reservation7, "paid");
			ReservationRequest reservation8 = new ReservationRequest(rider1, ride12, 1);
			addReservationRequest(reservation8);
			modifyReservationRequest(reservation8, "paid");
			ReservationRequest reservation9 = new ReservationRequest(rider1, ride13, 1);
			addReservationRequest(reservation9);
			modifyReservationRequest(reservation9, "paid");
			// This needs to be done out of the transaction because each of the method calls creates its own transaction

			// Iteration 2: Add riders to this ride so that they can review
			ReservationRequest reservation10 = new ReservationRequest(rider2, ride10, 1);
			addReservationRequest(reservation10);
			modifyReservationRequest(reservation10, "paid");
			ReservationRequest reservation11 = new ReservationRequest(rider3, ride10, 1);
			addReservationRequest(reservation11);
			modifyReservationRequest(reservation11, "paid");
			// Add some example reviews
			Review review1 = new Review(5, "Me ha gustado", ride10, rider1, ride10.getDriver());
			review1.addDriverAnswer("Me parece que tu reseña no representa el sentir del resto de viajeros que he recibido a lo largo de los "
					+ "ya 5 años en esta plataforma. \n \n \n \n Un saliudo!");
			addReview(review1);
			Review review2 = new Review(1, "A mi no", ride10, rider2, ride10.getDriver());
			review2.addDriverAnswer("Prueba 2");
			addReview(review2);
			Review review3 = new Review(3, "No esta mal", ride10, rider3, ride10.getDriver());
			addReview(review3);
			
			
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
	
	public boolean addReservationRequest(ReservationRequest rr) {
		Ride ride = db.find(Ride.class, rr.getRide().getRideNumber());
		Rider rider = db.find(Rider.class, rr.getRider().getEmail());
		// Check if there is already a reservation request made by the rider
		List<ReservationRequest> l = getReservationsOfRide(ride, null);
		for(ReservationRequest reservation : l)
			if(reservation.getRider().equals(rider)) return false;
		db.getTransaction().begin();
		if(ride != null & rider != null) {
			ride.addReservationRequest(rr);
			rider.addReservationRequest(rr);
		}
		db.getTransaction().commit();
		return true;
	}
	
	public void addReview(Review review) {
		Ride ride = db.find(Ride.class, review.getRide());
		Rider rider = db.find(Rider.class, review.getRider());
		Driver driver = db.find(Driver.class, review.getDriver());
		db.getTransaction().begin();
		ride.addReview(review);
		rider.addMadeReview(review);
		driver.addReview(review);
		db.getTransaction().commit();
	}
	
	public List<Review> getDriverReviews(Driver driver){
		Driver dr = db.find(Driver.class, driver.getEmail());
		TypedQuery<Review> query = db.createQuery("SELECT rev FROM Review rev WHERE rev.driver.email = ?1 ORDER BY rev.popularity, rev.date", Review.class);
		query.setParameter(1, dr.getEmail());
		List<Review> l = query.getResultList();
		return l;
	}
	
	public List<Review> getReviewsOfDriver(Driver driver){
		Driver dr = db.find(Driver.class, driver.getEmail());
		TypedQuery<Review> query = db.createQuery("SELECT rev FROM Review rev WHERE rev.driver.email = ?1  ORDER BY rev.popularity DESC, rev.date", Review.class);
		query.setParameter(1, dr.getEmail());
		List<Review> l = query.getResultList();
		return l;
	}
	
	public List<Review> getReviewsOfRide(Ride ride){
		Ride r = db.find(Ride.class, ride);
		return r.getReviews();
	}

	/*
	 * status: null (all reservations), pending, accepted
	 */
	public List<ReservationRequest> getReservationsOfRide(Ride ride, String status) {
		Ride r = db.find(Ride.class, ride.getRideNumber());
		TypedQuery<ReservationRequest> query;
		if (status == null)
		{
			query = db.createQuery("SELECT r FROM ReservationRequest r WHERE r.ride.rideNumber = ?1", ReservationRequest.class);
		}
		else
		{
			query = db.createQuery("SELECT r FROM ReservationRequest r WHERE r.ride.rideNumber = ?1 AND r.reservationState = ?2", ReservationRequest.class);
			query.setParameter(2, status);
		}
		
		query.setParameter(1, r.getRideNumber());
		List<ReservationRequest> l = query.getResultList();
		return l;
	}
	
	public boolean removeReservation(ReservationRequest reservation) {
		ReservationRequest rr = db.find(ReservationRequest.class, reservation.getId());
		if(rr == null) return false;
		db.getTransaction().begin();
		rr.getRide().removeReservationRequest(rr);
		rr.getRider().removeReservationRequest(rr);
		db.remove(rr);
		db.getTransaction().commit();
		return true;
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
	
	public List<Ride> getRidesOfDriver(Driver driver, Date date, int previousRides) {
		db.getTransaction().begin();
		Driver d = db.find(Driver.class, driver.getEmail());
		TypedQuery<Ride> query;
		if(previousRides == 1)
			query = db.createQuery("SELECT r FROM Ride r WHERE r.driver.email= ?1 AND r.date <= ?2", Ride.class);
		else
			query = db.createQuery("SELECT r FROM Ride r WHERE r.driver.email= ?1 AND r.date > ?2", Ride.class);
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
	 * Modifies given reservation request's state to the one given. 
	 * Accepted case is only modified if enough seats are left
	 * Paid case is only modified if reservationRequest was accepted
	 *  
	 * @param rr ReservationRequest to modify
	 * @param newStatus 
	 * @return true if the change was performed, false otherwise
	 */
	public boolean modifyReservationRequest(ReservationRequest rr, String newState)
	{
		Ride ride = db.find(Ride.class, rr.getRide().getRideNumber());
		
		if (!validStates.contains(newState))
		{
			// If newState is invalid, do nothing
			System.out.println("WARNING: modifyReservationRequest with invalid state " + newState);
			return false;
		}
		
		ReservationRequest reservation = db.find(ReservationRequest.class, rr.getId());
		
		if (newState.equals("accepted") && ride.getRemainingPlaces() < rr.getNumSeats()) return false;
		else if (newState.equals("paid") && !reservation.getReservationState().equals("accepted")) return false;
		
		db.getTransaction().begin();

		reservation.setReservationState(newState);
		/*
		 * Once a reservation is paid, automatically a chat will be created between the Rider and Driver of that ride
		 */
		if(newState.equals("paid")) {
			System.out.println("Entered here!");
			Driver dr = db.find(Driver.class, rr.getRide().getDriver());
			Rider r = db.find(Rider.class, rr.getRider());
			// If there is no chat between current rider and driver, create a new one.
			if(findChat(r, dr) == null) {
				Chat chat = new Chat(r, dr);
				db.persist(chat);
			}
		}
		
		db.getTransaction().commit();
		
		// Given object also need to be modified
		rr.setReservationState(newState);
		
		return true;
	}
	
	public List<ReservationRequest> getReservationRequestsOfRider(Rider rider){
		Rider r = db.find(Rider.class, rider.getEmail());
		TypedQuery<ReservationRequest> query = db.createQuery("SELECT rr FROM ReservationRequest rr WHERE rr.rider.email= ?1", ReservationRequest.class);
		query.setParameter(1, r.getEmail());
		List<ReservationRequest> l = query.getResultList();
		return l;
		
	}
	
	/**
	 * {@inheritDoc}
	 * @param rider 
	 * @param today
	 * @param onlyGetPast true, false, null (all)
	 * @param state pending, accepted, rejected, paid, null (all) ; if state is invalid, it will be considered null
	 */
	public List<ReservationRequest> getReservationRequestsOfRider(Rider rider, Date today, Boolean onlyGetPast, String state){
		Rider r = db.find(Rider.class, rider.getEmail());
		TypedQuery<ReservationRequest> query;
		
		// For code simplicity purposes, we'll use an auxiliary variable to construct the query
		String q = "SELECT rr FROM ReservationRequest rr WHERE rr.rider.email = ?1 "; 
		if (onlyGetPast == null)
			;
		else if (onlyGetPast)
			q = q + "AND rr.ride.date <= ?2 ";
		else if (!onlyGetPast)
			q = q + "AND rr.ride.date > ?2 ";
		
		
		if (state == null || !validStates.contains(state))
			;
		else
			q = q + "AND rr.reservationState = '" + state + "' ";
		
		query = db.createQuery(q, ReservationRequest.class);
		
		query.setParameter(1, r.getEmail());
		if (onlyGetPast != null)
			query.setParameter(2, today);
		List<ReservationRequest> l = query.getResultList();
		return l;
	}
	
	/**
	 * Get a list of all pending reservation requests of a driver. Requests made to expired rides (past rides)
	 * will NOT be returned.
	 * Results will be sorted by date
	 * 
	 * @return
	 */
	public List<ReservationRequest> getPendingReservationRequestsOfDriver(Driver driver, Date today)
	{
		Driver r = db.find(Driver.class, driver.getEmail());
		TypedQuery<ReservationRequest> query;
		
		query = db.createQuery("SELECT rr FROM ReservationRequest rr WHERE rr.ride.driver.email = ?1 AND rr.ride.date > ?2 AND rr.reservationState = 'pending'", ReservationRequest.class);
		
		query.setParameter(1, r.getEmail());
		query.setParameter(2, today);
		List<ReservationRequest> l = query.getResultList();
		return l;
	}
	
	/**
	 * Get the reservation requests that a driver has received. (requests made to it's rides)
	 * Params can be used to filter what requests to retrieve
	 * Result will be sorted by date
	 * 
	 * @param driver
	 * @param onlyGetPast true (past/historic rides), false (future/pending rides), null (all rides)
	 * @param state pending, accepted, rejected, paid, null (all)
	 */
	public List<ReservationRequest> getReservationRequestsOfDriver(Driver driver, Date today, Boolean onlyGetPast, String state)
	{
		Driver dbDriver = db.find(Driver.class, driver.getEmail());
		TypedQuery<ReservationRequest> query;
		
		// For code simplicity purposes, we'll use an auxiliary variable to construct the query
		String q = "SELECT rr FROM ReservationRequest rr WHERE rr.ride.driver.email = ?1 ";

		if (onlyGetPast == null)
			;
		else if (onlyGetPast)
			q += "AND rr.ride.date <= ?2 ";
		else if (!onlyGetPast)
			q += "AND rr.ride.date > ?2 ";
		
		if (state == null || !validStates.contains(state))
			;
		else
			q += "AND rr.reservationState = '" + state + "'";
		
		query = db.createQuery(q, ReservationRequest.class);
		
		query.setParameter(1, dbDriver.getEmail());
		if (onlyGetPast != null)
			query.setParameter(2, today);
		
		List<ReservationRequest> l = query.getResultList();
		return l;
	}
	
	/**
	 * Increase r's balance by amount
	 * 
	 * @param amount
	 * @param r
	 */
	public void depositMoney (double amount, Rider r)
	{
		Rider dbR = db.find(Rider.class, r.getEmail());
		db.getTransaction().begin();
		dbR.getPaid(amount);
		db.getTransaction().commit();
	}
	
	/**
	 * Attempt to pay a reservation request as rider. Will return true if operation succeeded, false otherwise.
	 * 
	 * @param rr
	 * @param rider
	 * @return true if payment succeeded (rider has enough money), false otherwise
	 */
	public boolean payReservationRequest (ReservationRequest rr, Rider rider)
	{
		Rider dbRider = db.find(Rider.class, rider.getEmail());
		ReservationRequest dbRR = db.find(ReservationRequest.class, rr.getId());
		Ride dbRide = db.find(Ride.class, dbRR.getRide().getRideNumber()); 
		Driver dbDriver = db.find(Driver.class, dbRide.getDriver().getEmail()); 
		
		double totalPrice = dbRide.getPrice() * dbRR.getNumSeats();
				
		System.out.println("Total cost: " + totalPrice);
		System.out.println("Balance: " + rider.getBalance());
		System.out.println("DB balance: " + dbRider.getBalance());
		
		if (dbRider.getBalance() < totalPrice)
			return false; // Not enough funds, cant pay
		
		modifyReservationRequest(dbRR, "paid");
		db.getTransaction().begin();
		dbRider.pay(totalPrice);
		dbDriver.getPaid(totalPrice);
		db.getTransaction().commit();
		
		// Given objects also need to be modified, as they are not re-fetched from DB
		rider.pay(totalPrice);
		rr.setReservationState("paid");
		
		return true;
	}
	
	/**
	 * Adds a ReviewRating if it hasn't yet, else modifies the rating.
	 * @param rating The ReviewRating to add or modify
	 */
	public void addReviewRating(ReviewRating rating) {
		TypedQuery<ReviewRating> query = db.createQuery("SELECT rate FROM ReviewRating rate WHERE rate.review.id = ?1 AND rate.rider.email = ?2", ReviewRating.class);
		Review review = db.find(Review.class, rating.getReview().getId());
		Rider rider = db.find(Rider.class, rating.getRider().getEmail());
		
		query.setParameter(1, review.getId());
		query.setParameter(2, rider.getEmail());
		List<ReviewRating> l = query.getResultList();
		ReviewRating r;
		
		db.getTransaction().begin();
		// if the reviewRating exists modify it
		if(l.size() > 0) {
			r = l.get(0);
			r.setRating(rating.getRating());
		} else {
			review.addReviewRating(rating);
			rider.addReviewRating(rating);
		}
		db.getTransaction().commit();
	}
	
	public void setRiderProfilePic(Rider r, ImageIcon icon) {
		Rider rider = db.find(Rider.class, r);
		db.getTransaction().begin();
		rider.setProfilePicIcon(icon);
		db.getTransaction().commit();
		r.setProfilePicIcon(icon);
	}
	
	public void updateName(Rider r, String newName) {
		Rider rider = db.find(Rider.class, r);
		db.getTransaction().begin();
		rider.setName(newName);;
		db.getTransaction().commit();
		r.setName(newName);
	}
	
	public void updateSurname(Rider r, String newSurname) {
		Rider rider = db.find(Rider.class, r);
		db.getTransaction().begin();
		rider.setSurname(newSurname);;
		db.getTransaction().commit();
		r.setSurname(newSurname);	
	}
	
	public void updatePassword(Rider r, String newPassword) {
		Rider rider = db.find(Rider.class, r);
		db.getTransaction().begin();
		rider.setPassword(newPassword);
		db.getTransaction().commit();
		r.setPassword(newPassword);	
	}
	
	public void addDriverAnswer(Review review, String msg) {
		Review rev = db.find(Review.class, review);
		db.getTransaction().begin();
		rev.addDriverAnswer(msg);
		db.getTransaction().commit();
	}
	
	public List<Chat> getChatsOfUser(Rider rider) {
		Rider r = db.find(Rider.class, rider);
		TypedQuery<Chat> query = db.createQuery("SELECT c FROM Chat c WHERE c.rider.email = ?1 ORDER BY c.lastMessage DESC", Chat.class);
		query.setParameter(1, r.getEmail());
		List<Chat> l = query.getResultList();
		return l;
	}
	
	public List<Chat> getChatsOfUser(Driver driver) {
		Driver dr = db.find(Driver.class, driver);
		TypedQuery<Chat> query = db.createQuery("SELECT c FROM Chat c WHERE c.driver.email = ?1 ORDER BY c.lastMessage DESC", Chat.class);
		query.setParameter(1, dr.getEmail());
		List<Chat> l = query.getResultList();
		return l;
	}
	
	public Chat findChat(Chat chat) {
		Chat c = db.find(Chat.class, chat);
		return c;
	}
	
	public Chat findChat(Rider rider, Driver driver) {
		Rider r = db.find(Rider.class, rider);
		Driver dr = db.find(Driver.class, driver);
		if(r == null | dr == null) {
			return null;
		}
		TypedQuery<Chat> query = db.createQuery("SELECT c FROM Chat c WHERE c.driver.email = ?1 AND c.rider.email = ?2", Chat.class);
		query.setParameter(1, dr.getEmail());
		query.setParameter(2,  r.getEmail());
		List<Chat> l = query.getResultList();
		if(l.size() == 0) return null;
		else return l.get(0);
	}
	
	public void addMessageToChat(String msg, Rider author, Chat chat) {
		Chat c = db.find(Chat.class, chat);
		Rider msgAuthor = db.find(Rider.class, author);
		db.getTransaction().begin();
		Message message = new Message(msgAuthor, c, msg);
		c.addMessage(message);
		db.getTransaction().commit();
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
