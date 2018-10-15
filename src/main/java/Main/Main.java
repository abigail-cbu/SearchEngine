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

public class Main {
    private static List<Thread> threads = new ArrayList<Thread>();
    public static void main(String[] args) throws Exception {
        List<Website> websiteList = new ArrayList<Website>();
        List<Crawling> crawlerList = new ArrayList<Crawling>();

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
        Crawling crawlingThread = new Crawling("CalBaptist","https://calbaptist.edu/",numberOfThread++);
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
        websiteList.add(nextPage);
        for(int i=0; i<nextPage.getLinkCount();i++)
        {
            Crawling crawlingSubThread = new Crawling(nextPage.getLinkNames().get(i+1),
                    nextPage.getLinks().get(i+1),numberOfThread++);
            Thread nextThread = new Thread(crawlingSubThread);
            crawlerList.add(crawlingSubThread);
            threads.add(nextThread);
            nextThread.start();
        }
        try {
            //main thread waits for the other threads to finish
            for (Thread t:threads)
                t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Crawling c:crawlerList){
            websiteList.add(c.nextCrawl());
            System.out.println("number of websites: "+websiteList.size());
        }


    }
}
