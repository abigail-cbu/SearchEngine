package Classes;

import com.mysql.cj.protocol.Resultset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.jdbc.Driver";

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private String _myUrl = "jdbc:mysql://localhost:3306/SearchEngineDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);

    public void InsertWebsite(String linkText, String url, int depth) {
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "INSERT INTO Websites (LinkText, URL, Crawled, Depth)"
                    + "VALUES (?, ?, ?, ?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, linkText);
            preparedStmt.setString(2, url);
            preparedStmt.setBoolean(3, false); // should be first time inserting link
            preparedStmt.setInt(4, depth);


            // execute the preparedstatement
            preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void UpdateWebsite(String url) {
        /// update if we have crawled the url before
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "UPDATE Websites"
                    + "SET Crawled = TRUE" +
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
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "SELECT Crawled"
                    + "FROM Websites" +
                    "WHERE URL = ?";

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
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "UPDATE Websites"
                    + "SET Crawled = TRUE" +
                    " WHERE URL= ?";

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

    public void InsertSourceCode(String url, String sourceCode) {
        // insert source code for specified url

        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "UPDATE SourceCodes" +
                    "SET SourceCode = ?" +
                    "WHERE LinkID = " +
                    "(" +
                    "   SELECT LinkID" +
                    "   FROM Websites" +
                    "   WHERE URL = ?" +
                    ")";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, sourceCode);
            preparedStmt.setString(2, url);

            // execute the preparedstatement
            preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public Boolean WebsiteExists(String url) {
        // check if url has been crawled
        ArrayList<String> websites = new ArrayList<String>();
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "SELECT URL"
                    + "FROM Websites" +
                    "WHERE URL = ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, url);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                websites.add(rs.getString("URL"));
            }

            if (websites.size() > 0)
                return true;

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return false;
    }

}
