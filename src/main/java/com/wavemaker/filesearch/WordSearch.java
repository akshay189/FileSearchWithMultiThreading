package com.wavemaker.filesearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.*;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;


public class WordSearch {

    private Map<String, List<SearchEntry>> result;
    static Logger log = Logger.getLogger(WordSearch.class.getName());

    public WordSearch() {
        this.result = new HashMap<>();

    }

    public void searchWord(String folderPath, String searchKey, int numberOfConsumerThreads, boolean sequential) throws InterruptedException {

        SearchContext searchContext = new SearchContext(folderPath, searchKey.toLowerCase(), result, new ArrayBlockingQueue<String>(50));
        Thread producerThread = new Thread(new Producer(searchContext));
        producerThread.start();
        log.info("producer Thread started");
        if (sequential) {
            producerThread.join();
            searchFile(1, searchContext);
        } else
            searchFile(numberOfConsumerThreads, searchContext);
        //return searchContext.getResult();
    }

    private void searchFile(int numberOfConsumerThreads, SearchContext searchContext) throws InterruptedException {

        Consumer consumer = new Consumer(searchContext);

        List<Thread> threadList = new ArrayList<>(numberOfConsumerThreads);
        for (int i = 0; i < numberOfConsumerThreads; i++) {
            Thread consumerThread = new Thread(consumer);
            consumerThread.setName("ConsumerThread- " + i);
            threadList.add(consumerThread);
            log.info("consumer Thread :" + consumerThread.getName() + " has started");
            consumerThread.start();

        }
        for (Thread thread : threadList) {
            thread.join();
        }
    }

    public class Producer implements Runnable {
        private SearchContext searchContext;

        Producer(SearchContext searchContextInfo) {
            this.searchContext = searchContextInfo;
        }

        @Override
        public void run() {
            searchFile(searchContext.getFolderPath(), searchContext);
            searchContext.setFinished(true);
        }

        private void searchFile(String folderPath, SearchContext searchContext) {
            File folder = new File(folderPath);
            for (File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    searchFile(file.getPath(), searchContext);
                } else {
                    log.info("Producer adding file : " + file.getPath());
                    searchContext.getListOfFiles().add(file.getPath());
                }
            }
        }
    }

    public class Consumer implements Runnable {
        private SearchContext searchContext;

        Consumer(SearchContext searchContext) {
            this.searchContext = searchContext;
        }

        @Override
        public void run() {
            List<SearchEntry> listOfSearchEntries;
            BufferedReader bufferedReader = null;
            try {
                while (true) {
                    String filePath;
                    synchronized (searchContext) {
                        if (searchContext.getListOfFiles().isEmpty() && searchContext.isFinished()) {
                            log.info("Consumer Thread "+Thread.currentThread().getName()+" has returned and list is empty");
                            return;
                        } else {

                            filePath = searchContext.getListOfFiles().take();
                            log.info("Consumer Thread "+Thread.currentThread().getName()+" has taken file "+filePath+" to process...");
                        }
                    }
                    listOfSearchEntries = new ArrayList<>();

                    String line;
                    int rowCount = 0, index;
                    bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
                    while ((line = bufferedReader.readLine()) != null) {
                        line = line.toLowerCase();
                        index = 0;
                        rowCount++;
                        while ((index = line.indexOf(searchContext.getSearchKey(), index)) != -1) {
                            listOfSearchEntries.add(new SearchEntry(rowCount, index));
                            index += 1;
                        }
                    }
                    searchContext.getResult().put(filePath, listOfSearchEntries);
                }
            } catch (Exception ex) {
                log.error("An interrupted exception has occured");
                throw new RuntimeException(ex);
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception ex) {
                        log.error("Failed to close the stream");
                        throw new RuntimeException("Failed to close the stream", ex);
                    }
                }
            }
        }
    }

    public Map<String, List<SearchEntry>> getResult() {
        return result;
    }


}