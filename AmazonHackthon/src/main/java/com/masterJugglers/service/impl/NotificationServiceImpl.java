package com.masterJugglers.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.masterJugglers.modal.Product;
import com.masterJugglers.modal.Subscriber;
import com.masterJugglers.service.NotificationService;

@Component
public class NotificationServiceImpl implements NotificationService {

	@Resource(name = "subscriberList")
	private List<Subscriber> subscriberList;

	public void processNotification(String attributeName, String value, Product product) {
		for(Subscriber subscriber : subscriberList){
			processNotificationForSubscriber(subscriber, attributeName, value, product);
		}
	}

	public void processNotificationForSubscriber(Subscriber subscriber, String attributeName, String value, Product product) {
		boolean isValid = false;
		Map<String, String> subscriberMap = subscriber.getSubscribeData();
		attributeName = attributeName.trim();
		String subValue = subscriberMap.get(attributeName);
		if(subValue != null) {
			if(attributeName.equals("listPrice_i")){
				int val1 = Integer.parseInt(subValue);
				int val2 = Integer.parseInt(subscriberMap.get("listPrice_i_2"));
				int storedVal = Integer.parseInt(value);
				if(storedVal >= val1 && storedVal <= val2){
					isValid = true;
				}
			} else {
				if(subValue.equals(value.trim())){
					isValid = true;
				}
			}
		}
		if(isValid) {
			sendNotification(subscriber.getSubscriberMail(), product);
		}
	}

	public void addSubscriber(Subscriber subscriber) {
		subscriberList.add(subscriber);
	}

	private void sendNotification(String userMail, Product product) {
		System.out.println(
				new StringBuilder("Notification sent :").append(userMail).append(" ").append(product).toString());
	}

}
