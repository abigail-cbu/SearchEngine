package Main;

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
    public static void main(String[] args) throws Exception {

        List<Thread> threadList = new ArrayList<Thread>();

        Website seedSite = new Website("CalBaptist","https://calbaptist.edu/",0);
        sitesToCrawl.add(seedSite);
        while (!sitesToCrawl.isEmpty()){
            threadList.clear();
            Website w =sitesToCrawl.poll();
            Crawling crawlingThread = new Crawling(w,threadID++);
            Thread thread = new Thread(crawlingThread);
            threadList.add(thread);
            if(sitesToCrawl.size()>100) {
                Website[] ww = new Website[100];
                Crawling[] cc = new Crawling[100];
                for(int ii=0;ii<100;ii++){
                    ww[ii] = sitesToCrawl.poll();
                    cc[ii] = new Crawling(ww[ii], threadID++);
                    threadList.add(new Thread(cc[ii]));
                }

            }

            //threads.add(thread);
            for(Thread t:threadList)
                t.start();

            try {
                //main thread waits for the other threads to finish
                for(Thread t:threadList)
                t.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("sites to crawl"+sitesToCrawl.size()+"    tread ID:"+threadID+"   depth:"+w.getDepth()+"  isCrawled:"+w.getIsCrawled()+"  name"+w.getSiteName()+"  URL "+w.getUrl());
        }

    }

}
