package com.marshall.jms.topic;

import java.io.*;
import java.util.Properties;

import javax.jms.*;
import javax.naming.*;

public class TPublisher
	{
	public static void main(String[] args)
		{
		new TPublisher().publish();
		}

	public void publish()
		{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try
			{
			// Strings for JNDI names
			String factoryName = "jms/RemoteConnectionFactory";
			String topicName = "jms/topic/test";
			// Create an initial context.
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
			props.put(Context.PROVIDER_URL, "remote://localhost:4447");
			props.put(Context.SECURITY_PRINCIPAL, "testuser");
			props.put(Context.SECURITY_CREDENTIALS, "password");
			props.put("jboss.naming.client.ejb.context", true);
			
			
			InitialContext context = new InitialContext(props);
			
			TopicConnectionFactory factory = (TopicConnectionFactory) context.lookup(factoryName);
			Topic topic = (Topic) context.lookup(topicName);
			context.close();
			// Create JMS objects
			TopicConnection connection = factory.createTopicConnection("testuser","password");
			TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			TopicPublisher publisher = session.createPublisher(topic);
			// Send messages
			System.out.println("Enter message to send or 'quit' to quit");
			String messageText = "123";
			while (true)
				{
				messageText = reader.readLine();
				if ("quit".equalsIgnoreCase(messageText)) 
					{
					break;
					}
				TextMessage message = session.createTextMessage(messageText);
				publisher.publish(message);
				}
			// Exit
			System.out.println("Exiting...");
			reader.close();
			connection.close();
			System.out.println("Goodbye!");
			}
		catch (Exception e)
			{
			e.printStackTrace();
			System.exit(1);
			}
		}
	}