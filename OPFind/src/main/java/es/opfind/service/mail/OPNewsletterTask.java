package es.opfind.service.mail;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import es.opfind.service.NewsletterMgr;

@Service
public class OPNewsletterTask implements Serializable {

	@Resource
	private NewsletterMgr newsletterMgr;
	
	public void sendOneMail(){
		
		//TODO: Leer esto de un archivo de configuracion
		
		Properties props = new Properties();

		// Nombre del host de correo, es smtp.gmail.com
		props.setProperty("mail.smtp.host", "smtp.gmail.com");

		// TLS si está disponible
		props.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port","587");

		// Nombre del usuario
		props.setProperty("mail.smtp.user", "opfind.es@gmail.com");

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");
		
		
		Session session = Session.getDefaultInstance(props);
		
		//TODO: Quitar esta linea de depuracion
		session.setDebug(true);
		
		
		MimeMessage message = new MimeMessage(session);
		
		// Quien envia el correo
		try {
			message.setFrom(new InternetAddress("opfind.es@gmail.com"));
			
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("joan.navarrete.sanchez@gmail.com"));
			
			message.setSubject("Hola");
			message.setText("Mensajito con Java Mail de los buenos porque si");
			
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		Transport t;
		try {
			t = session.getTransport("smtp");
			t.connect("opfind.es@gmail.com","2PnowG2i");
			t.sendMessage(message,message.getAllRecipients());
			t.close();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		

		
		
	}
	
}
