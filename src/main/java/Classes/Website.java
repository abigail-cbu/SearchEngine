package Classes;

public class Website {
    private int linkID;
    private String siteName;
    private String url;
    private int linkCount;
    private String sourceCode;
    private boolean isCrawled;
    private int depth;
    private int prevID;
    private int parentID;


    public static class Builder {
        private int linkID;
        private String siteName;
        private String url;
        private int linkCount;
        private String sourceCode;
        private boolean isCrawled;
        private int depth;
        private int prevID;
        private int parentID;

        public Website.Builder withLinkID(int ID) {
            this.linkID = ID;
            return this;
        }
        public Website.Builder withSiteName(String siteName){
            this.siteName = siteName;
            return this;
        }
        public Website.Builder withURL(String url){
            this.url = url;
            return this;
        }
        public Website.Builder withLinkCount(int linkCount){
            this.linkCount = linkCount;
            return this;
        }
        public Website.Builder withIsCrawled(boolean isCrawled){
            this.isCrawled = isCrawled;
            return this;
        }
        public Website.Builder withDepth(int depth){
            this.depth = depth;
            return this;
        }
        public Website.Builder withPrevID(int prevID){
            this.prevID = prevID;
            return this;
        }
        public Website.Builder withParentID(int parentID){
            this.parentID = parentID;
            return this;
        }
        public Website.Builder withSourceCode(String sourceCode){
            this.sourceCode = sourceCode;
            return this;
        }


        public Website build(){
            Website website = new Website();
            website.linkID = this.linkID;
            website.prevID = this.prevID;
            website.parentID = this.parentID;
            website.isCrawled = this.isCrawled;
            website.siteName = this.siteName;
            website.sourceCode = this.sourceCode;
            website.depth = this.depth;
            website.url = this.url;
            website.linkCount = this.linkCount;
            return website;
        }
    }

    private Website() {

    }

    public int getLinkID(){
        return this.linkID;
    }

    public void setLinkID(int pLinkID){
        this.linkID = pLinkID;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(int pLInkCount){
        this.linkCount = pLInkCount;
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
        this.linkCount++;
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

    public int getDepth() {
        return this.depth;
    }

    public int getParentID(){
        return this.parentID;
    }

    public void setParentID(int pParentLink){
        this.parentID = pParentLink;
    }

    public int getPrevID(){
        return this.prevID;
    }
    public  void setPrevID(int pPrevID){
        this.prevID = pPrevID;
    }
}
