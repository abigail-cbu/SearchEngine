package Classes;

import Layout.GUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.cj.jdbc.Driver";

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private String dbName;
    private String dbUserName;
    private String dbPassword;
    private String _ConnectionString;
    //private String _ConnectionString = "jdbc:mysql://localhost/SearchEngineDB";
    Connection conn = null;
    //public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);
    public GUI gui;
    public SearchEngineRepository(GUI pGUI){
        gui=pGUI;
        dbName = gui.getDBName();
        dbUserName = gui.getDBUser();
        dbPassword = gui.getDBPass();
        _ConnectionString = gui.getDBAddress() + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&useSSL=false";

        try {
            Class.forName(_myDrive);
            conn = DriverManager.getConnection(_ConnectionString, dbUserName, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void openConnection(){
        try {
            Class.forName(_myDrive);
            conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int InsertWebsite(String siteName, String url, int depth,int prevLink,int pParentID) {
       // openConnection();
        int id =-1;
        try {
            //   logger.info("InsertWebsite for " + siteName);

            //the mysql insert statement
            String query = "INSERT INTO Websites (SiteName, URL, Crawled, Depth,PrevLinkID,SeedID)"
                    + "VALUES (?, ?, ?, ?, ?,?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, siteName);
            preparedStmt.setString(2, url);
            preparedStmt.setBoolean(3, false); // should be first time inserting link
            preparedStmt.setInt(4, depth);
            preparedStmt.setInt(5, prevLink);
            preparedStmt.setInt(6, pParentID);


            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id=(int)(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (Exception ex) {
            gui.error("error in insertion "+url+" "+ex.getMessage());
        }finally {
           // closeDB();
        }
        return id;

    }


    public List<Website> ReadNotCrawled() {
       //openConnection();
        List<Website> l = new ArrayList<>();
        try {

            String query ="SELECT LinkID,SiteName,URL,LinkCount,PrevLinkID,Crawled,Depth,SeedID FROM searchenginedb.websites where Crawled = 0;";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                l.add(new Website.Builder().withSiteName(rs.getString("SiteName")).withURL(rs.getString("URL"))
                .withDepth(rs.getInt("Depth")).withPrevID(rs.getInt("PrevLinkID")).withLinkID(rs.getInt("LinkID"))
                .withParentID(rs.getInt("SeedID")).build());
            }
        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }finally {
           // closeDB();
        }

        return l;
    }

    public void SetCrawled(String url) {
       // openConnection();
        try {
           /* Class.forName(_myDrive);

            conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);
            logger.info("Connected to SearchEngineDB");*/

            //the mysql insert statement
            String query = "UPDATE Websites"
                    + " SET Crawled = TRUE" +
                    " WHERE URL= ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, url);

            // execute the preparedstatement
            preparedStmt.execute();

            // conn.close();

        } catch (Exception ex) {
            gui.error(ex.getMessage());
        } finally {
            //closeDB();
        }
    }
    public void SetLinkCount(String url,int count) {
       // openConnection();
        try {
           /* Class.forName(_myDrive);

            conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);
            logger.info("Connected to SearchEngineDB");*/

            //the mysql insert statement
            String query = "UPDATE Websites"
                    + " SET LinkCount = ?" +
                    " WHERE URL= ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, count);
            preparedStmt.setString(2, url);

            // execute the preparedstatement
            preparedStmt.execute();

            // conn.close();

        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }finally {
          //  closeDB();
        }
    }

    public void InsertSourceCode(int id, String sourceCode) {
        //openConnection();
        try {
            //  Class.forName(_myDrive);
            //     conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);
            //   logger.debug("InsertSourceCode");

            //the mysql insert statement
           /* String query = "UPDATE SourceCodes" +
                    " SET SourceCode = ?" +
                    " WHERE LinkID = " +
                    "(" +
                    "   SELECT LinkID" +
                    "   FROM Websites" +
                    "   WHERE URL = ?" +
                    ")";
*/
            String query = "INSERT INTO sourcecodes (LinkID, SourceCode)"
                    + "VALUES (?, ?)";
            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, sourceCode);

            // execute the preparedstatement
            preparedStmt.execute();

//            conn.close();
        } catch (Exception ex) {
            gui.error("error in sourceCode insertion "+id+" "+ex.getMessage());
        } finally {
           // closeDB();
        }
    }

    public Boolean WebsiteExists(String url,int pDepth) {
       // openConnection();
        ArrayList<String> websites = new ArrayList<String>();

        try {

            String query = "SELECT URL"
                    + " FROM Websites " +
                    "WHERE Depth <=? and URL = ?";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, pDepth);
            preparedStmt.setString(2, url);

            ResultSet rs = preparedStmt.executeQuery();


            while (rs.next()) {
                websites.add(rs.getString("URL"));
            }

            if (websites.size() > 0)
                return true;

        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }finally {
          //  closeDB();
        }

        return false;
    }

    public int getDBSize() {
       // openConnection();
        int size = 0;
        try {
            String query ="SELECT "+
                    "ROUND(SUM(data_length + index_length) / 1024 , 0) "+
                    " FROM information_schema.tables "+
                    " where table_schema = 'searchenginedb'"+
                    " GROUP BY table_schema";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }finally {
          //closeDB();
        }

        return size;
    }
    public void closeDB(){
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                //gui.info("closed connection");
                System.out.println("DataBase Is Closed...");
            }
        } catch (Exception ex) {
            /*ignore*/
        }
    }

    public String getSummary(){
        //openConnection();
        String summary = "";
        try {
            String query ="SELECT count(*) FROM searchenginedb.websites";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                summary+= "Number of unique domains crawled: "+  rs.getInt(1)+"\n";
            }
             query ="SELECT count(*) c ,w.Sitename from searchenginedb.websites w" +
                     "            inner join searchenginedb.websites w2 on w2.seedid = w.linkid group by w.Sitename" ;

            preparedStmt = conn.prepareStatement(query);

             rs = preparedStmt.executeQuery();

            while (rs.next()) {
                summary+= "Number of unique domains initiated from "+  rs.getString("Sitename") +" is "+rs.getInt("c")+"\n";
            }

        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }finally {
          //  closeDB();
        }
        return summary;
    }

    public int InsertPrice(Product product) {
       //  openConnection();
        int id =-1;
        try {
            //   logger.info("InsertWebsite for " + siteName);

            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            String currentTime = sdf.format(dt);
            //the mysql insert statement
            String query = "INSERT INTO searchenginedb.prices(URL,date_time,price,productName) "
                    + "VALUES (?, ?, ?, ?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);;
            preparedStmt.setString(1, product.getURL());
            preparedStmt.setString(2, currentTime);
            preparedStmt.setString(3, product.getPrice());
            preparedStmt.setString(4, product.getProductName());


            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id=(int)(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }

        } catch (Exception ex) {
            gui.error("error in insertion "+ product.getURL()+" "+ex.getMessage());
        }
        finally {
          //  closeDB();
        }
        return id;

    }
    public List<String> readProductsName() {
        //openConnection();
        List<String> productName = new ArrayList<>();
        try {

            String query ="SELECT distinct productName FROM searchenginedb.prices";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                productName.add(rs.getString("productName"));

            }
        } catch (Exception ex) {
            gui.errorProductPricing(ex.getMessage());
        }finally {
          // closeDB();
        }

        return productName;
    }
    public List<Product> readProducts() {
        //openConnection();
        List<Product> products = new ArrayList<>();
        try {

            String query ="SELECT distinct productName,URL FROM searchenginedb.prices";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Product p = new Product.Builder().withProductName(rs.getString("productName"))
                        .withURL(rs.getString("URL")).build();
                products.add(p);

            }
        } catch (Exception ex) {
            gui.errorProductPricing(ex.getMessage());
        }finally {
         //  closeDB();
        }

        return products;
    }

    public String readPrices(String pProductName) {
       //openConnection();
        String print = "";
        try {

            String query ="SELECT  URL,date_time,price FROM searchenginedb.prices where productName = ?";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, pProductName);

            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                print+=rs.getString("date_time")+": "+rs.getString("price")+
                        ": "+rs.getString("URL")+"\n";
            }
        } catch (Exception ex) {
            gui.errorProductPricing(ex.getMessage());
        }finally {
        //    closeDB();
        }

        return print;
    }
    public String deleteProduct(String pProductName) {
      // openConnection();
        try {

            String query ="DELETE FROM searchenginedb.prices where productName = ?;";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, pProductName);

            preparedStmt.execute();


        } catch (Exception ex) {
            gui.errorProductPricing(ex.getMessage());
        }finally {
        //   closeDB();
        }

        return pProductName+" Deleted\n";
    }
}
