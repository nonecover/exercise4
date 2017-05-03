package wdsr.exercise4.receiver;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import wdsr.exercise4.PriceAlert;
import wdsr.exercise4.VolumeAlert;

public class MessageListenerImp implements MessageListener {
	private final AlertService alertService;

	public MessageListenerImp(AlertService alertService) {
		this.alertService = alertService;
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				onObjectMessage((ObjectMessage) message);
			} else if (message instanceof TextMessage) {
				onTextMessage((TextMessage) message);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void onObjectMessage(ObjectMessage objectMessage) throws JMSException {
		String jmsType = objectMessage.getJMSType();
		if ("PriceAlert".equals(jmsType)) {
			alertService.processPriceAlert((PriceAlert) objectMessage.getObject());
		} else if ("VolumeAlert".equals(jmsType)) {
			alertService.processVolumeAlert((VolumeAlert) objectMessage.getObject());
		}
	}

	private void onTextMessage(TextMessage textMessage) throws JMSException {
		String text = textMessage.getText();
		String[] processAlertArgs = splitMessageText(text);
		String jmsType = textMessage.getJMSType();

		if ("PriceAlert".equals(jmsType)) {
			alertService.processPriceAlert(new PriceAlert(Long.valueOf(processAlertArgs[0]), processAlertArgs[1],
					new BigDecimal(processAlertArgs[2])));
		} else if ("VolumeAlert".equals(jmsType)) {
			alertService.processVolumeAlert(new VolumeAlert(Long.valueOf(processAlertArgs[0]), processAlertArgs[1],
					Long.valueOf(processAlertArgs[2])));
		}

	}

	private String[] splitMessageText(String text) {
		return Arrays.stream(text.split("[\r\n]+")).map(s -> s.substring(s.indexOf('=') + 1).trim())
				.toArray(String[]::new);
	}
}