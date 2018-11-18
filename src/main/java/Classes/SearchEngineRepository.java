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
    private String dbPassword = "";
    private String _ConnectionString = "jdbc:mysql://localhost/" + dbName + "?user=" + dbUserName + "&password=" + dbPassword + "&useUnicode=true&characterEncoding=UTF-8";
    //private String _ConnectionString = "jdbc:mysql://localhost/SearchEngineDB";

    public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);

    public void InsertWebsite(String siteName, String url, int depth) {
        Connection conn = null;
        try {
            logger.info("InsertWebsite for " + siteName);
            Class.forName(_myDrive);
            conn = DriverManager.getConnection(_ConnectionString, "root", "");

            //the mysql insert statement
            String query = "INSERT INTO Websites (SiteName, URL, Crawled, Depth)"
                    + "VALUES (?, ?, ?, ?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, siteName);
            preparedStmt.setString(2, url);
            preparedStmt.setBoolean(3, false); // should be first time inserting link
            preparedStmt.setInt(4, depth);

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("closing connection");
                }
            } catch (Exception ex) {/*ignore*/}
        }
    }

    public void UpdateWebsite(String url) {
        /// update if we have crawled the url before
        try {
            logger.info("UpdateWebsite for " + url);
            Class.forName(_myDrive);
            Connection _globalConnectionString = DriverManager.getConnection(_ConnectionString, "root", "");

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
            logger.info("Connected to SearchEngineDB");
            Class.forName(_myDrive);
            Connection _globalConnectionString = DriverManager.getConnection(_ConnectionString, "root", "");

            //the mysql insert statement
            String query = "SELECT Crawled"
                    + " FROM Websites" +
                    " WHERE URL = ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, url);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                isCrawled = rs.getBoolean("Crawled");
            }

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return isCrawled;
    }

    public void SetCrawled(String url) {
        Connection conn = null;
        try {
            Class.forName(_myDrive);

            conn = DriverManager.getConnection(_ConnectionString, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "UPDATE Websites"
                    + " SET Crawled = TRUE" +
                    " WHERE URL= ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, url);

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("closing connection");
                }
            } catch (Exception ex) {/*ignore*/}
        }
    }

    public void InsertSourceCode(String url, String sourceCode) {
        // insert source code for specified url
        Connection conn = null;
        try {
            Class.forName(_myDrive);
            conn = DriverManager.getConnection(_ConnectionString, "root", "");
            logger.info("InsertSourceCode");

            //the mysql insert statement
            String query = "UPDATE SourceCodes" +
                    " SET SourceCode = ?" +
                    " WHERE LinkID = " +
                    "(" +
                    "   SELECT LinkID" +
                    "   FROM Websites" +
                    "   WHERE URL = ?" +
                    ")";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, sourceCode);
            preparedStmt.setString(2, url);

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("connection close");
                }
            } catch (Exception ex) {/*ignore*/}
        }
    }

    public Boolean WebsiteExists(String url) {
        // check if url has been crawled
        ArrayList<String> websites = new ArrayList<String>();
        Connection conn = null;
        try {
            logger.info("WebsiteExists for " + url);
            Class.forName(_myDrive);
            DriverManager.setLoginTimeout(30);
            conn = DriverManager.getConnection(_ConnectionString, "root", "");

            //the mysql insert statement
            String query = "SELECT URL"
                    + " FROM Websites " +
                    "WHERE URL = ?";

            logger.info("executing WebsiteExists query");
            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, url);

            logger.info("getting results");
            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                websites.add(rs.getString("URL"));
            }

            logger.info("websites found " + websites.size());
            conn.close();

            if (websites.size() > 0)
                return true;

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    logger.info("closed connection");
                }
            } catch (Exception ex) { /*ignore*/}
        }

        return false;
    }

    public int GetDBSize() {
        Connection conn = null;
        int size = 0;
        try {
            logger.info("GetDBSize");
            Class.forName(_myDrive);

            DriverManager.setLoginTimeout(10);
            conn = DriverManager.getConnection(_ConnectionString, "root", "");

            String query =
                    "SELECT table_schema SearchEngineDB,"
                            + " ROUND(SUM(data_length + index_length) / 1024 / 1024, 1) \"DB Size in MB\""
                            + " FROM information_schema.tables"
                            + "GROUP BY table_schema;";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                size = rs.getInt(0);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed())
                    conn.close();
                logger.info("closed connection");
            } catch (Exception ex) {
                /*ignore*/
            }
        }

        return size;
    }
}
