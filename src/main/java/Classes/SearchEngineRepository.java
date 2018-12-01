package Classes;

import Layout.GUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.cj.jdbc.Driver";

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private String dbName = "SearchEngineDB";
    private String dbUserName = "root";
    private String dbPassword = "1111";
    private String _ConnectionString = "jdbc:mysql://localhost/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&useSSL=false";
    //private String _ConnectionString = "jdbc:mysql://localhost/SearchEngineDB";
    Connection conn = null;
    //public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);
    public GUI gui;
        public SearchEngineRepository(GUI pGUI){
            gui=pGUI;
        try {
            Class.forName(_myDrive);
            conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int InsertWebsite(String siteName, String url, int depth,int prevLink) {

        int id =-1;
        try {
            //   logger.info("InsertWebsite for " + siteName);

            //the mysql insert statement
            String query = "INSERT INTO Websites (SiteName, URL, Crawled, Depth,PrevLinkID)"
                    + "VALUES (?, ?, ?, ?, ?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);;
            preparedStmt.setString(1, siteName);
            preparedStmt.setString(2, url);
            preparedStmt.setBoolean(3, false); // should be first time inserting link
            preparedStmt.setInt(4, depth);
            preparedStmt.setInt(5, prevLink);


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

            // conn.close();


        } catch (Exception ex) {
            gui.error("error in insertion "+url+" "+ex.getMessage());
        } finally {
//
            return id;
//
        }
//
    }

    public void UpdateWebsite(String url) {
        /// update if we have crawled the url before
        try {
            //  logger.info("UpdateWebsite for " + url);
            Class.forName(_myDrive);
            Connection _globalConnectionString = DriverManager.getConnection(_ConnectionString, "root", dbPassword);

            //the mysql insert statement
            String query = "UPDATE Websites"
                    + " SET Crawled = TRUE" +
                    "   WHERE URL = ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, url);

            // execute the preparedstatement
            preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }
    }

    public Boolean CheckIfIsCrawled(String url) {
        // check if url has been crawled
        Boolean isCrawled = false; // it is okay if we have to crawl a url again
        try {

            String query ="SELECT Crawled FROM searchenginedb.websites where URL = ?";

//            PreparedStatement preparedStmt = conn.prepareStatement(query);
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, url);
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                isCrawled = rs.getBoolean(1);
            }
        } catch (Exception ex) {
            gui.error(ex.getMessage());
        } finally {
            try {
                //   if (conn != null && !conn.isClosed())
//                    conn.close();
                //           logger.info("closed connection");
            } catch (Exception ex) {
                /*ignore*/
            }
        }

        return isCrawled;
    }

    public List<Website> ReadNotCrawled() {
        List<Website> l = new ArrayList<>();
        try {

            String query ="SELECT LinkID,SiteName,URL,LinkCount,PrevLinkID,Crawled,Depth FROM searchenginedb.websites where Crawled = 0;";


            PreparedStatement preparedStmt = conn.prepareStatement(query);
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                l.add(new Website(rs.getString("SiteName"),rs.getString("URL"),rs.getInt("Depth"),rs.getInt("PrevLinkID"),rs.getInt("LinkID")));

            }
        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }

        return l;
    }

    public void SetCrawled(String url) {

        try {

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
        }
    }
    public void SetLinkCount(String url,int count) {

        try {
            String query = "UPDATE Websites"
                    + " SET LinkCount = ?" +
                    " WHERE URL= ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, count);
            preparedStmt.setString(2, url);

            // execute the preparedstatement
            preparedStmt.execute();


        } catch (Exception ex) {
            gui.error(ex.getMessage());

        }
    }

    public void InsertSourceCode(int id, String sourceCode) {

        try {

            String query = "INSERT INTO sourcecodes (LinkID, SourceCode)"
                    + "VALUES (?, ?)";
            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, sourceCode);

            // execute the preparedstatement
            preparedStmt.execute();

        } catch (Exception ex) {
            gui.error("error in sourceCode insertion "+id+" "+ex.getMessage());
        }
    }

    public Boolean WebsiteExists(String url,int pDepth) {
        // check if url has been crawled
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
        }

        return false;
    }

    public int GetDBSize() {
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
        }

        return size;
    }
    public void closeDB(){
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                gui.info("closed connection");
            }
        } catch (Exception ex) {
            /*ignore*/
        }
    }

    public void getSummary(){
            String summary = "";
        try {
            String query ="SELECT count(*) FROM searchenginedb.websites";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            ResultSet rs = preparedStmt.executeQuery();

            while (rs.next()) {
                summary+= "Number of unique domains crawled: "+  rs.getInt(1);
            }
        } catch (Exception ex) {
            gui.error(ex.getMessage());
        }

    }
}
