package wdsr.exercise4;

import javax.jms.DeliveryMode;

import wdsr.exercise4.sender.JmsSender;

public class Main {
	static final String QUEUE_NAME = "NONECOVER.QUEUE?consumer.prefetchSize=100000";
	public static void main(String[] args) {
		JmsSender jmsSender = new JmsSender(QUEUE_NAME);
		
		for(int i=1;i<=10000;++i){
			jmsSender.sendTextToQueue("test_"+String.valueOf(i), DeliveryMode.PERSISTENT);
		}
		for(int i=10001;i<=20000;++i){
			jmsSender.sendTextToQueue("test_"+String.valueOf(i), DeliveryMode.NON_PERSISTENT);
		}
		
		jmsSender.shutdown();
	}
}
