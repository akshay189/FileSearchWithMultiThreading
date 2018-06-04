package com.wavemaker.filesearch;


import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.*;

public class FileSearchTestCases
{



    @Test
    public void testFileSearch()
    {
        FileSearch fileSearch = new FileSearch();
        Map<String,List<SearchEntry>> entryMap   = fileSearch.keyWordSearch("/home/akshayk/Desktop/TestFolder","I");
        Assert.assertEquals(3,entryMap.size());
        Assert.assertEquals(5,entryMap.get("a.txt").get(2).getRowNumber());
    }





    /*@Test
    public void testFileresult() throws IOException {
        FileSearch fileSearch = new FileSearch();
        Map<String,List<SearchEntry>> entryMap = fileSearch.keyWordSearch("/home/akshayk/Desktop/TestFolder","I");
        Assert.assertEquals(2,entryMap.size());
        Assert.assertEquals(5,entryMap.get("a.txt").get(2).getRowNUmber());
    }
    @Test
    public void testEmptyFIle() throws IOException
    {
        FileSearch fileSearch = new FileSearch();
        List<SearchEntry> entryList = fileSearch.keyWordSearch("/home/akshayk/IdeaProjects/javafilesearch/src/main/java/com/wavemaker/filesearch/emptyFile.txt","I");
        Assert.assertEquals(0,entryList.size());
        Assert.assertTrue(entryList.isEmpty());
    }
    @Test(expectedExceptions = FileNotFoundException.class)
    public void testNoFileInput() throws IOException
    {
        FileSearch fileSearch = new FileSearch();
        List<SearchEntry> entryList = fileSearch.keyWordSearch("","I");
        Assert.assertEquals(0,entryList.size());
        Assert.assertTrue(entryList.isEmpty());
    }
    @Test
    public void testDirectoryResults()throws IOException
    {
        DirectorySearch directorySearch = new DirectorySearch();
        Map<String,List<SearchEntry>> resultMap = directorySearch.listOfFiles("/home/akshayk/Desktop/TestFolder");
        Assert.assertEquals(2,resultMap.size());
        Assert.assertEquals(7,resultMap.get("a.txt").size());
    }
    @Test
    public void testEmptyDirectory()throws IOException
    {
        DirectorySearch directorySearch = new DirectorySearch();
        Map<String,List<SearchEntry>> resultMap = directorySearch.listOfFiles("/home/akshayk/Desktop/TestEmptyFolder");
        Assert.assertEquals(0,resultMap.size());
        Assert.assertTrue(resultMap.isEmpty());
    }*/
}

