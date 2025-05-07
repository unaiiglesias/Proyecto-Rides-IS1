package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Chat {
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	private Rider rider;
	@ManyToOne
	private Driver driver;
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Message> messages;
	private Date initDate;
	private Date lastMessage;
	
	public Chat(Rider rider, Driver driver) {
		this.rider = rider;
		this.driver = driver;
		this.messages = new ArrayList<Message>();
		this.initDate = new Date();
		this.lastMessage = new Date(); // In order to be able to sort even if no message has been send
	}
	
	public void addMessage(Message message) {
		this.messages.add(message);
		if(message.getDate().after(lastMessage)) {
			lastMessage = message.getDate();
		}
	}
	
	public Rider getOtherUser(Rider user) {
		if(user.getEmail().equals(rider.getEmail())) {
			return (Rider) driver;
		} else {
			return rider;
		}
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

	public List<Message> getMessages() {
		Collections.sort(this.messages, new Comparator<Message>() {
			public int compare(Message msg1, Message msg2) {
				return msg1.getDate().compareTo(msg2.getDate());
			}
		});
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getLastModification() {
		return lastMessage;
	}

	public void setLastModification(Date lastModification) {
		this.lastMessage = lastModification;
	}
	
}
