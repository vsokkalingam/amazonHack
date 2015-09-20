package com.masterJugglers.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.masterJugglers.dao.SolrService;

@Component
public class FileReadScheduler {
	
	@Resource(name = "fileNameList")
	private List<String> fileNameList;
	
	@Autowired
	private SolrService solrService;
	
	public static final String DIR_PATH = "E:\\samples\\";

	@Scheduled(fixedRate = 3000)
	public void reportCurrentTime() throws IOException, SolrServerException {
		File folder = new File(DIR_PATH);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filePath = listOfFiles[i].getAbsolutePath();
				String fileName = listOfFiles[i].getName();
				if(filePath.endsWith(".csv") && !fileNameList.contains(fileName)){
					System.out.println("FILE READING STARTED : " + fileName);
					fileNameList.add(fileName);
					solrService.convertCsvToProducts(filePath);
				}
			} 
		}
	}

}
