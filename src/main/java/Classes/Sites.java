package Classes;

public class Sites
{
    private int link_id;
    private String site_name, url, source_code;

    public Sites(int lid, String sname, String url, String code)
    {
        link_id = lid;
        site_name = sname;
        this.url = url;
        source_code = code;
    }

    public int getLinkId()
    {
        return link_id;
    }

    public String getSiteName()
    {
        return site_name;
    }

    public String getURL()
    {
        return url;
    }

    public String getSourceCode()
    {
        return source_code;
    }
}
