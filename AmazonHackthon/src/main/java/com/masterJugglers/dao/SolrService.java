package com.masterJugglers.dao;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Service;

import com.masterJugglers.modal.Product;
import com.masterJugglers.modal.ProductCSV;

@Service
public interface SolrService {

	public void updateProductsInSolr(List<ProductCSV> productList) throws SolrServerException, IOException;
	
	public void convertCsvToProducts(String filePath) throws IOException, SolrServerException;
	
	public Product getProduct(String productId) throws IOException, SolrServerException;
}
