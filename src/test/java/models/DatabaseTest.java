package models;

import java.io.File;
import java.sql.ResultSet;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DatabaseTest extends TestCase {

    /** Path to src/test/resources/database/ */
    private final String testDatabasePath =
            "src"
                    + File.separator
                    + "test"
                    + File.separator
                    + "resources"
                    + File.separator
                    + "database"
                    + File.separator;

    /** Path to folder locating the databases */
    private final String databasesPath = testDatabasePath + "databases" + File.separator;

    /** Path to invalid data folder */
    private final String invalidTestDataPath =
            testDatabasePath + "testData" + File.separator + "invalidLogs" + File.separator;

    /** Path to valid data folder */
    private final String validTestDataPath =
            testDatabasePath + "testData" + File.separator + "validLogs" + File.separator;

    /** Paths to invalid csv files */
    private final String invalidClickPath = invalidTestDataPath + "click_log.csv";
    private final String invalidImpressionPath = invalidTestDataPath + "impression_log.csv";
    private final String invalidServerPath = invalidTestDataPath + "server_log.csv";

    /** Paths to valid csv files */
    private final String validClickPath = validTestDataPath + "click_log.csv";
    private final String validImpressionPath = validTestDataPath + "impression_log.csv";
    private final String validServerPath = validTestDataPath + "server_log.csv";

    /** Tests the database class by using invalid data in the click_log.csv file */
    @Test
    public void Invalid_Click_CSV_File_Data() {
        Assertions.assertThrows(Exception.class, () -> new Database(
                invalidClickPath,
                validImpressionPath,
                validServerPath,
                databasesPath + "database1.db"));

    }

    /** Tests the database class by using an invalid location for the click_log.csv file */
    @Test
    public void Invalid_Click_CSV_File_Location() {
        Assertions.assertThrows(Exception.class, () -> new Database(
                        validTestDataPath + "notAFile.csv",
                        validServerPath,
                        validImpressionPath,
                        databasesPath + "database2.db"));
    }

    /** Tests the database class by using invalid data in the impression_log.csv file */
    @Test
    public void Invalid_Impression_CSV_File_Data() {
        Assertions.assertThrows(Exception.class, () -> new Database(
                        validClickPath,
                        invalidImpressionPath,
                        validServerPath,
                        databasesPath + "database3.db"));
    }

    /** Tests the database class by using an invalid location for the impression_log.csv file */
    @Test
    public void Invalid_Impression_CSV_File_Location() {
        Assertions.assertThrows(Exception.class, () -> new Database(
                        validClickPath,
                        validTestDataPath + "notAFile.csv",
                        validServerPath,
                        databasesPath + "database4.db"));
    }

    /** Tests the database class by using invalid data in the server_log.csv file */
    @Test
    public void Invalid_Server_CSV_File_Data() {
        Assertions.assertThrows(Exception.class, () -> new Database(
                validClickPath,
                validImpressionPath,
                invalidServerPath,
                databasesPath + "database5.db"));

    }

    /** Tests the database class by using an invalid location for the server_log.csv file */
    @Test
    public void Invalid_Server_CSV_File_Location() {
        Assertions.assertThrows(Exception.class, () -> new Database(
                        validClickPath,
                        validImpressionPath,
                        validTestDataPath + "notAFile.csv",
                        databasesPath + "database6.db"));
    }

    /**
     * Tests the database class by using valid data for each csv and checking there is the correct
     * amount of data in the Click Table
     */
    @Test
    public void All_Valid_Data_Query_Click_Count() {
        try {
            Database db = new Database(databasesPath + "database7.db");
            ResultSet rs = db.queryDatabase("SELECT Count(*) FROM Click;");

            Assertions.assertEquals(rs.getInt(1), 10);
        } catch (Exception e) {
            Assertions.fail("Could not query database");
        }
    }

    /**
     * Tests the database class by using valid data for each csv and checking there is the correct
     * amount of data in the Impression Table
     */
    @Test
    public void All_Valid_Data_Query_Impression_Count() {
        try {
            Database db = new Database(databasesPath + "database8.db");
            ResultSet rs = db.queryDatabase("SELECT Count(*) FROM Impression;");
            Assertions.assertEquals(rs.getInt(1), 10);
        } catch (Exception e) {
            Assertions.fail("Could not query database");
        }
    }

    /**
     * Tests the database class by using valid data for each csv and checking there is the correct
     * amount of data in the Server Table
     */
    @Test
    public void All_Valid_Data_Query_Server_Count() {
        try {
            Database db = new Database(databasesPath + "database9.db");
            ResultSet rs = db.queryDatabase("SELECT Count(*) FROM Server");
            Assertions.assertEquals(rs.getInt(1), 11);
        } catch (Exception e) {
            Assertions.fail("Could not query database");
        }
    }

    /**
     * Tests the database class by using valid data for each csv and checking there is the correct
     * amount of data in the User Table
     */
    @Test
    public void All_Valid_Data_Query_User_Count() {
        try {
            Database db = new Database(databasesPath + "database10.db");
            ResultSet rs = db.queryDatabase("SELECT Count(*) FROM SERVER");
            Assertions.assertEquals(rs.getInt(1), 11);
        } catch (Exception e) {
            Assertions.fail("Could not query database");
        }
    }
}
