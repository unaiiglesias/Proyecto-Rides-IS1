package domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Review {
	@Id
	@GeneratedValue
	private Integer id;
	private Date date;
	private Integer points;
	private String message;
	@ManyToOne
	private Ride ride;
	@ManyToOne
	private Rider rider;
	@ManyToOne 
	private Driver driver;
	
	public Review(Integer points, String message, Ride ride, Rider rider, Driver driver) {
		this.points = points;
		this.message = message;
		this.ride = ride;
		this.date = new Date();
		this.rider = rider;
		this.driver = driver;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public String getStringDate() {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return(formater.format(this.date));
	}
	
}
