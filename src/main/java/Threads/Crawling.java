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
    private Website page;
    private int MAX_DEPTH = 1;
    public GUI gui ;

    public Crawling(Website pWebsite, int pID, GUI pGui) {
        gui=pGui;
        MAX_DEPTH = gui.getMaxDepth();
        this.threadId = pID;
        page = pWebsite;
    }

    public int getId() {
        return this.threadId;
    }

    public void run() {

        try {
            final Document doc = Jsoup.connect(page.getUrl()).header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .timeout(12000)
                    .get();
            String elementUrl;

            if (page.getDepth() <= MAX_DEPTH-1) {
                for (Element l : doc.select("a[href]")) {
                    Website w;
                    int newDepth = page.getDepth() + 1;
                    if (l.attr("href").startsWith("//")) {
                        w = new Website.Builder().withSiteName(l.text()).withURL("http:"+ l.attr("href")).withDepth(newDepth).build();
                    } else if (l.attr("href").startsWith("/")) {
                        elementUrl = l.attr("href");
                        elementUrl = page.getUrl() + elementUrl.substring(1);
                        w = new Website.Builder().withSiteName(l.text()).withURL(elementUrl).withDepth(newDepth).build();
                    } else {
                        w = new Website.Builder().withSiteName(l.text()).withURL(l.attr("href")).withDepth(newDepth).build();
                    }

                    if (!gui.ser.WebsiteExists(w.getUrl(),w.getDepth())) {
                        w.setPrevID(page.getLinkID());
                        w.setParentID(page.getParentID());
                        int id=gui.ser.InsertWebsite(w.getSiteName(), w.getUrl(), w.getDepth(),w.getPrevID(),w.getParentID());
                        w .setLinkID(id);
                        Main.sitesToCrawl.add(w);
                        page.linkCountPlusOne();
                    }
                }
            }
            gui.ser.SetLinkCount(page.getUrl(),page.getLinkCount());
            page.setSourceCode(doc.body().text());
            gui.ser.InsertSourceCode(page.getLinkID(), doc.body().text());
            page.isCrawled();
            gui.ser.SetCrawled(page.getUrl());
        } catch (Exception e) {
            gui.error( e.getMessage() + " " + page.getUrl());
        }

    }

}
