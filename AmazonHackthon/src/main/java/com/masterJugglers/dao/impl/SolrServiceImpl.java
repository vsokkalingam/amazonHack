package com.masterJugglers.dao.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.masterJugglers.dao.SolrService;
import com.masterJugglers.modal.Product;
import com.masterJugglers.modal.ProductCSV;
import com.masterJugglers.service.ChunkingTaskExecutorService;
import com.masterJugglers.service.NotificationService;

@Component
public class SolrServiceImpl implements SolrService {

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private NotificationService notificationService;

	public void convertCsvToProducts(String filePath) throws IOException, SolrServerException {
		
		final List<ProductCSV> productList = new ArrayList<ProductCSV>();
		ExecutorService exec = Executors.newFixedThreadPool(8);
		final RandomAccessFile in = new RandomAccessFile(filePath, "r");
		in.seek(0L);
		
		ChunkingTaskExecutorService s = new ChunkingTaskExecutorService(exec, 1);
		s.execute(new Runnable(){
			public void run(){
				try {
					int readSize = (int)Math.min(100000000, in.length() - in.getFilePointer());
				
					final byte[] data = new byte[readSize];
					in.readFully(data);
					String str = new String(data);
					String[] array = str.split("\n");
					for(String line : array){
						String[] words = line.split(",");
						System.out.println("PROCESSING ID : " + words[0]);
						String attName = words[1];
						ProductCSV product = new ProductCSV();
						product.setProductId(words[0]);
						if (attName.startsWith("release ")) {
							attName = "releaseDate_d";
						} else if (attName.startsWith("list ")) {
							attName = "listPrice_i";
						} else {
							attName = attName + "_f";
						}
						product.setAttributeName(attName);
						product.setAttributeValue(words[2]);
						productList.add(product);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		updateProductsInSolr(productList);
	}

	public void updateProductsInSolr(List<ProductCSV> productList) throws SolrServerException, IOException {

		for (ProductCSV product : productList) {
			SolrDocument document = solrClient.getById("amazProduct", product.getProductId());
			String attrName = product.getAttributeName();
			String attrVal = product.getAttributeValue();
			SolrInputDocument sdoc = null;
			if (document != null) {
				sdoc = ClientUtils.toSolrInputDocument(document);
				if (sdoc.get(attrName) == null) {
					sdoc.addField(attrName, attrVal);
				} else {
					sdoc.setField(attrName, attrVal);
				}
				Product productFull = new Product();
				Map<String, String> valMap = new HashMap<String, String>();
				productFull.setAttributeMap(valMap);
				for (String fieldName : sdoc.getFieldNames()) {
					valMap.put(fieldName, sdoc.get(fieldName).toString());
				}
				notificationService.processNotification(attrName, attrVal, productFull);
			} else {
				sdoc = new SolrInputDocument();
				sdoc.addField("id", product.getProductId());
				sdoc.addField(attrName, attrVal);
				Product productFull = new Product();
				Map<String, String> valMap = new HashMap<String, String>();
				productFull.setAttributeMap(valMap);
				valMap.put(attrName, attrVal);
				notificationService.processNotification(attrName, attrVal, productFull);
			}

			solrClient.add("amazProduct", sdoc);
			solrClient.commit("amazProduct", true, false);
		}
		solrClient.commit("amazProduct", true, true);
	}

	public Product getProduct(String productId) throws IOException, SolrServerException {
		SolrDocument document = solrClient.getById("amazProduct", productId);
		Product productFull = new Product();
		if (document != null) {
			Map<String, String> valMap = new HashMap<String, String>();
			productFull.setAttributeMap(valMap);
			for (String fieldName : document.getFieldNames()) {
				valMap.put(fieldName, document.get(fieldName).toString());
			}
		}
		return productFull;
	}
}
