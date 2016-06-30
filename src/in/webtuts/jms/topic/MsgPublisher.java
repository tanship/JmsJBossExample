package in.webtuts.jms.topic;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


//http://www.roytuts.com/jms-client-using-jboss-7-publishsubscribe-messaging/
public class MsgPublisher {

	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;
	Destination destination;
	MessageProducer messageProducer;

	public MsgPublisher() throws JMSException, NamingException {
		
		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");
		properties.setProperty(Context.PROVIDER_URL, "remote://localhost:4447");
		properties.put("jboss.naming.client.ejb.context", true);
		properties.put(Context.SECURITY_PRINCIPAL, "user");
		properties.put(Context.SECURITY_CREDENTIALS, "kolkata");
		InitialContext initialContext = new InitialContext(properties);
		
		
		this.connectionFactory = (ConnectionFactory) initialContext
				.lookup("jms/RemoteConnectionFactory");
		
		this.destination = (Destination) initialContext
				.lookup("jms/topic/test");
		
		this.connection = connectionFactory.createConnection("user", "kolkata");
		
		connection.start();
		this.session = connection
				.createSession(false, Session.AUTO_ACKNOWLEDGE);
		this.messageProducer = session.createProducer(destination);
	}

	private void publishMessage(String msg) throws JMSException {
		TextMessage textMessage = session.createTextMessage(msg);
		messageProducer.send(textMessage);
	}

	private void closeConnection() throws JMSException {
		connection.close();
	}

	/**
	 * @param args
	 * @throws NamingException
	 * @throws JMSException
	 */
	public static void main(String[] args) throws JMSException, NamingException {
		MsgPublisher msgPublisher = new MsgPublisher();
		msgPublisher.publishMessage("Publishing Message.");
		msgPublisher.closeConnection();
	}

}
