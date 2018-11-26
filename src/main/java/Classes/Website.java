package Classes;

import java.util.ArrayList;

public class Website {
    private int linkID;
    private String siteName;
    private String url;
    private int LinkCount;
    private String sourceCode;
    private boolean isCrawled;
    private int depth;
    ArrayList<Website> innerWebsites;
    private int parentLink;

    public Website(String pSiteName, String pUrl, int pDepth) {
        this.siteName = pSiteName;
        this.url = pUrl;
        LinkCount = 0;
        isCrawled = false;
        depth = pDepth;
        innerWebsites = new ArrayList<Website>();
    }

    public int getLinkID(){
        return this.linkID;
    }

    public void setLinkID(int pLinkID){
        this.linkID = pLinkID;
    }

    public int getLinkCount() {
        return LinkCount;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getUrl() {
        return url;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void linkCountPlusOne() {
        this.LinkCount++;
    }

    public String getSourceCode() {
        return this.sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void isCrawled() {
        this.isCrawled = true;
    }

    public boolean getIsCrawled() {
        return this.isCrawled;
    }

    public void depthPlusOne() {
        this.depth++;
    }

    public int getDepth() {
        return this.depth;
    }

    public void addToInnerWebsites(Website pWebsite) {
        this.innerWebsites.add(pWebsite);
    }

    public ArrayList<Website> getInnerWebsites() {
        return this.innerWebsites;
    }

    public int getParentLink(){
        return this.parentLink;
    }

    public void setParentLink(int pParentLink){
        this.parentLink = pParentLink;
    }
}
