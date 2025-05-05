package domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Message {
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	private Chat chat;
	private Rider author;
	private Date date;
	private String message;
	
	public Message(Rider author, Chat chat, String message) {
		this.author = author;
		this.chat = chat;
		this.message = message;
		this.date = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public Rider getAuthor() {
		return author;
	}

	public void setAuthor(Rider author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getStringDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return(formatter.format(this.date));
	}
	
	public String getStringDateHourMin() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		return(formatter.format(this.date));
	}
	
}
