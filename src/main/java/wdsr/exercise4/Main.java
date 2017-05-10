package wdsr.exercise4;

import javax.jms.DeliveryMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.receiver.JmsQueueReceiver;
import wdsr.exercise4.sender.JmsSender;

public class Main {
	static final String QUEUE_NAME = "NONECOVER.QUEUE?consumer.prefetchSize=100000";
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		receiver();
	}
	
	private static void sender(){
		JmsSender jmsSender = new JmsSender(QUEUE_NAME);

		countTimeAndLog(() -> {
			for (int i = 1; i <= 10000; ++i) {
				jmsSender.sendTextToQueue("test_" + String.valueOf(i), DeliveryMode.PERSISTENT);
			}
		}, "10000 persistent messages sent in %d milliseconds.");

		countTimeAndLog(() -> {
			for (int i = 10001; i <= 20000; ++i) {
				jmsSender.sendTextToQueue("test_" + String.valueOf(i), DeliveryMode.NON_PERSISTENT);
			}
		}, "10000 persistent messages sent in %d milliseconds.");

		jmsSender.shutdown();
	}
	
	private static void countTimeAndLog(Runnable runnable, String message) {
		long begTime = System.currentTimeMillis();
		runnable.run();
		long executeTime = System.currentTimeMillis() - begTime;
		logger.info(String.format(message, executeTime));
	}
	
	private static void receiver(){
		JmsQueueReceiver receiver = new JmsQueueReceiver(QUEUE_NAME);
		receiver.recive();
		receiver.shutdown();
	}
}
