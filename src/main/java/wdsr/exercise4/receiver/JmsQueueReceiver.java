package wdsr.exercise4.receiver;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Complete this class so that it consumes messages from the given queue
 * and invokes the registered callback when an alert is received.
 * 
 * Assume the ActiveMQ broker is running on tcp://localhost:62616
 */
public class JmsQueueReceiver {
	private static final Logger log = LoggerFactory.getLogger(JmsQueueReceiver.class);
	private static final String BROKER_URI = "tcp://localhost:62616";

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;
	private Destination destination;

	/**
	 * Creates this object
	 * 
	 * @param queueName
	 *            Name of the queue to consume messages from.
	 */
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

	/**
	 * Registers the provided callback. The callback will be invoked when a
	 * price or volume alert is consumed from the queue.
	 * 
	 * @param alertService
	 *            Callback to be registered.

	/**
	 * Deregisters all consumers and closes the connection to JMS broker.
	 */
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
