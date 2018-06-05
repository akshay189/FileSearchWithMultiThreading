package com.wavemaker.filesearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSearch {

    public Map<String, List<SearchEntry>> keyWordSearch(String filePath, String stringToBeSearched) {
        Map<String, List<SearchEntry>> result = new HashMap<>();
        searchFile(filePath, stringToBeSearched.toLowerCase(), result);
        return result;
    }

    private void searchFile(String filePath, String keyToBeSearched, Map<String, List<SearchEntry>> resultMap) {
        File folder = new File(filePath);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                searchFile(file.getPath(), keyToBeSearched, resultMap);
            } else {
                resultMap.put(file.getPath(), searchFile(file, keyToBeSearched));
            }
        }
    }

    private List<SearchEntry> searchFile(File file, String keyToBeSearched) {
        List<SearchEntry> listOfSearchEntries = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            String line;
            int rowCount = 0, index;
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                line = line.toLowerCase();
                index = 0;
                rowCount++;
                while ((index = line.indexOf(keyToBeSearched, index)) != -1) {
                    listOfSearchEntries.add(new SearchEntry(rowCount, index));
                    index += 1;
                }
            }
            return listOfSearchEntries;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to close the stream", ex);
            }
        }
    }

}
