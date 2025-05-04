package domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class Review {
	@Id
	@GeneratedValue
	private Integer id;
	private Date date;
	private Integer points;
	private String message;
	private Integer popularity;
	private String driverAnswer;
	private Date driverAnswerDate;
	
	@ManyToOne
	private Ride ride;
	@ManyToOne
	private Rider rider;
	@ManyToOne 
	private Driver driver;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<ReviewRating> ratings;
	
	public Review(Integer points, String message, Ride ride, Rider rider, Driver driver) {
		this.points = points;
		this.message = message;
		this.ride = ride;
		this.date = new Date();
		this.rider = rider;
		this.driver = driver;
		this.ratings = new ArrayList<ReviewRating>();
		this.popularity = 0;
	}
	
	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	public void addReviewRating(ReviewRating rating) {
		this.ratings.add(rating);
		this.setPopularity(this.getPopularity() + rating.getRating());
	}
	
	public void addDriverAnswer(String msg) {
		this.driverAnswer = msg;
		this.driverAnswerDate = new Date();
	}
	
	public String getDriverAnswer() {
		return driverAnswer;
	}

	public void setDriverAnswer(String driverAnswer) {
		this.driverAnswer = driverAnswer;
	}

	public Date getDriverAnswerDate() {
		return driverAnswerDate;
	}

	public void setDriverAnswerDate(Date driverAnswerDate) {
		this.driverAnswerDate = driverAnswerDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<ReviewRating> getRatings() {
		return ratings;
	}

	public void setRatings(List<ReviewRating> ratings) {
		this.ratings = ratings;
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
	
	public String getStringDriverAnswerDate() {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return(formater.format(this.driverAnswerDate));
	}
	
}
