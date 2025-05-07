package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
// Rider and Driver instances will be saved in the same database table.
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Entity
public class Driver extends Rider implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String licensePlate;
	private String vehicleModel;
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new Vector<Ride>();
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Review> reviews;
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private List<Chat> chatsAsDriver;

	public Driver() {
		super();
		this.rides = new ArrayList<Ride>();
	}

	public Driver(String email, String password, String name, String surname, int age, String licensePlate, String vehicleModel) {
		super(email, password, name, surname, age);
		this.licensePlate = licensePlate;
		this.vehicleModel = vehicleModel;
		this.rides = new ArrayList<Ride>();
		this.reviews = new ArrayList<Review>();
		this.chatsAsDriver = new ArrayList<Chat>();
	}
	
	
	public String toString(){
		return this.getEmail()+";"+this.getName()+rides;
	}
	
	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Ride addRide(String from, String to, Date date, int nPlaces, float price)  {
        Ride ride=new Ride(from,to,date,nPlaces,price, this);
        rides.add(ride);
        return ride;
	}
	
	public void addReview(Review review) {
		this.reviews.add(review);
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String from, String to, Date date)  {	
		for (Ride r:rides)
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
			 return true;
		
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (this.getEmail() != other.getEmail())
			return false;
		return true;
	}

	public Ride removeRide(String from, String to, Date date) {
		boolean found=false;
		int index=0;
		Ride r=null;
		while (!found && index<=rides.size()) {
			r=rides.get(++index);
			if ( (java.util.Objects.equals(r.getFrom(),from)) && (java.util.Objects.equals(r.getTo(),to)) && (java.util.Objects.equals(r.getDate(),date)) )
			found=true;
		}
			
		if (found) {
			rides.remove(index);
			return r;
		} else return null;
	}
	
}
