package Classes;

import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Website {
    private String siteName;
    private String url;
    private Map<Integer,String> links;
    private Map<Integer,String> linkNames;
    private int LinkCount;
    private String bodyText;
    private boolean isCrawled;

    public Website(String pSiteName, String pUrl){
        this.siteName = pSiteName;
        this.url = pUrl;
        links = new HashMap<Integer, String>();
        linkNames = new HashMap<Integer, String>();
        LinkCount = 0;
        isCrawled=false;
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

    public Map<Integer,String> getLinks() {
        return links;
    }
    public Map<Integer,String> getLinkNames() {
        return linkNames;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setLinks(Integer pKey, String pValue) {
        this.links.put(pKey,pValue);
    }
    public void setLinkNames(Integer pKey, String pValue) {
        this.linkNames.put(pKey,pValue);
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
}
