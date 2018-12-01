package Main;

import Classes.SearchEngineRepository;
import Classes.Website;
import Layout.GUI;
import Threads.Crawling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Main {
    private static int threadID = 1;
    //public static final Logger logger = LogManager.getLogger(Main.class);
    public static Queue<Website> sitesToCrawl = new ConcurrentLinkedQueue<Website>();
    private final static int MAX_SIZE = 3145728;
    private final static int MAX_THREAD_COUNT = 10;
    public static SearchEngineRepository ser;
    public static boolean crawlerISRunning = false;
    public GUI gui ;

    public Main(GUI pGui)  {
        crawlerISRunning = true;
        gui = pGui;
        ser = new SearchEngineRepository(gui);
        long startTime = System.currentTimeMillis();


        List<Website> NotCrawleds = ser.ReadNotCrawled();
        for(Website w:NotCrawleds)
            sitesToCrawl.add(w);

        List<Thread> threadList = new ArrayList<Thread>();
        Queue<Website> seeds = new ConcurrentLinkedQueue<>();
        //seeds.add(new Website("CalBaptist", "https://calbaptist.edu/", 0));
        //seeds.add(new Website("CNN", "https://www.cnn.com/", 0));
        //seeds.add(new Website("Wiki", "https://www.wikipedia.org/", 0));
        seeds.add(new Website("WhiteHouse", "https://www.whitehouse.gov/", 0));
        //seeds.add(new Website("Nasa", "https://www.nasa.gov/", 0));


        getFromSeed(seeds);


        gui.info("Starting WebCrawler");
        while (!sitesToCrawl.isEmpty() && ser.GetDBSize()<MAX_SIZE && crawlerISRunning) {
            threadList.clear();
            //Website w = sitesToCrawl.poll();
            Website w = sitesToCrawl.remove();
            Crawling crawlingThread = new Crawling(w, threadID++,gui);
            Thread thread = new Thread(crawlingThread);
            threadList.add(thread);
            if (sitesToCrawl.size() > MAX_THREAD_COUNT) {
                //logger.info("Creating more threads");
                Website[] ww = new Website[MAX_THREAD_COUNT];
                Crawling[] cc = new Crawling[MAX_THREAD_COUNT];
                for (int ii = 0; ii < MAX_THREAD_COUNT; ii++) {
                    ww[ii] = sitesToCrawl.poll();
                    cc[ii] = new Crawling(ww[ii], threadID++,gui);
                    threadList.add(new Thread(cc[ii]));
                }

            }

            //threads.add(thread);
            for (Thread t : threadList)
                t.start();

            try {
                // logger.info("Joining Threads");
                //main thread waits for the other threads to finish
                for (Thread t : threadList)
                    t.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
                gui.error(e.getMessage());
            }

            gui.info("sites to crawl " + sitesToCrawl.size() + "    thread ID: " + threadID + "   depth: " + w.getDepth() );

           /* if(switchSeed){
                sitesToCrawl.clear();
                switchSeed = false;
            }*/
            getFromSeed(seeds);

        }
        ser.closeDB();

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
                if (!ser.WebsiteExists(seedSite.getUrl(),0)) {
                    int id = ser.InsertWebsite(seedSite.getSiteName(), seedSite.getUrl(), 0, -1);
                    seedSite.setLinkID(id);
                    sitesToCrawl.add(seedSite);
                    gui.info("New Seed Added: " + seedSite.getUrl());
                }
            }
        }
    }
}
