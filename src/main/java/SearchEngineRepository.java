

import com.mysql.cj.protocol.Resultset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.jdbc.Driver";

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private String _myUrl = "jdbc:mysql://localhost:3306/SearchEngineDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);

    public void InsertLink(String linkText, String url, int prevLinkID, int depth) {
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "INSERT INTO LINKS (LinkText, URL, PrevLinkID, Crawled, Depth)"
                    + "VALUES (?, ?, ?, ?, ?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, linkText);
            preparedStmt.setString(2, url);
            preparedStmt.setInt(3, prevLinkID);
            preparedStmt.setBoolean(4, false); // should be first time inserting link
            preparedStmt.setInt(5, depth);


            // execute the preparedstatement
            preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void UpdateLink(String url) {
        /// update if we have crawled the url before
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "UPDATE LINKS"
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

    public Boolean IsCrawled(String url) {
        // check if url has been crawled
        Boolean isCrawled = false; // it is okay if we have to crawl a url again
        try {
            Class.forName(_myDrive);

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "SELECT Crawled"
                    + "FROM Links" +
                    "WHERE URL = ?";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
            preparedStmt.setString(1, url);

            // execute the preparedstatement
            Boolean value = preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return isCrawled;
    }

    public void InsertSourceCode(String url) {
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
                    "   FROM Links" +
                    "   WHERE URL = ?" +
                    ")";

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

}
