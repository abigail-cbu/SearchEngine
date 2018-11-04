package Threads;

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
    public Crawling(Website pWebsite,int pID){
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
        if(page.getDepth()>3 ){
            return;
        }

        // saves web page as a doc
        try {
            final Document doc = Jsoup.connect(page.getUrl()).get();
            String temp;
            for(Element l:doc.select("a[href]"))
            {
                Website w;
                page.linkCountPlusOne();
                if(l.attr("href").startsWith("/")){
                    temp = l.attr("href");
                    temp = page.getUrl()+temp.substring(1);
                    w= new Website(l.text(),temp,page.getDepth()+1);
                }
                else
                    w= new Website(l.text(),l.attr("href"),page.getDepth()+1);
                page.addToInnerWebsites(w);
                if(!Main.urlStrings.contains(w.getUrl())) {
                    Main.urlStrings.add(w.getUrl());
                    Main.sitesToCrawl.add(w);
                   // logger.info("   name:"+w.getSiteName()+"   URL"+w.getUrl()+"   depth:"+w.getDepth()+"  isCrawled:"+w.getIsCrawled());
                }


            }
            for(int i = 0;i<page.getLinkCount();i++){
                sb.append(i+1);
                sb.append(',');
              //  sb.append(page.getLinkNames().get(i+1));
                sb.append(',');
              //  sb.append(page.getLinks().get(i+1));
                sb.append('\n');

            }
            page.setBodyText(doc.body().text());
            page.isCrawled();

        }catch (Exception e){
            
        }
        
    }
    public boolean isDone() {
        return this.isDone;
    }
    public void toFile (String pPathName){
        try {
            PrintWriter pw = new PrintWriter(new File(pPathName));
            pw.write(sb.toString());
            pw.close();
        }catch (Exception e){

        }

    }
    public Website nextCrawl()
    {
        return page;

    }

}
