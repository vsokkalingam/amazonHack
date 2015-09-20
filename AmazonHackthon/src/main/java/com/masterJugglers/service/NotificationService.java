package com.masterJugglers.service;

import org.springframework.stereotype.Service;

import com.masterJugglers.modal.Product;
import com.masterJugglers.modal.Subscriber;

@Service
public interface NotificationService {
	public void processNotification(String attributeName, String value, Product product);
	public void addSubscriber(Subscriber subscriber);
}
