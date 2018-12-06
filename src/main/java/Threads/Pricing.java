package Threads;

import Classes.Product;
import Layout.GUI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Timer;
import java.util.TimerTask;

public class Pricing
{
    public GUI gui ;
    Timer timer;

    public Pricing(GUI pGui) {
        gui=pGui;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                crawlPrice();
            }
        },0, gui.getRepetitionCycle()*1000);

    }

    public static void crawl(Product p, GUI gui) {
        if (p.getURL().equals(""))
        {
            return;
        }
        final Document doc;

        // saves web page as a doc
        try {
            doc = Jsoup.connect(p.getURL()).header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .timeout(12000)
                    .get();

            String pr = "";
            String name = "";

            Element productTitle = doc.select("span#productTitle").first();
            Element price = doc.select("span#priceblock_ourprice").first();

            if (price != null)
            {
                pr = price.text();
            }

            if (productTitle != null)
            {
                name = productTitle.text();
            }

            gui.ser.InsertPrice(new Product.Builder().withURL(p.getURL()).withProductName(name).withPrice(pr).build());

        } catch (Exception e) {
            gui.errorProductPricing( "Pricing - " + e.getMessage() + " ");
        }

    }

    public void crawlPrice()
    {
        try{
            for (Product p:gui.ser.readProducts())
            {
                crawl(p, gui);
            }
        }catch (Exception e)
        {
            gui.errorProductPricing( "Pricing - " + e.getMessage() + " ");
        }
    }
}
