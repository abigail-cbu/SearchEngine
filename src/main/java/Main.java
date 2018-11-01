import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws Exception {

        Website CBU = new Website("CalBaptist", "https://calbaptist.edu/");

        PrintWriter pw = new PrintWriter(new File("test.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Title");
        sb.append(',');
        sb.append("Link");
        sb.append('\n');

        final Document doc = Jsoup.connect(CBU.getUrl()).get();
        String temp;
        for (Element l : doc.select("a[href]")) {
            CBU.linkCountPlusOne();
            CBU.setLinkNames(CBU.getLinkCount(), l.text());
            if (l.attr("href").startsWith("/")) {
                temp = l.attr("href");
                temp = CBU.getUrl() + temp.substring(1);
                CBU.setLinks(CBU.getLinkCount(), temp);
            } else
                CBU.setLinks(CBU.getLinkCount(), l.attr("href"));
        }

           /* Elements links = doc.select("a[href]"); // a with href
            Elements pngs = doc.select("img[src$=.png]");
            // img with src ending .png
            int counter =1;
            Element masthead = doc.select("div.masthead").first();*/

        /*
        for(int i = 0;i<CBU.getLinkCount();i++){

            //System.out.println(CBU.getLinkNames().get(i)+":      "+CBU.getLinks().get(i));

            sb.append(i+1);
            sb.append(',');
            sb.append(CBU.getLinkNames().get(i+1));
            sb.append(',');
            sb.append(CBU.getLinks().get(i+1));
            sb.append('\n');

        }

        pw.write(sb.toString());
        pw.close();
        */

        for (int i = 0; i < CBU.getLinkCount(); i++) {
            SearchEngineRepository ser = new SearchEngineRepository();
            ser.InsertLink(CBU.getLinkNames().get(i + 1), CBU.getLinks().get(i + 1), 0, 0);
        }

    }
}
