package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Ride;
import domain.Rider;
import domain.Driver;
import domain.ReservationRequest;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.IncorrectCredentialsException;
import exceptions.RideAlreadyExistException;
import exceptions.UserAlreadyExistException;
import exceptions.UserDoesNotExistException;

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
		ConfigXML c=ConfigXML.getInstance();
		
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
	public void makeReservationRequest(Ride ride, Rider rider) {
		dbManager.open();
		ReservationRequest rr = new ReservationRequest(rider, ride); 
		dbManager.addReservationRequest(rr);
		dbManager.close();
	}
	
	
    /**
     * {@inheritDoc}
     */
	public List<ReservationRequest> getReservationsOfRide(Ride ride) {
		List<ReservationRequest> l = null;
		dbManager.open();
		l = dbManager.getReservationsOfRide(ride);
		dbManager.close();
		return l;
	}
	
    /**
     * {@inheritDoc}
     */
	public List<Ride> getRidesOfDriver(Driver driver) {
		dbManager.open();
		List<Ride> l = dbManager.getRidesOfDriver(driver);
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

