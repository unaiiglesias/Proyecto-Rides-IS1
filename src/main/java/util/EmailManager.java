package util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import domain.Driver;
import domain.ReservationRequest;
import domain.Ride;
import domain.Rider;


public class EmailManager {

	final static String sender = "correoridesis1@gmail.com", pass = "tnss rkci qeih mccb";
	// The email will be sent by our company's gmail account, for which we need the app password
	// (This should be extracted to an .env and UNDER NO CIRMUNSTANCE be compiled into the client app)
	// AKA: This util class is BussinessLogic only
	
	public static void sendRequestAcceptedEmail (ReservationRequest rr) {
		
		Rider rider = rr.getRider();
		Ride ride = rr.getRide();
		Driver driver = ride.getDriver();
		String receiver = rider.getEmail();
		
		String subject = "Su solicitud de viaje a " + ride.getTo() + " ha sido aceptada",  
				msg = "Estimad@ " + rr.getRider().getName() + ", ";
		
		msg = String.format(
				"Estimado %s,\n\n Su solicitud de viaje desde %s a %s con el/la conductor/a %s (%s) ha sido aceptada.\nLe recordamos que puede realizar el pago hasta un dia antes de la salida del viaje, que será el %s.\n\n¡Gracias por usar nmuestros servicios!", 
				rider.getName(), ride.getFrom(), ride.getTo(), driver.getName()  + " " + driver.getSurname() + driver.getEmail(), ride.getDate());
		
		// Configurar para gmail
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.enable", "false");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, pass);
			}
		});

		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(receiver));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
			message.setSubject("Mensaje de prueba");
			message.setText("Hola, esto es un mensaje de prueba");

			Transport.send(message);

			System.out.println("EMAIL sent successfully to " + receiver);
		}
		catch (Exception e) 
		{
			System.err.println("EMAIL failed... ");
			System.out.println("Error: "+e.getMessage());
		}

	}
	
	public static void main (String [] args) {
		;
	}
	
}
