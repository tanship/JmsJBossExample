package com.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Accessing Remotly-JMS Queue
 */

public class RemoteJMSClient {

	private static final String PASSWORD = "password";

	private static final String APPICATION_USER_NAME = "bandu";

	private static final String JMS_QUEUE_JNDI_NAME = "jms/queue/test";

	public static void main(String[] args) throws MalformedURLException,
			IOException, NamingException, JMSException {

		final Properties env = new Properties();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");
		env.put(Context.PROVIDER_URL, "remote://localhost:4447");
		env.put(Context.SECURITY_PRINCIPAL, APPICATION_USER_NAME);
		env.put(Context.SECURITY_CREDENTIALS, PASSWORD);
		Context context = new InitialContext(env);

		ConnectionFactory cf = (ConnectionFactory) context
				.lookup("jms/RemoteConnectionFactory");
		Queue queue = (Queue) context.lookup(JMS_QUEUE_JNDI_NAME);
		Connection connection = cf.createConnection(APPICATION_USER_NAME,
				PASSWORD);// NOTE: role should be guest when
							// creating application user
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		QueueBrowser browser = session.createBrowser(queue);

		Enumeration messageEnum = browser.getEnumeration();
		while (messageEnum.hasMoreElements()) {
			TextMessage message = (TextMessage) messageEnum.nextElement();
			System.out.println("Queue Message---------------->: "
					+ message.getText());
			System.out.println("JMSCorrelation ID------------>: "
					+ message.getJMSCorrelationID());
			System.out.println("Message ID ------------------>: "
					+ message.getJMSMessageID());

		}

	}

}
