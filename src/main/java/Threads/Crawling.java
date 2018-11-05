package Threads;

import Classes.SearchEngineRepository;
import Classes.Website;
import Main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.PrintWriter;

public class Crawling implements Runnable {
    private int threadId = 1;
    private boolean isDone = false;
    private Website page;
    private StringBuilder sb;
    public static final Logger logger = LogManager.getLogger(Crawling.class);

    public Crawling(Website pWebsite, int pID) {
        this.threadId = pID;
        page = pWebsite;
        sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Title");
        sb.append(',');
        sb.append("Link");
        sb.append('\n');
    }

    public int getId() {
        return this.threadId;
    }

    public void run() {
        if (page.getDepth() > 0) {
            return;
        }

        // saves web page as a doc
        try {
            final Document doc = Jsoup.connect(page.getUrl()).get();
            String elementUrl; // for urls that are pointing to home page (i.e. /apply == calbaptist.edu/apply)
            SearchEngineRepository ser = new SearchEngineRepository();

            logger.info("Searching through page for links: " + page.getUrl());
            for (Element l : doc.select("a[href]")) {
                Website w;
                page.linkCountPlusOne();
                int newDepth = page.getDepth() + 1;

                if (l.attr("href").startsWith("/")) {
                    elementUrl = l.attr("href");
                    elementUrl = page.getUrl() + elementUrl.substring(1);
                    w = new Website(l.text(), elementUrl, newDepth);
                } else {
                    w = new Website(l.text(), l.attr("href"), newDepth);
                }

                //page.addToInnerWebsites(w); // todo: we may not need this... -Abby

                // 11/4/2018: saving websites to database
//                if (!Main.urlStrings.contains(w.getUrl())) {
//                    Main.urlStrings.add(w.getUrl());
//                    Main.sitesToCrawl.add(w);
//                    // logger.info("   name:"+w.getSiteName()+"   URL"+w.getUrl()+"   depth:"+w.getDepth()+"  isCrawled:"+w.getIsCrawled());
//                }

                // only save unique websites
                if (!ser.WebsiteExists(w.getUrl())) {
                    logger.info("Insert Website: " + w.getUrl());
                    ser.InsertWebsite(w.getSiteName(), w.getUrl(), w.getDepth());
                    Main.sitesToCrawl.add(w);
                }
                else
                {
                    logger.info("Website Exists: " + w.getUrl());
                }
            }

            logger.info("Set Source Code for Website " + page.getUrl());
            page.setSourceCode(doc.body().text());
            ser.InsertSourceCode(page.getUrl(), doc.body().text());

            logger.info("Set Crawled for Website " + page.getUrl());
            page.isCrawled();
            ser.SetCrawled(page.getUrl());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public boolean isDone() {
        return this.isDone;
    }

    public void toFile(String pPathName) {
        try {
            PrintWriter pw = new PrintWriter(new File(pPathName));
            pw.write(sb.toString());
            pw.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public Website nextCrawl() {
        return page;

    }

}
