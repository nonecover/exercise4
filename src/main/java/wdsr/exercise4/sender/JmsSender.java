package wdsr.exercise4.sender;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


public class JmsSender {
	private static final String BROKER_URI = "tcp://localhost:61616";

	private final String queueName;

	private ActiveMQConnectionFactory activeMQConnectionFactory;
	private Connection connection;
	private Session session;

	public JmsSender(final String queueName) {
		this.queueName = queueName;
		try {
			activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URI);
			connection = activeMQConnectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendTextToQueue(String text, int deliveryMode) {
		try {
			Destination destination = session.createQueue(queueName);
			MessageProducer messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(deliveryMode);
			TextMessage textMessage = session.createTextMessage(text);
			messageProducer.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
