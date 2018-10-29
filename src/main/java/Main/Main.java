package Main;

import Classes.Website;
import Threads.Crawling;
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
    public static void main(String[] args) throws Exception {
        List<Website> websiteList = new ArrayList<Website>();
        List<Crawling> crawlerList = new ArrayList<Crawling>();
        Queue<Website> sitesToCrawl = new ConcurrentLinkedQueue<Website>();
        /*Website CBU = new Website("CalBaptist","https://calbaptist.edu/");

        PrintWriter pw = new PrintWriter(new File("test.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Title");
        sb.append(',');
        sb.append("Link");
        sb.append('\n');

        // saves web page as a doc
        final Document doc = Jsoup.connect(CBU.getUrl()).get();
        String temp;
        //searches every element in the doc for links
        for(Element l:doc.select("a[href]"))
        {
            CBU.linkCountPlusOne();
            CBU.setLinkNames(CBU.getLinkCount(),l.text());
            if(l.attr("href").startsWith("/")){
                temp = l.attr("href");
                temp = CBU.getUrl()+temp.substring(1);
                CBU.setLinks(CBU.getLinkCount(),temp);
            }
            else
                CBU.setLinks(CBU.getLinkCount(),l.attr("href"));
        }
           *//* Elements links = doc.select("a[href]"); // a with href
            Elements pngs = doc.select("img[src$=.png]");
            // img with src ending .png
            int counter =1;
            Element masthead = doc.select("div.masthead").first();*//*
        for(int i = 0;i<CBU.getLinkCount();i++){

            //System.out.println(CBU.getLinkNames().get(i)+":      "+CBU.getLinks().get(i));

            sb.append(i+1);
            sb.append(',');
            sb.append(CBU.getLinkNames().get(i+1));
            sb.append(',');
            sb.append(CBU.getLinks().get(i+1));
            sb.append('\n');

        }
        //CBU.setBodyText(doc.body().text());
        String[] splited = doc.body().text().split(" ");
        for(String s:splited)
              System.out.println(s);
        pw.write(sb.toString());
        pw.close();*/
        int numberOfThread = 1;
        Crawling crawlingThread = new Crawling("CalBaptist","https://calbaptist.edu/",threadID++);
        Thread thread = new Thread(crawlingThread);
        //threads.add(thread);
        thread.start();
        try {
            //main thread waits for the other threads to finish
            thread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       // crawlingThread.toFile("test.csv");
        Website nextPage = crawlingThread.nextCrawl();
       // websiteList.add(nextPage);
        /*for(int i=0; i<nextPage.getLinkCount();i++)
        {
            Crawling crawlingSubThread = new Crawling(nextPage.getLinkNames().get(i+1),
                    nextPage.getLinks().get(i+1),numberOfThread++);
            Thread nextThread = new Thread(crawlingSubThread);
            crawlerList.add(crawlingSubThread);
            threads.add(nextThread);
            nextThread.start();
        }*/
        System.out.println(nextPage.getSiteName());
        System.out.println(nextPage.getLinkCount());
        for (int i = 0; i < nextPage.getLinkCount(); i++) {
            sitesToCrawl.add(new Website(nextPage.getLinkNames().get(i+1),nextPage.getLinks().get(i+1)));
            System.out.println(nextPage.getLinks().get(i+1));
        }

        while(!sitesToCrawl.isEmpty()){
            Crawling crawlingThread2 = new Crawling(sitesToCrawl.poll().getSiteName(),sitesToCrawl.poll().getUrl(),threadID++);
            Thread thread2 = new Thread(crawlingThread2);
            thread2.start();
            try {
                //main thread waits for the other threads to finish
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<crawlingThread2.nextCrawl().getLinkCount();i++){
                websiteList.add(new Website(crawlingThread2.nextCrawl().getLinkNames().get(i+1),crawlingThread2.nextCrawl().getLinks().get(i+1)));
            }


        }
        for(Website w:websiteList)
            sitesToCrawl.add(w);

       /* createNewCrawlingThreads(nextPage,websiteList, crawlerList,2);
        try {
            //main thread waits for the other threads to finish
            for (Thread t:threads)
                t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int nextIteration = threads.size();
        threads.clear();
        for(int j = 0;j<nextIteration; j++){
            websiteList.add(crawlerList.get(j).nextCrawl());
            createNewCrawlingThreads(crawlerList.get(j).nextCrawl(),websiteList,crawlerList,3);
          //  System.out.println("number of websites: "+websiteList.size());
        }
        try {
            //main thread waits for the other threads to finish
            for (Thread t:threads)
                t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextIteration = threads.size();
        for(Crawling c:crawlerList) {
            if(!websiteList.contains(c.nextCrawl()))
            websiteList.add(c.nextCrawl());
           // System.out.println("number of websites: "+websiteList.size()+"   Site Name");
        }*/
        int count = 1;
        for(Website w:websiteList)
            System.out.println(count++ +" Site:"+w.getSiteName()+ "  "+w.getIsCrawled());


    }
   static void createNewCrawlingThreads (Website pPage,List<Website> pWebsiteList, List<Crawling> pCrawlerList,int pIteration){
        if(pIteration<=3) {
            for (int i = 0; i < pPage.getLinkCount(); i++) {

                Crawling crawlingSubThread = new Crawling(pPage.getLinkNames().get(i + 1),
                        pPage.getLinks().get(i + 1), threadID++);
                Thread nextThread = new Thread(crawlingSubThread);
                pCrawlerList.add(crawlingSubThread);
                threads.add(nextThread);
                nextThread.start();
            }
            try {
                //main thread waits for the other threads to finish
                for (Thread t : threads)
                    t.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int nextNUmberOfCrawls = threads.size();
            threads.clear();
            pIteration = 3;
            for(int j = 0;j<nextNUmberOfCrawls; j++){
                pWebsiteList.add(pCrawlerList.get(j).nextCrawl());
                createNewCrawlingThreads(pCrawlerList.get(j).nextCrawl(),pWebsiteList,pCrawlerList,pIteration);
                //  System.out.println("number of websites: "+websiteList.size());
            }
        }
    }
}
