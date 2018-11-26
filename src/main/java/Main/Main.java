package Main;

import Classes.SearchEngineRepository;
import Classes.Website;
import Threads.Crawling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    private static List<Thread> threads = new ArrayList<Thread>();
    private static int threadID = 1;
    public static final Logger logger = LogManager.getLogger(Main.class);
    public static Queue<Website> sitesToCrawl = new ConcurrentLinkedQueue<Website>();
    public static List<String> urlStrings = new ArrayList<String>();
    public static boolean switchSeed = false;
    private final static int MAX_SIZE = 3145728;


    public static void main(String[] args) throws Exception {

        List<Thread> threadList = new ArrayList<Thread>();
        Queue<Website> seeds = new ConcurrentLinkedQueue<>();
        seeds.add(new Website("CalBaptist", "https://calbaptist.edu/", 0));
        //seeds.add(new Website("CNN", "https://www.cnn.com/", 0));
        //seeds.add(new Website("Wiki", "https://www.wikipedia.org/", 0));
        //seeds.add(new Website("WhiteHouse", "https://www.whitehouse.gov/", 0));
       // seeds.add(new Website("Nasa", "https://www.nasa.gov/", 0));

        Website seedSite = seeds.remove();

        SearchEngineRepository ser = new SearchEngineRepository();
        if (!ser.WebsiteExists(seedSite.getUrl())) {
            int id=ser.InsertWebsite(seedSite.getSiteName(), seedSite.getUrl(), 0,-1);
            seedSite.setLinkID(id);

        }
        sitesToCrawl.add(seedSite);

        logger.info("Starting WebCrawler");
        while (!sitesToCrawl.isEmpty() && ser.GetDBSize()<MAX_SIZE ) {
            threadList.clear();
            //Website w = sitesToCrawl.poll();
            Website w = sitesToCrawl.remove();
            Crawling crawlingThread = new Crawling(w, threadID++);
            Thread thread = new Thread(crawlingThread);
            threadList.add(thread);
            /*if (sitesToCrawl.size() > 10) {
                logger.info("Creating more threads");
                Website[] ww = new Website[100];
                Crawling[] cc = new Crawling[100];
                for (int ii = 0; ii < 100; ii++) {
                    ww[ii] = sitesToCrawl.poll();
                    cc[ii] = new Crawling(ww[ii], threadID++);
                    threadList.add(new Thread(cc[ii]));
                }

            }*/

            //threads.add(thread);
            for (Thread t : threadList)
                t.start();

            try {
                logger.info("Joining Threads");
                //main thread waits for the other threads to finish
                for (Thread t : threadList)
                    t.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            logger.info("sites to crawl " + sitesToCrawl.size() + "    thread ID: " + threadID + "   depth: " + w.getDepth() + "  isCrawled: " + w.getIsCrawled() + "  name " + w.getSiteName() + "  URL " + w.getUrl());

           /* if(switchSeed){
                sitesToCrawl.clear();
                switchSeed = false;
            }*/
            if(sitesToCrawl.isEmpty()){
                if(!seeds.isEmpty()) {
                    seedSite = seeds.remove();
                    if (!ser.WebsiteExists(seedSite.getUrl())) {
                        int id=ser.InsertWebsite(seedSite.getSiteName(), seedSite.getUrl(), 0,-1);
                        seedSite.setLinkID(id);
                    }
                    sitesToCrawl.add(seedSite);

                }
            }
        }

        logger.info("Finished WebCrawler");
    }
}
