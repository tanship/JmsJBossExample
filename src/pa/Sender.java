package pa;

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

public class Sender {
	private ConnectionFactory cf;
	private Connection c;
	private Session s;
	private Destination d;
	private MessageProducer mp;

	public Sender() throws NamingException, JMSException {
		// InitialContext init = new InitialContext();

		Properties properties = new Properties();
		properties.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"org.jboss.naming.remote.client.InitialContextFactory");
		properties.setProperty(Context.PROVIDER_URL, "remote://localhost:4447");
		properties.put("jboss.naming.client.ejb.context", true);
		properties.put(Context.SECURITY_PRINCIPAL, "user");
		properties.put(Context.SECURITY_CREDENTIALS, "kolkata");
		InitialContext init = new InitialContext(properties);

		this.cf = (ConnectionFactory) init
				.lookup("jms/RemoteConnectionFactory");
		this.d = (Destination) init.lookup("jms/topic/test");
		this.c = (Connection) this.cf.createConnection("user", "kolkata");
		this.c.start();
		this.s = this.c.createSession(false, Session.AUTO_ACKNOWLEDGE);
		this.mp = this.s.createProducer(this.d);
	}

	private void send(String string) throws JMSException {
		TextMessage tm = this.s.createTextMessage(string);
		this.mp.send(tm);
	}

	private void close() throws JMSException {
		this.c.close();
	}

	/**
	 * @param args
	 * @throws JMSException
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException, JMSException {
		Sender s = new Sender();
		s.send("Ola ca estou eu!");
		s.close();
	}

}