package in.webtuts.jms.topic;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MsgSubscriberOne {

	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;
	Destination destination;
	MessageConsumer messageConsumer;

	public MsgSubscriberOne() throws NamingException, JMSException {
		
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
		this.messageConsumer = session.createConsumer(destination);
	}

	private String receiveMessage() throws JMSException {
		TextMessage textMessage = (TextMessage) messageConsumer.receive();
		return textMessage.getText();
	}

	private void closeConnection() throws JMSException {
		connection.close();
	}

	/**
	 * @param args
	 * @throws JMSException
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException, JMSException {
		MsgSubscriberOne msgSubscriber = new MsgSubscriberOne();
		String msg = msgSubscriber.receiveMessage();
		System.out.println("SubscriberOne");
		System.out.println("-------------");
		System.out.println(msg);
		msgSubscriber.closeConnection();
	}

}
