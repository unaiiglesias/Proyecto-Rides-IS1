package domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

@Entity
public class ReservationRequest {
	@Id
	@GeneratedValue // Automatically will assign a non-used ID
	private Integer id;
	private Integer numSeats;
	private String reservationState = "pending"; // "pending", "accepted" or "denied"
	private Date date;
	@ManyToOne
	private Rider rider;
	@ManyToOne
	private Ride ride;
		
	public ReservationRequest(Rider rider, Ride ride, Integer numSeats) {
		this.rider = rider;
		this.numSeats = numSeats;
		this.ride = ride;
		this.date = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReservationState() {
		return reservationState;
	}

	public void setReservationState(String reservationState) {
		this.reservationState = reservationState;
	}

	public Date getDate() {
		return date;
	}
	
	public String getStringDate() {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return(formater.format(this.date));
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}
	
	public Integer getNumSeats() {
		return this.numSeats;
	}
	
	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}
	
}
