import java.sql.*;

public class SearchEngineRepository {

    private String _myDrive = "com.mysql.jdbc.Driver";

    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private String _myUrl = "jdbc:mysql://localhost:3306/SearchEngineDB";

    public void InsertLink(String linkText, String url, int prevLinkID) {
        try {
            //Class.forName(_myDrive);
            Connection _globalConnectionString = DriverManager.getConnection(_myUrl, "root", "");

            // the mysql insert statement
//            String query = "INSERT INTO LINKS (LinkText, URL, PrevLinkID)"
//                    + "VALUES (?, ?, ?)";
//
//            // create the mysql insert and add parameters
//            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);
//            preparedStmt.setString(1, linkText);
//            preparedStmt.setString(2, url);
//            preparedStmt.setInt(3, prevLinkID);

            String query = "select * from Links";
            PreparedStatement preparedStmt = _globalConnectionString.prepareStatement(query);

            // execute the preparedstatement
            preparedStmt.execute();

            _globalConnectionString.close();

        } catch (Exception ex) {
            System.err.println("Got an exception!");
            System.err.println(ex.getCause() + " " + ex.getMessage());
        }
    }
}
