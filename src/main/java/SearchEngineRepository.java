

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.jdbc.Driver";  //insert this

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password" //insert this
    private String _myUrl = "jdbc:mysql://localhost:3306/SearchEngineDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; //insert this

    public static final Logger logger = LogManager.getLogger(SearchEngineRepository.class);

    public void InsertLink(String linkText, String url, int prevLinkID) {
        try {
            Class.forName(_myDrive); //insert this

            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", ""); //insert this
            logger.info("Connected to SearchEngineDB");

            //the mysql insert statement
            String query = "INSERT INTO LINKS (LinkText, URL, PrevLinkID)"
                    + "VALUES (?, ?, ?)";

            // create the mysql insert and add parameters
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query); //prob use get/read
            preparedStmt.setString(1, linkText);
            preparedStmt.setString(2, url);
            preparedStmt.setInt(3, prevLinkID);

            // execute the preparedstatement
            preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
