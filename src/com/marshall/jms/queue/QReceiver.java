package com.marshall.jms.queue;

import java.io.*;
import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QReceiver implements MessageListener
	{
	private boolean stop = false;

	public static void main(String[] args)
		{
		new QReceiver().receive();
		}

	public void receive()
		{
		try
			{
			// Strings for JNDI names
			String factoryName = "jms/RemoteConnectionFactory";
			String queueName =  "jms/queue/test";
			// Create an initial context.
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
			props.put(Context.PROVIDER_URL, "remote://localhost:4447");
			props.put(Context.SECURITY_PRINCIPAL, "testuser");
			props.put(Context.SECURITY_CREDENTIALS, "password");
			InitialContext context = new InitialContext(props);
			
			QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup(factoryName);
			Queue queue = (Queue) context.lookup(queueName);
			context.close();
			// Create JMS objects
			QueueConnection connection = factory.createQueueConnection("testuser","password");
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueReceiver receiver = session.createReceiver(queue);
			receiver.setMessageListener(this);
			connection.start();
			// Wait for stop
			while (!stop)
				{
				Thread.sleep(1000);
				}
			// Exit
			System.out.println("Exiting...");
			connection.close();
			System.out.println("Goodbye!");
			}
		catch (Exception e)
			{
			e.printStackTrace();
			System.exit(1);
			}
		}

	public void onMessage(Message message)
		{
		try
			{
			String msgText = ((TextMessage) message).getText();
			System.out.println(msgText);
			if ("stop".equals(msgText)) stop = true;
			}
		catch (JMSException e)
			{
			e.printStackTrace();
			stop = true;
			}
		}
	}