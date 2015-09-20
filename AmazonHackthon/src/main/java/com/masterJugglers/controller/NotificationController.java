package com.masterJugglers.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.masterJugglers.dao.SolrService;
import com.masterJugglers.modal.NotificationVO;
import com.masterJugglers.modal.Product;
import com.masterJugglers.modal.ProductVO;
import com.masterJugglers.modal.Subscriber;
import com.masterJugglers.service.NotificationService;

@Controller
@RequestMapping("/main")
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	SolrService solrService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String message(@ModelAttribute NotificationVO notification) {
		Subscriber subscriber = new Subscriber();
		Map<String, String> productReqMap = new HashMap<String, String>();
		productReqMap.put(notification.getAttrName(), notification.getAttrValue());
		subscriber.setSubscriberMail(notification.getMailAddress());
		notificationService.addSubscriber(subscriber);
		return "SUBSCRIBED";
	}
	
	@RequestMapping("/subscirption")
	@ResponseBody
	public String subscirption(@ModelAttribute ProductVO productVO) {
		Subscriber subscriber = new Subscriber();
		Map<String, String> productReqMap = new HashMap<String, String>();
		productReqMap.put("authors_f", productVO.getAuthor());
		productReqMap.put("publisher_f", productVO.getPubliser());
		productReqMap.put("releaseDate_d", productVO.getReleaseDate());
		productReqMap.put("title_f", productVO.getTitle());
		productReqMap.put("listPrice_i", productVO.getMaxlevel());
		productReqMap.put("listPrice_i_2", productVO.getMinlevel());
		subscriber.setSubscribeData(productReqMap);
		subscriber.setSubscriberMail(productVO.getMailAddress());
		notificationService.addSubscriber(subscriber);
		return "SUBSCRIBED";
	}

	
	@RequestMapping("/search")
	@ResponseBody
	public String search(@RequestParam("productId") String productId) {
		try {
			Product product = solrService.getProduct(productId);
			Map<String, String> displayOutput = product.getAttributeMap();
			displayOutput.remove("_version_");
			displayOutput.remove("updateTime");
			System.out.println(displayOutput);
			return displayOutput.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productId;
	}
}
