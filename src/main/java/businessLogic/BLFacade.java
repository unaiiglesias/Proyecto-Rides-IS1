package businessLogic;

import java.util.Date;
import java.util.List;

//import domain.Booking;
import domain.Ride;
import domain.Rider;
import domain.Driver;
import domain.ReservationRequest;
import domain.Review;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesNotExistException;
import exceptions.IncorrectCredentialsException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.swing.ImageIcon;
 
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {
	  
	/**
	 * This method returns all the cities where rides depart 
	 * @return collection of cities
	 */
	@WebMethod public List<String> getDepartCities();
	
	/**
	 * This method returns all the arrival destinations, from all rides that depart from a given city  
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	@WebMethod public List<String> getDestinationCities(String from);


	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @param nPlaces available seats
	 * @param driver to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today 
 	 * @throws RideAlreadyExistException if the same ride already exists for the driver
	 */
   @WebMethod
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideMustBeLaterThanTodayException, RideAlreadyExistException;
	
	
	/**
	 * This method retrieves the rides from two locations on a given date 
	 * 
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride
	 * @param date the date of the ride 
	 * @return collection of rides
	 */
	@WebMethod public List<Ride> getRides(String from, String to, Date date);
	
	/**
	 * This method retrieves from the database the dates a month for which there are events
	 * @param from the origin location of a ride
	 * @param to the destination location of a ride 
	 * @param date of the month for which days with rides want to be retrieved 
	 * @return collection of rides
	 */
	@WebMethod public List<Date> getThisMonthDatesWithRides(String from, String to, Date date);
	
	/**
	 * This method calls the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();
	
	/**
	 * This method does the registration of a new Rider in the database
	 * @throws UserAlreadyExistException if the Rider already exist
	 */
	public void addRider(String email, String password, String name, String surname, int age) throws UserAlreadyExistException;
	
	/**
	 * This method does the registration of a new Driver in the database
	 * @throws UserAlreadyExistException if the Driver already exist
	 */
	public void addDriver(String email, String password, String name, String surname, int age, String licensePlate, String vehicleModel) throws UserAlreadyExistException;
	
	public void addReview(Integer points, String message, Ride ride, Rider rider, Driver driver);
	
	public List<Review> getReviewsOfDriver(Driver driver);
	
	public String getDriverStars(Driver driver);
	
	/**
	 * This method does the login of a Rider or Driver
	 * @return instance of Rider (or Driver) 
	 * @throws IncorrectCredentialsException if the password is incorrect
	 * @throws UserDoesNotExistException if given email doesn't match any Rider (or Driver) one's 
	 */
	public Rider login(String email, String password) throws IncorrectCredentialsException, UserDoesNotExistException;
	
	/**
	 * This method finds a Rider in the database
	 * Even though the returned object is of class Rider, it might also be of class Driver
	 */
	public Rider getRider(String email);
	
	/**
	 * This method makes a reservation request of a Ride
	 * @return false if the rider had already done a reservation request to that ride else true
	 */
	public boolean makeReservationRequest(Ride ride, Rider rider, Integer numSeats);
	
	/**
	 * This method finds all the ReservationRequests associated to a ride
	 * @param ride
	 * @param status pending, accepted, rejected, null
	 * @return a sorted by date list ReservationRequests
	 */
	public List<ReservationRequest> getReservationsOfRide(Ride ride, String status);
	
	/**
	 * This method removes a reservation request
	 * @param reservation The ReservationRequest to be removed
	 * @return true if removed successfully, else false
	 */
	public boolean removeReservation(ReservationRequest reservation);
	/**
	 * This method finds all the Rides create by a given driver
	 * @param driver The driver whose rides will be found
	 * @return a sorted by date list of Rides
	 */
	public List<Ride> getRidesOfDriver(Driver driver);
	
	/**
	 * This method removes a Ride from DB if it has no reservation requests associated
	 * @param ride Ride to be removed
	 * @return true if Ride successfully deleted else false
	 */
	public boolean removeRide(Ride ride);
	
	/**
	 * This method finds all the Rides created by a given driver that are posterior to the current date
	 * @param driver The driver whose rides will be found
	 * @return a sorted by date list of Rides
	 */
	public List<Ride> getPosteriorRidesOfDriver(Driver driver);
	
	public List<Ride> getEndedRidesOfDriver(Driver driver);
	
	/**
	 * This method modifies a given ReservationRequest's state.
	 * @param rr The reservation request to accept
	 * @param newState pending, accepted, rejected, paid
	 */
	public boolean modifyReservationRequestState(ReservationRequest rr, String newState);
	
	/**
	 * This method finds all the Rides done by a Rider
	 * @param rider The Rider whose already done Rides (accepted Reservation Requests) will find
	 * @return a sorted by Ride's date list of ReservationRequest
	 */
	public List<ReservationRequest> getRidesDoneByRider(Rider rider);
	
	/**
	 * Get the reservation requests that a rider has made (rides it has requested)
	 * Params can be used to filter what requests to retrieve
	 * Result will be sorted by date
	 * 
	 * @param rider 
	 * @param onlyGetPast true (past/historic rides), false (future/pending rides), null (all rides)
	 * @param state pending, accepted, rejected, paid, null (all)
	 */
	public List<ReservationRequest> getReservationRequestsOfRider(Rider rider, Boolean onlyGetPast, String state);
	
	/**
	 * Get the reservation requests that a driver has received. (requests made to it's rides)
	 * Params can be used to filter what requests to retrieve
	 * Result will be sorted by date
	 * 
	 * @param driver
	 * @param onlyGetPast true (past/historic rides), false (future/pending rides), null (all rides)
	 * @param state pending, accepted, rejected, paid, null (all)
	 */
	public List<ReservationRequest> getReservationRequestsOfDriver(Driver driver, Boolean onlyGetPast, String state);
	
	/**
	 * Increase r's balance by amount
	 * 
	 * @param amount
	 * @param r
	 */
	public void depositMoney (double amount, Rider r);
	
	/**
	 * Attemp to pay a reservation request as rider. Will return true if operation succeeded, false otherwise.
	 * Only accepted reservation requests may be paid.
	 * 
	 * @param rr
	 * @param rider
	 * @return true if payment succeeded (enough balance and accepted), false otherwise
	 */
	public boolean payReservationRequest (ReservationRequest rr, Rider rider);
	
	/**
	 * Adds a rating to a review.
	 * @param rider The rider that does the review's rating
	 * @param review The review being rated
	 * @param rating The rating (-1 or 1) given to the review
	 */
	public void addReviewRating(Review review, Rider rider, Integer rating);
	
	/**
	 * Changes a given riders profile picture to the one given
	 * 
	 * @param r
	 * @param icon
	 */
	public void setRiderProfilePic(Rider r, ImageIcon icon);
	
	/**
	 * Updates attributes of Rider instance on DB
	 */
	public void updateName(Rider r, String newName);
	public void updateSurname(Rider r, String newSurname);
	public void updatePassword(Rider r, String newPassword);
}
