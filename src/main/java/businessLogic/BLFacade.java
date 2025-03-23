package businessLogic;

import java.util.Date;
import java.util.List;

//import domain.Booking;
import domain.Ride;
import domain.Rider;
import domain.Driver;
import domain.ReservationRequest;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesNotExistException;
import exceptions.IncorrectCredentialsException;

import javax.jws.WebMethod;
import javax.jws.WebService;
 
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
	 */
	public void makeReservationRequest(Ride ride, Rider rider);
	
	/**
	 * This method finds all the ReservationRequests associated to a ride
	 * @return a sorted by date list ReservationRequests
	 */
	public List<ReservationRequest> getReservationsOfRide(Ride ride);
	
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
	
	
	/**
	 * This method accepts a given ReservationRequest.
	 * @param rr The reservation request to accept
	 */
	public boolean acceptReservationRequest(ReservationRequest rr);
}
