package com.wavemaker.filesearch;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class SearchContext {
    private String folderPath;
    private String searchKey;
    private Map<String, List<SearchEntry>> result;
    private BlockingQueue<String> listOfFiles;
    private boolean finished;
    private FileSearch fileSearch = new FileSearch();
    public SearchContext(String folderPath, String searchKey,Map<String,List<SearchEntry>> result, BlockingQueue<String> listOfFiles) {
        this.folderPath = folderPath;
        this.searchKey = searchKey;
        this.result = result;
        this.listOfFiles = listOfFiles;

    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Map<String, List<SearchEntry>> getResult() {
        return result;
    }

    public void setResult(Map<String, List<SearchEntry>> result) {
        this.result = result;
    }

    public BlockingQueue<String> getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(BlockingQueue<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    public boolean isFinished() {
        return finished;
    }
    public FileSearch getFileSearchObject()
    {
        return fileSearch;
    }
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
