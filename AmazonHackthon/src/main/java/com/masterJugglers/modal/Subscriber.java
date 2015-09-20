package com.masterJugglers.modal;

import java.util.Map;

public class Subscriber {
	
	private String subscriberMail;
	
	private Map<String, String> subscribeData;
	public String getSubscriberMail() {
		return subscriberMail;
	}
	public void setSubscriberMail(String subscriberMail) {
		this.subscriberMail = subscriberMail;
	}
	public Map<String, String> getSubscribeData() {
		return subscribeData;
	}
	public void setSubscribeData(Map<String, String> subscribeData) {
		this.subscribeData = subscribeData;
	}
	
}
