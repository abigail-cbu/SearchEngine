package Threads;

import Classes.SearchEngineRepository;
import Classes.Website;
import Layout.GUI;
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
    private final int MAX_DEPTH = 1;

    // public static final Logger logger = LogManager.getLogger(Crawling.class);
    public GUI gui ;

    public Crawling(Website pWebsite, int pID, GUI pGui) {
        gui=pGui;
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


        // saves web page as a doc
        try {
            final Document doc = Jsoup.connect(page.getUrl()).header("Accept-Encoding", "gzip, deflate")
                    //.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .timeout(12000)
                    .get();
            String elementUrl; // for urls that are pointing to home page (i.e. /apply == calbaptist.edu/apply)
            // SearchEngineRepository ser = new SearchEngineRepository();

            // logger.info("Searching through page for links: " + page.getUrl());
            // logger.debug(doc.body().text());
            if (page.getDepth() <= MAX_DEPTH-1) {
                for (Element l : doc.select("a[href]")) {

                    Website w;

                    int newDepth = page.getDepth() + 1;

                    if (l.attr("href").startsWith("//")) {
                        w = new Website(l.text(), "http:"+ l.attr("href"), newDepth);
                    } else if (l.attr("href").startsWith("/")) {
                        elementUrl = l.attr("href");
                        elementUrl = page.getUrl() + elementUrl.substring(1);
                        w = new Website(l.text(), elementUrl, newDepth);
                    } else {
                        w = new Website(l.text(), l.attr("href"), newDepth);
                    }

                    //logger.info("Checking webiste:" + w.getUrl());

                    //page.addToInnerWebsites(w); // todo: we may not need this... -Abby

                    // 11/4/2018: saving websites to database
                    //                if (!Main.urlStrings.contains(w.getUrl())) {
                    //                    Main.urlStrings.add(w.getUrl());
                    //                    Main.sitesToCrawl.add(w);
                    //                    // logger.info("   name:"+w.getSiteName()+"   URL"+w.getUrl()+"   depth:"+w.getDepth()+"  isCrawled:"+w.getIsCrawled());
                    //                }

                    // only save unique websites
                    if (!Main.ser.WebsiteExists(w.getUrl(),w.getDepth())) {
                        // logger.info("Insert Website: " + w.getUrl());
                        w.setParentLink(page.getLinkID());
                        int id=Main.ser.InsertWebsite(w.getSiteName(), w.getUrl(), w.getDepth(),w.getParentLink());
                        w .setLinkID(id);
                        Main.sitesToCrawl.add(w);
                        page.linkCountPlusOne();
                    }// else if (!Main.ser.CheckIfIsCrawled(w.getUrl())) {
                    // Main.sitesToCrawl.add(w);
                    //}
                }
            }
            //page.setLinkCount(doc.select("a[href]").size());
            Main.ser.SetLinkCount(page.getUrl(),page.getLinkCount());

            // logger.info("Set Source Code for Website " + page.getUrl());
            page.setSourceCode(doc.body().text());
            Main.ser.InsertSourceCode(page.getLinkID(), doc.body().text());

            //logger.info("Set Crawled for Website " + page.getUrl());
            page.isCrawled();
            Main.ser.SetCrawled(page.getUrl());

        } catch (Exception e) {
            gui.error( e.getMessage() + " " + page.getUrl());
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
            gui.error(e.getMessage());
        }

    }

    public Website nextCrawl() {
        return page;

    }

}
