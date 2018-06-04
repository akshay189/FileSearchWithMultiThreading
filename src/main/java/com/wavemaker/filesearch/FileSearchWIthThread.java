package com.wavemaker.filesearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;


public class FileSearchWIthThread {

    public Map<String, List<SearchEntry>> keySearch(String folderPath, String searchKey,int numberOfConsumerThreads) throws InterruptedException {

        SearchContext searchContext = new SearchContext(folderPath, searchKey, new HashMap<>(), new ArrayBlockingQueue<String>(50));

        Thread producerThread = new Thread(new Producer(searchContext));
        producerThread.start();

        Thread consumerThread = new Thread(new Consumer(searchContext));
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        return searchContext.getResult();
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
            List<SearchEntry> listOfSearchEntries = new ArrayList<>();
            BufferedReader bufferedReader = null;
            try {
                while (!searchContext.getListOfFiles().isEmpty() || !searchContext.isFinished()) {
                    String filePath = searchContext.getListOfFiles().take();
                    String line;
                    int rowCount = 0, index;
                    bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
                    while ((line = bufferedReader.readLine()) != null) {
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
                throw new RuntimeException(ex);
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to close the stream", ex);
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        FileSearchWIthThread fst = new FileSearchWIthThread();
        long startTime = System.currentTimeMillis();
        System.out.println(fst.keySearch("/home/akshayk/Desktop/TestEmptyFolder", "I").keySet().size());
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}