package wdsr.exercise4.sender;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.Order;

public class JmsSender {
	private static final Logger log = LoggerFactory.getLogger(JmsSender.class);
	private static final String BROKER_URI = "tcp://localhost:62616";

	private final String queueName;
	private final String topicName;

	private ActiveMQConnectionFactory activeMQConnectionFactory;
	private Connection connection;
	private Session session;

	public JmsSender(final String queueName, final String topicName) {
		this.queueName = queueName;
		this.topicName = topicName;
		try {
			activeMQConnectionFactory = new ActiveMQConnectionFactory(BROKER_URI);
			connection = activeMQConnectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates an Order message with the given parameters and sends
	 * it as an ObjectMessage to the queue.
	 * 
	 * @param orderId
	 *            ID of the product
	 * @param product
	 *            Name of the product
	 * @param price
	 *            Price of the product
	 */
	public void sendOrderToQueue(final int orderId, final String product, final BigDecimal price) {
		Order order = new Order(orderId, product, price);
		try {
			Destination destination = session.createQueue(queueName);
			MessageProducer messageProducer = session.createProducer(destination);
			ObjectMessage objectMessage = session.createObjectMessage(order);
			objectMessage.setJMSType("Order");
			objectMessage.setStringProperty("WDSR-System", "OrderProcessor");
			messageProducer.send(objectMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sends the given String to the queue as a TextMessage.
	 * 
	 * @param text
	 *            String to be sent
	 */
	public void sendTextToQueue(String text) {
		try {
			Destination destination = session.createQueue(queueName);
			MessageProducer messageProducer = session.createProducer(destination);
			TextMessage textMessage = session.createTextMessage(text);
			messageProducer.send(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends key-value pairs from the given map to the topic as a MapMessage.
	 * 
	 * @param map
	 *            Map of key-value pairs to be sent.
	 * @throws JMSException
	 */
	public void sendMapToTopic(Map<String, String> map) {
		try {
			Destination destination = session.createTopic(topicName);
			MessageProducer messageProducer = session.createProducer(destination);
			MapMessage mapMessage = session.createMapMessage();

			for (Entry<String, String> entrySet : map.entrySet())
				mapMessage.setString(entrySet.getKey(), entrySet.getValue());

			messageProducer.send(mapMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
