package businessLogic;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.swing.ImageIcon;
import configuration.ConfigXML;
import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.*;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.IncorrectCredentialsException;
import exceptions.RideAlreadyExistException;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesNotExistException;
import util.EmailManager;
import util.ImageManagerUtil;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {
	DataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new DataAccess();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(DataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c = ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    /**
     * {@inheritDoc}
     */
    public void addRider(String email, String password, String name, String surname, int age) throws UserAlreadyExistException {
    	dbManager.open();
    	try {
    		Rider rider = new Rider(email, password, name, surname, age);
    		dbManager.userRegistration(rider);
    	} catch(Exception e) {
    		throw e;
    	} finally {
    		dbManager.close();	
    	}
    }
    
    /**
     * {@inheritDoc}
     */
	public void addDriver(String email, String password, String name, String surname, int age, String licensePlate, String vehicleModel) throws UserAlreadyExistException {
    	dbManager.open();
    	try {
    		Rider driver = new Driver(email, password, name, surname, age, licensePlate, vehicleModel);
    		dbManager.userRegistration(driver);
    	} catch(Exception e) {
    		throw e;
    	} finally {
    		dbManager.close();	
    	}
	}
	
	public void addReview(Integer points, String message, Ride ride, Rider rider, Driver driver) {
		dbManager.open();
		Review review = new Review(points, message, ride, rider, driver);
		dbManager.addReview(review);
		dbManager.close();
	}
	
	public String getDriverStars(Driver driver) {
		String stars = "";
		dbManager.open();
		List<Review> l = dbManager.getDriverReviews(driver);
		dbManager.close();
		Integer sumPoints = 0;
		for(Review rev : l) sumPoints += rev.getPoints();
		int meanPoints = (int) Math.round((double) sumPoints / l.size());
		for(int i=0; i<meanPoints; i++) stars = stars + "★";
		for(int i=0; i<5-meanPoints; i++) stars = stars + "☆";
		return stars;
	}
	
	public List<Review> getReviewsOfDriver(Driver driver){
		dbManager.open();
		List<Review> l = dbManager.getReviewsOfDriver(driver);
		dbManager.close();
		return l;
	}
	
	public List<Review> getReviewsOfRide(Ride ride){
		dbManager.open();
		List<Review> l = dbManager.getReviewsOfRide(ride);
		dbManager.close();
		return l;
	}
	
    /**
     * {@inheritDoc}
     */
	public Rider login(String email, String password) throws IncorrectCredentialsException, UserDoesNotExistException {
    	dbManager.open();
    	
    	Rider rider = dbManager.getRider(email);
    	if(rider == null) throw new UserDoesNotExistException();
    	if(!rider.getPassword().equals(password)) throw new IncorrectCredentialsException();
 
    	dbManager.close();
    	return rider;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Rider getRider(String email) {
		dbManager.open();
		Rider rider = dbManager.getRider(email);
		dbManager.close();
		return rider;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean makeReservationRequest(Ride ride, Rider rider, Integer numSeats) {
		dbManager.open();
		ReservationRequest rr = new ReservationRequest(rider, ride, numSeats); 
		Boolean done = dbManager.addReservationRequest(rr);
		dbManager.close();
		return done;
	}
	
	
    /**
     * {@inheritDoc}
     * status: null, pending, accepted
     */
	public List<ReservationRequest> getReservationsOfRide(Ride ride, String status) {
		List<ReservationRequest> l = null;
		dbManager.open();
		l = dbManager.getReservationsOfRide(ride, status);
		Collections.sort(l, new Comparator<ReservationRequest>() {
			public int compare(ReservationRequest r1, ReservationRequest r2) {
				return r1.getDate().compareTo(r2.getDate());
			}
		});
		dbManager.close();
		return l;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeReservation(ReservationRequest reservation) {
		dbManager.open();
		Boolean rm = dbManager.removeReservation(reservation);
		dbManager.close();
		return rm;
	}
	
    /**
     * {@inheritDoc}
     */
	public List<Ride> getRidesOfDriver(Driver driver) {
		dbManager.open();
		List<Ride> l = dbManager.getRidesOfDriver(driver);
		Collections.sort(l, new Comparator<Ride>() {
			public int compare(Ride r1, Ride r2) {
				return r1.getDate().compareTo(r2.getDate());
			}
		});
		dbManager.close();
		return l;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod
	public List<Ride> getPosteriorRidesOfDriver(Driver driver) {
		dbManager.open();
		Date date = getCurrentDate();
		List<Ride> l = dbManager.getRidesOfDriver(driver, date, 0);
		Collections.sort(l, new Comparator<Ride>() {
			public int compare(Ride r1, Ride r2) {
				return r1.getDate().compareTo(r2.getDate());
			}
		});
		dbManager.close();
		return l;
	}
	
	public List<Ride> getEndedRidesOfDriver(Driver driver){
		dbManager.open();
		Date date = getCurrentDate();
		List<Ride> l = dbManager.getRidesOfDriver(driver, date, 1);
		Collections.sort(l, new Comparator<Ride>() {
			public int compare(Ride r1, Ride r2) {
				return r1.getDate().compareTo(r2.getDate());
			}
		});
		dbManager.close();
		return l;
	}
	
    /**
     * {@inheritDoc}
     */
    @WebMethod public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	@WebMethod public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		dbManager.close();
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */
	@WebMethod 
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@WebMethod 
	public boolean removeRide(Ride ride){
		dbManager.open();
		Boolean deleted = dbManager.removeRide(ride);
		dbManager.close();
		return deleted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean modifyReservationRequestState(ReservationRequest rr, String newStatus) {
		dbManager.open();
		boolean accepted = dbManager.modifyReservationRequest(rr, newStatus);
		dbManager.close();
		if (accepted && newStatus.equals("accepted"))
			EmailManager.sendRequestAcceptedEmailAsync(rr);
		return accepted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<ReservationRequest> getRidesDoneByRider(Rider rider) {
		dbManager.open();
		List<ReservationRequest> l = dbManager.getReservationRequestsOfRider(rider, getCurrentDate(), true, "paid");
		dbManager.close();
		Collections.sort(l, new Comparator<ReservationRequest>() {
			public int compare(ReservationRequest r1, ReservationRequest r2) {
				return r1.getRide().getDate().compareTo(r2.getRide().getDate());
			}
		});
		return l;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<ReservationRequest> getReservationRequestsOfRider(Rider rider, Boolean onlyGetPast, String state) {
		dbManager.open();
		List<ReservationRequest> l = dbManager.getReservationRequestsOfRider(rider, getCurrentDate(), onlyGetPast, state);
		dbManager.close();
		Collections.sort(l, new Comparator<ReservationRequest>() {
			public int compare(ReservationRequest r1, ReservationRequest r2) {
				return r1.getRide().getDate().compareTo(r2.getRide().getDate());
			}
		});
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ReservationRequest> getReservationRequestsOfDriver(Driver driver, Boolean onlyGetPast, String state)
	{
		dbManager.open();
		List<ReservationRequest> l = dbManager.getReservationRequestsOfDriver(driver, getCurrentDate(), onlyGetPast, state);
		dbManager.close();
		Collections.sort(l, new Comparator<ReservationRequest>() {
			public int compare(ReservationRequest r1, ReservationRequest r2) {
				return r1.getRide().getDate().compareTo(r2.getRide().getDate());
			}
		});
		return l;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void depositMoney (double amount, Rider r)
	{	
		dbManager.open();
		dbManager.depositMoney(amount, r);
		dbManager.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean payReservationRequest (ReservationRequest rr, Rider rider)
	{
		// If the rr isn't accepted, it wont even allow payment
		if (!rr.getReservationState().equals("accepted"))
		{
			System.out.println("WARNING: payReservationRequest called on not accepted rr");
			System.out.println(rr.getReservationState());
			return false;
		}
			
		dbManager.open();
		boolean res = dbManager.payReservationRequest(rr, rider);
		dbManager.close();
		
		if (res)
			System.out.println("PAYMENT SUCCEEDED");
		else
		{
			System.out.println("PAYMENT FAILED");
		}
		
		return res;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addReviewRating(Review review, Rider rider, Integer rating) {
		ReviewRating r = new ReviewRating(review, rider, rating);
		dbManager.open();
		dbManager.addReviewRating(r);
		dbManager.close();
	}
	
	
	private Date getCurrentDate() {
		// TODO: Is this correct?
		Calendar today = Calendar.getInstance();
	   	int month=today.get(Calendar.MONTH);
	   	int year=today.get(Calendar.YEAR);
	   	if (month==12) { month=1; year+=1;}  
		return UtilDate.newDate(year, month, today.get(Calendar.DAY_OF_MONTH));
	}
	
	public String setRiderProfilePic(Rider r, ImageIcon icon) {
		String verification = ImageManagerUtil.verifyIcon(icon);
		
		if (verification != null)
			return verification;
			
		dbManager.open();
		dbManager.setRiderProfilePic(r, icon);
		dbManager.close();
		
		return null;
	}
	
	
	public void updateName(Rider r, String newName) {
		dbManager.open();
		dbManager.updateName(r, newName);
		dbManager.close();
	}
	
	public void updateSurname(Rider r, String newSurname) {
		dbManager.open();
		dbManager.updateSurname(r, newSurname);
		dbManager.close();
	}
	
	public void updatePassword(Rider r, String newPassword) {
		dbManager.open();
		dbManager.updatePassword(r, newPassword);
		dbManager.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addDriverAnswer(Review review, String msg) {
		dbManager.open();
		dbManager.addDriverAnswer(review, msg);
		dbManager.close();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Chat> getChatsOfUser(Rider rider, boolean asRider){
		dbManager.open();
		List<Chat> l = dbManager.getChatsOfUser(rider, asRider);
		dbManager.close();
		return l;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Chat findChat(Chat chat) {
		dbManager.open();
		Chat c = dbManager.findChat(chat);
		dbManager.close();
		return c;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addMessageToChat(String msg, Rider author,  Chat chat) {
		dbManager.open();
		dbManager.addMessageToChat(msg, author, chat);
		dbManager.close();
	}
	
	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    @WebMethod	
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}

}

