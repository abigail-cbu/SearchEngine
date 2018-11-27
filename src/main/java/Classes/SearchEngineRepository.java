package Classes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.cj.jdbc.Driver";

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private String dbName = "SearchEngineDB";
    private String dbUserName = "root";
    private String dbPassword = "1111";
    private String _ConnectionString = "jdbc:mysql://localhost/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    //private String _ConnectionString = "jdbc:mysql://localhost/SearchEngineDB";
    Connection conn = null;
    public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);
    public SearchEngineRepository(){
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

            // execute the preparedstatement
           //id = preparedStmt.executeUpdate(Statement.RETURN_GENERATED_KEYS);
            //preparedStmt.execute();


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
            logger.error("error in insertion "+url+" "+ex.getMessage());
        } finally {
//            try {
//                if (conn != null && !conn.isClosed()) {
//                    conn.close();
//                    logger.info("closing connection");
//                }
                return id;
//            } catch (Exception ex) {
//                logger.error(ex.getMessage());
//            }
        }
//        logger.error("error in insertion "+url);
//        return 0;
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
            logger.error(ex.getMessage());
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
            logger.error(ex.getMessage());
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

    public void SetCrawled(String url) {
        //Connection conn = null;
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
            logger.error(ex.getMessage());
        } finally {
           /* try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("closing connection");
                }
            } catch (Exception ex) {*//*ignore*//*}*/
        }
    }
    public void SetLinkCount(String url,int count) {
        //Connection conn = null;
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
            logger.error(ex.getMessage());
        } finally {
           /* try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("closing connection");
                }
            } catch (Exception ex) {*//*ignore*//*}*/
        }
    }

    public void InsertSourceCode(int id, String sourceCode) {
        // insert source code for specified url
       //// Connection conn = null;
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
            logger.error("error in sourceCode insertion "+id+" "+ex.getMessage());
        } finally {
            /*try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("connection close");
                }
            } catch (Exception ex) {*//*ignore*//*}*/
        }
    }

    public Boolean WebsiteExists(String url) {
        // check if url has been crawled
        ArrayList<String> websites = new ArrayList<String>();
//        Connection conn = null;
        try {
         //   logger.info("WebsiteExists for " + url);
//            Class.forName(_myDrive);
//            DriverManager.setLoginTimeout(30);
//            conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);

            //the mysql insert statement
            String query = "SELECT URL"
                    + " FROM Websites " +
                    "WHERE URL = ?";

         //   logger.info("executing WebsiteExists query");
            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, url);

        //    logger.info("getting results");
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                websites.add(rs.getString("URL"));
            }

          //  logger.info("websites found " + websites.size());
//            conn.close();

            if (websites.size() > 0)
                return true;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
//                    conn.close();
        //            logger.info("closed connection");
                }
            } catch (Exception ex) { /*ignore*/}
        }

        return false;
    }

    public int GetDBSize() {
//        Connection conn = null;
        int size = 0;
        try {
         //   logger.info("GetDBSize");
//            Class.forName(_myDrive);
//
//            DriverManager.setLoginTimeout(10);
//            conn = DriverManager.getConnection(_ConnectionString, "root", dbPassword);

           /* String query =
                    "SELECT table_schema SearchEngineDB,"
                            + " ROUND(SUM(data_length + index_length) / 1024 / 1024, 1) \"DB Size in MB\""
                            + " FROM information_schema.tables"
                            + "GROUP BY table_schema;";*/
            String query ="SELECT "+
            "ROUND(SUM(data_length + index_length) / 1024 , 0) "+
            " FROM information_schema.tables "+
            " where table_schema = 'searchenginedb'"+
            " GROUP BY table_schema";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
             //   if (conn != null && !conn.isClosed())
//                    conn.close();
     //           logger.info("closed connection");
            } catch (Exception ex) {
                /*ignore*/
            }
        }

        return size;
    }
    public void closeDB(){
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.info("closed connection");
            }
        } catch (Exception ex) {
            /*ignore*/
        }
    }
}
