package com.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hornetq.api.core.management.ObjectNameBuilder;
import org.hornetq.api.jms.management.JMSQueueControl;

public class RemoteJMXClient {

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException,
			IOException {

		// NOTE : standalone-full.xml configuration
		// Enabled - <jmx-management-enabled>true</jmx-management-enabled>
		String urlString = "service:jmx:remoting-jmx://localhost:9999";
		JMXConnector connector = JMXConnectorFactory.connect(new JMXServiceURL(
				urlString), new HashMap<String, String>());

		try {

			MBeanServerConnection mbsc = connector.getMBeanServerConnection();
			ObjectName on = ObjectNameBuilder.DEFAULT
					.getJMSQueueObjectName("testQueue");

			JMSQueueControl jmsqserverControl = (JMSQueueControl) MBeanServerInvocationHandler
					.newProxyInstance(mbsc, on, JMSQueueControl.class, false);

			Map<String, Object>[] messages = jmsqserverControl
					.listMessages(null);
			System.out.println("Added Message Count------>"
					+ jmsqserverControl.getMessagesAdded());
			System.out.println("Consumed Message Count--->"
					+ jmsqserverControl.getConsumerCount());

			for (Map<String, Object> map : messages) {

				System.out.println("Message Id-------->"
						+ map.get("JMSMessageID"));
				System.out.println("Message Address--->" + map.get("address"));

			}
		} catch (Throwable e) {

			System.out.println("Exception : " + e);

		} finally {

			connector.close();
			System.out.println("The end");

		}

	}

}