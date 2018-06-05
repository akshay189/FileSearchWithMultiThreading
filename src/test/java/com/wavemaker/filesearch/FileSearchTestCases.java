package com.wavemaker.filesearch;


import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.*;

public class FileSearchTestCases
{



    @Test
    public void testFileSearch() throws InterruptedException
    {
        FileSearch fileSearch = new FileSearch();
        Map<String,List<SearchEntry>> entryMap   = fileSearch.keyWordSearch("/home/akshayk/Desktop/testFolder","Java");
        System.out.println(entryMap.get("/home/akshayk/Desktop/testFolder/Effective_Java_2nd_Edition.pdf").size());
        FileSearchWIthThread fileSearchWIthThread = new FileSearchWIthThread();
        System.out.println(fileSearchWIthThread.keySearch("/home/akshayk/Desktop/testFolder","Java",10).get("/home/akshayk/Desktop/testFolder/folder1/Effective_Java_2nd_Edition.pdf").size());
//        Assert.assertEquals(3,entryMap.size());
//        Assert.assertEquals(5,entryMap.get("a.txt").get(2).getRowNumber());
    }
    @Test
    public void testFileSearchWithThreads() throws InterruptedException {
        FileSearch fileSearch = new FileSearch();
        FileSearchWIthThread fileSearchWIthThread = new FileSearchWIthThread();
        Map<String,List<SearchEntry>> entryMap   = fileSearch.keyWordSearch("/home/akshayk/Desktop/TestFolder","I");

        Map<String,List<SearchEntry>> resultMapOfThreadImplementation = fileSearchWIthThread.keySearch("/home/akshayk/Desktop/TestFolder","I",10);



        Assert.assertEquals(entryMap.size(),resultMapOfThreadImplementation.size());
        Assert.assertEquals(entryMap.keySet(),resultMapOfThreadImplementation.keySet());
        Assert.assertEquals(entryMap.get("a.txt").get(2).getRowNumber(),resultMapOfThreadImplementation.get("a.txt").get(2).getRowNumber());
    }
    @Test
    public void testTimeConsumptionBetweenApproaches() throws InterruptedException
    {
        FileSearch fileSearch = new FileSearch();
        FileSearchWIthThread fileSearchWIthThread = new FileSearchWIthThread();
        long startTimeOfIterartive = System.currentTimeMillis();
        Map<String,List<SearchEntry>> entryMap   = fileSearch.keyWordSearch("/home/akshayk/Desktop/TestEmptyFolder","I");
        long endTimeOfIterative = System.currentTimeMillis();
        System.out.println("Time taken to process without threads " + (endTimeOfIterative - startTimeOfIterartive));
        long startTimeOfConcurrence = System.currentTimeMillis();
        Map<String,List<SearchEntry>> resultMapOfThreadImplementation = fileSearchWIthThread.keySearch("/home/akshayk/Desktop/TestEmptyFolder","I",10);
        long endTimeOfConcurrence = System.currentTimeMillis();
        System.out.println("Time taken to process with Threads"+(endTimeOfConcurrence - startTimeOfConcurrence));
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

