package domain;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import domain.*;

@Entity
public class ReviewRating {
	@Id
	@GeneratedValue
	private Integer id;
	private Date date;
	@ManyToOne
	private Review review;
	@ManyToOne
	private Rider rider;
	private Integer rating; // -1 or 1

	public ReviewRating(Review review, Rider rider, Integer rating) {
		this.review = review;
		this.rider = rider;
		this.rating = rating;
		this.date = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
