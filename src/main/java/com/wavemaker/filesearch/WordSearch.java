package com.wavemaker.filesearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;


public class WordSearch {

    private Map<String, List<SearchEntry>> result;

    public WordSearch()
    {
        this.result = new HashMap<>();

    }

    public void searchWord(String folderPath, String searchKey, int numberOfConsumerThreads, boolean sequential) throws InterruptedException {

        SearchContext searchContext = new SearchContext(folderPath, searchKey.toLowerCase(), result, new ArrayBlockingQueue<String>(50));
        Thread producerThread = new Thread(new Producer(searchContext));
        producerThread.start();
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
            consumerThread.setName("Thread- " + i);
            threadList.add(consumerThread);
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
        public void run()
        {
            List<SearchEntry> listOfSearchEntries;
            BufferedReader bufferedReader = null;
            try {
                while (true) {
                    String filePath;
                    synchronized (searchContext) {
                        if (searchContext.getListOfFiles().isEmpty() && searchContext.isFinished()) {
                            return;
                        } else
                            filePath = searchContext.getListOfFiles().take();
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

    public Map<String, List<SearchEntry>> getResult() {
        return result;
    }


}