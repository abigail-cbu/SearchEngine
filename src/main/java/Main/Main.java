package Main;

import Classes.Website;
import Layout.GUI;
import Threads.Crawling;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Main {
    private static int threadID = 1;
    public static Queue<Website> sitesToCrawl = new ConcurrentLinkedQueue<Website>();
    private static int MAX_SIZE = 3145728;
    private static int MAX_THREAD_COUNT = 10;
    public static boolean crawlerISRunning = false;
    public GUI gui ;

    public Main(GUI pGui)  {
        crawlerISRunning = true;
        gui = pGui;
        long startTime = System.currentTimeMillis();
        MAX_SIZE = gui.getMaxSize();
        MAX_THREAD_COUNT = gui.getMaxNumOfThreads();
        List<Website> NotCrawleds = gui.ser.ReadNotCrawled();
        for(Website w:NotCrawleds) {
            sitesToCrawl.add(w);
        }
        List<Thread> threadList = new ArrayList<Thread>();
        Queue<Website> seeds = new ConcurrentLinkedQueue<>();
        ArrayList<Website> seedsFromGUI = (ArrayList<Website>) gui.getSeeds().clone();
        for(Website w : seedsFromGUI){
            seeds.add(w);
        }
        getFromSeed(seeds);
        gui.info("Starting WebCrawler");
        while (!sitesToCrawl.isEmpty() && gui.ser.getDBSize()<MAX_SIZE && crawlerISRunning) {
            threadList.clear();
            Website w = sitesToCrawl.remove();
            Crawling crawlingThread = new Crawling(w, threadID++,gui);
            Thread thread = new Thread(crawlingThread);
            threadList.add(thread);
            if (sitesToCrawl.size() > MAX_THREAD_COUNT) {
                Website[] ww = new Website[MAX_THREAD_COUNT];
                Crawling[] cc = new Crawling[MAX_THREAD_COUNT];
                for (int ii = 0; ii < MAX_THREAD_COUNT; ii++) {
                    ww[ii] = sitesToCrawl.poll();
                    cc[ii] = new Crawling(ww[ii], threadID++,gui);
                    threadList.add(new Thread(cc[ii]));
                }

            }
            for (Thread t : threadList) {
                t.start();
            }
            try {
                // logger.info("Joining Threads");
                //main thread waits for the other threads to finish
                for (Thread t : threadList) {
                    t.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                gui.error(e.getMessage());
            }
            gui.info("sites to crawl " + sitesToCrawl.size() + "    thread ID: " + threadID + "   depth: " + w.getDepth() );
            getFromSeed(seeds);
        }

        gui.info("Finished WebCrawler");
        gui.btnStart.setText("Start Crawler");
        crawlerISRunning=false;
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        gui.info("Time elapsed: "+(duration/1000) + "s");
    }

    private void getFromSeed(Queue<Website> seeds) {
        if(sitesToCrawl.isEmpty()) {
            while (sitesToCrawl.isEmpty() && !seeds.isEmpty()) {
                Website seedSite = seeds.remove();
                if (!gui.ser.WebsiteExists(seedSite.getUrl(),0)) {
                    int id = gui.ser.InsertWebsite(seedSite.getSiteName(), seedSite.getUrl(), 0, -1,-1);
                    seedSite.setLinkID(id);
                    seedSite.setParentID(id);
                    sitesToCrawl.add(seedSite);
                    gui.info("New Seed Added: " + seedSite.getUrl());
                }
            }
        }
    }
}
