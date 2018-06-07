package com.wavemaker.filesearch;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileSearchTestCases  {


    @Test
    public void testPerformance()throws InterruptedException
    {
        WordSearch linearSearch = new WordSearch();
        /*
        * Linear search Performance */

        long startTimeOfLinearSearch = System.currentTimeMillis();
        linearSearch.searchWord("/home/akshayk/Desktop/TestFolder","Java",1,true);
        long endTimeOfLinearSearch = System.currentTimeMillis();

        System.out.println(endTimeOfLinearSearch - startTimeOfLinearSearch);

        /*
        * Parallel Search Performance */
        WordSearch parallelSearch = new WordSearch();

        for(int i=2;i<=14;i+=2)
        {
            long startTimeOfParallelSearch = System.currentTimeMillis();
            parallelSearch.searchWord("/home/akshayk/Desktop/TestFolder","Java",i,false);
            long endTimeOfParallelSearch = System.currentTimeMillis();

            System.out.println("Time taken by "+i+" Threads "+(endTimeOfParallelSearch - startTimeOfParallelSearch));
        }


    }

    @Test
    public void testFileSearchSequentialAndParallel() throws InterruptedException {


        WordSearch linearSearch = new WordSearch();;
        WordSearch parallelSearch = new WordSearch();


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    linearSearch.searchWord("/home/akshayk/Desktop/TestFolder", "Java", 1, true);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Thread interrupt :"+e);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    parallelSearch.searchWord("/home/akshayk/Desktop/TestFolder","java",10,false);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Thread interrupt :"+e);
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        Map<String, List<SearchEntry>> linearSearcOutput = linearSearch.getResult();
        Map<String, List<SearchEntry>> multiProcessingOutput = parallelSearch.getResult();

        Assert.assertEquals(linearSearcOutput.size(),multiProcessingOutput.size());

        for(Map.Entry<String,List<SearchEntry>> entry : linearSearcOutput.entrySet())
        {
            String file = entry.getKey();
            Assert.assertTrue(multiProcessingOutput.containsKey(file));


            List<SearchEntry> linearSearch_Result = entry.getValue();
            List<SearchEntry> parallelSearch_Result = multiProcessingOutput.get(file);


            Iterator linearSearchIterator = linearSearch_Result.iterator();
            Iterator parallelSearchIterator = parallelSearch_Result.iterator();


            while(linearSearchIterator.hasNext() && parallelSearchIterator.hasNext())
            {
                SearchEntry sequentialListEntry = (SearchEntry) linearSearchIterator.next();
                SearchEntry parallelListEntry = (SearchEntry) parallelSearchIterator.next();

                Assert.assertEquals(sequentialListEntry.getRowNumber(),parallelListEntry.getRowNumber());
                Assert.assertEquals(sequentialListEntry.getColumnNumber(),parallelListEntry.getColumnNumber());
            }
        }

    }
}

