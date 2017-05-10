package wdsr.exercise4.receiver;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsQueueReceiver {
	private static final Logger log = LoggerFactory.getLogger(JmsQueueReceiver.class);
	private static final String BROKER_URI = "tcp://localhost:61616";

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;

	public JmsQueueReceiver(final String queueName) {
		connectionFactory = new ActiveMQConnectionFactory(BROKER_URI);
		connectionFactory.setTrustAllPackages(true);
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void recive() {
		try {
			MessageConsumer messageConsumer = session.createConsumer(destination);
			TextMessage mssg = (TextMessage) messageConsumer.receive(100);
			while (mssg != null) {
				log.info("recived " + mssg.getText());
				mssg = (TextMessage) messageConsumer.receive(100);
			}
		} catch (JMSException e) {
			log.error("failed");
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
