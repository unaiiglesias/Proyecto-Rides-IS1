package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Rider {
	@XmlID
	@Id
	private String email;
	private String password;
	private String name;
	private String surname;
	private int age;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<ReservationRequest> reservations;
	
	public Rider() {
		
	}
	
	public void addReservationRequest(ReservationRequest rr) {
		this.reservations.add(rr);
	}
	
	public void removeReservationRequest(ReservationRequest rr) {
		this.reservations.remove(rr);
	}
	
	public Rider(String email, String password, String name, String surname, int age) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.reservations = new ArrayList<ReservationRequest>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public List<ReservationRequest> getReservations(){
		return this.reservations;
	}
	
	
	
}
