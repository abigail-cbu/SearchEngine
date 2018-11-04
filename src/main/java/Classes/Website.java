package Classes;

import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Website {
    private String siteName;
    private String url;
    private int LinkCount;
    private String bodyText;
    private boolean isCrawled;
    private int depth;
    ArrayList<Website> innerWebsites;

    public Website(String pSiteName, String pUrl, int pDepth){
        this.siteName = pSiteName;
        this.url = pUrl;
        LinkCount = 0;
        isCrawled=false;
        depth = pDepth;
        innerWebsites = new ArrayList<Website>();
    }

    public int getLinkCount() {
        return LinkCount;
    }

    public String getSiteName() {
        return siteName;
    }
    public String getUrl(){
        return url;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void linkCountPlusOne(){
        this.LinkCount++;
    }

    public String getBodyText() {
        return this.bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
    public void isCrawled(){
        this.isCrawled = true;
    }
    public boolean getIsCrawled (){
        return this.isCrawled;
    }
    public void depthPlusOne (){
        this.depth++;
    }
    public int getDepth(){
        return this.depth;
    }
    public void addToInnerWebsites (Website pWebsite){
        this.innerWebsites.add(pWebsite);
    }
    public ArrayList<Website> getInnerWebsites(){
        return this.innerWebsites;
    }
}
