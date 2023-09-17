package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Data class for initialising the database */
public class Database {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(Database.class);

    /** Path to database.db file */
    private final String databasePath;

    /** Database file location */
    private final String jdbcURL;

    /** Connection to database */
    private Connection connection;

    /** Path to createTables.sql file */
    private final String createTablesSQLPath =
            "database" + File.separator + "sqlScripts" + File.separator + "createTables.sql";

    /** Path to normalise.sql file */
    private final String normaliseSQLPath =
            "database" + File.separator + "sqlScripts" + File.separator + "normalise.sql";

    /**
     * Class initializer. Takes in 3 parameters for each log file, and adds them to the database
     *
     * @param clickLog click_log.csv data
     * @param impressionLog impression_log.csv data
     * @param serverLog server_log.csv data
     * @param databasePath path to the database.db file
     * @throws Exception unable to connect to database
     */
    public Database(String clickLog, String impressionLog, String serverLog, String databasePath)
            throws Exception {
        logger.info("Creating database class and creating a new database");

        // Initialising database paths
        this.databasePath = databasePath;
        this.jdbcURL = "jdbc:sqlite:" + databasePath;

        try {
            recreateDatabase();
            addClickLogTable(clickLog);
            addImpressionTable(impressionLog);
            addServerLogTable(serverLog);
            normaliseTable();

            logger.info("Completed initialisation of the database");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Loads a database from the database path
     *
     * @param databasePath location of the database.db file
     * @throws Exception unable to connect to database
     */
    public Database(String databasePath) throws Exception {
        logger.info("Creating database class and loading database");

        // Initialising database paths
        this.databasePath = databasePath;
        this.jdbcURL = "jdbc:sqlite:" + databasePath;

        try {
            // Connect to the database
            connection = DriverManager.getConnection(jdbcURL);
            connection.setAutoCommit(false);
            logger.info("Connecting to server");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Unable to create a connection to the database file. Please restart program.");
        }
    }

    /**
     * Trys to connect to the database. The database is then deleted, and remade using the
     * createTables.sql script file
     *
     * @throws Exception unable to connect to database
     */
    private void recreateDatabase() throws Exception {
        try {
            File file = new File(databasePath);
            logger.info("Checking if database file already exists");

            if (file.exists()) {
                logger.info("Deleting file");
                file.delete();
            }

            logger.info("Creating new database file");
            file.createNewFile();

            // Connect to the database
            connection = DriverManager.getConnection(jdbcURL);
            connection.setAutoCommit(false);
            logger.info("Connecting to server");

            // Initialize the script runner
            ScriptRunner sr = new ScriptRunner(connection);
            logger.info("Loading script runner");

            // Creating the reader object
            //            InputStream inputStream =
            // getClass().getClassLoader().getResourceAsStream(createTablesSQLPath);
            //            logger.info("Getting resource file createTables.sql");
            //            Reader reader = new InputStreamReader(inputStream);
            //            logger.info("Converting to reader");

            Reader reader = new StringReader(createTablesSql());

            // Run the sql script
            logger.info("Running createTables.sql script");
            sr.runScript(reader);
            logger.info("Completed running createTables.sql");

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Unable to create the database table. Please restart program.");
        }
    }

    /**
     * Inserts the data into the ServerLog table using the data from the server_log.csv file
     *
     * @param fileName File location of the server_log.csv file
     * @throws Exception unable to connect to database
     */
    private void addServerLogTable(String fileName) throws Exception {
        logger.info("Inserting Server Log Data");

        String sqlQuery =
                "INSERT INTO ServerLog(EntryDate, ID, ExitDate, PagesViewed, Conversion) VALUES (?,?,?,?,?)";
        // For each row of data
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            int batchNum = 0;

            logger.info("Opening server_log.csv file");
            // Create a file object
            FileReader file = new FileReader(fileName);
            // Create a buffered reader
            BufferedReader bf = new BufferedReader(file);

            // Skips the first line
            String line = bf.readLine();

            while ((line = bf.readLine()) != null) {
                String[] data = line.split(",");
                // entryDate
                stmt.setTimestamp(1, Timestamp.valueOf(data[0]));

                // ID
                stmt.setString(2, data[1]);

                // exitDate - If it is n/a, skip (have NULL)
                if (!data[2].equals("n/a")) {
                    stmt.setTimestamp(3, Timestamp.valueOf(data[2]));
                }

                // pagesViewed
                stmt.setInt(4, Integer.parseInt(data[3]));

                // conversion - Convert Yes/No to Boolean
                stmt.setBoolean(5, !data[4].equals("No"));

                // Add batch
                stmt.addBatch();
                batchNum++;
                if (batchNum % 1000 == 0) {
                    // Execute batch every 1000
                    stmt.executeBatch();
                }
            }
            file.close();
            bf.close();
            stmt.executeBatch();

            logger.info("Committing changes");
            // Commit changes
            connection.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Server log data may be invalid. Please import data again using a valid file.");
        }
    }

    /**
     * Inserts the data into the ImpressionLog table using the data from the impression_log.csv file
     *
     * @param fileName File location of the impression_log.csv file
     * @throws Exception unable to connect to database
     */
    private void addImpressionTable(String fileName) throws Exception {
        logger.info("Inserting Impression Log data");

        String sqlQuery =
                "INSERT INTO ImpressionLog(Date, ID, Gender, Age, Income, Context, ImpressionCost) VALUES (?,?,?,?,?,?,?)";

        // For each row of data
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);

            logger.info("Opening impression_log.csv");

            // Create a file object
            FileReader file = new FileReader(fileName);
            // Create a buffered reader
            BufferedReader bf = new BufferedReader(file);

            // Skips the first line
            String line = bf.readLine();

            int batchNum = 0;
            while ((line = bf.readLine()) != null) {
                String[] data = line.split(",");
                // date
                stmt.setTimestamp(1, Timestamp.valueOf(data[0]));

                // ID
                stmt.setString(2, data[1]);

                // gender
                stmt.setString(3, data[2]);

                // age
                stmt.setString(4, data[3]);

                // income
                stmt.setString(5, data[4]);

                // context
                stmt.setString(6, data[5]);

                // impressionCost
                stmt.setFloat(7, Float.parseFloat(data[6]));

                // Add batch
                stmt.addBatch();
                batchNum++;
                if (batchNum % 1000 == 0) {
                    // Execute batch every 1000
                    stmt.executeBatch();
                }
            }
            file.close();
            bf.close();
            stmt.executeBatch();

            logger.info("Committing changes");

            // Commit changes
            connection.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Impression log data may be invalid. Please import data again using a valid file.");
        }
    }

    /**
     * Inserts the data into the ImpressionLog table using the data from the click_log.csv file
     *
     * @param fileName File location of the click_log.csv file
     * @throws Exception unable to connect to database
     */
    private void addClickLogTable(String fileName) throws Exception {
        logger.info("Inserting Click Log Data");
        String sqlQuery = "INSERT INTO ClickLog(Date, ID, ClickCost) VALUES (?,?,?)";

        // For each row of data
        try {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);

            int batchNum = 0;

            logger.info("Opening click_log.csv");
            // Create a file object
            FileReader file = new FileReader(fileName);
            // Create a buffered reader
            BufferedReader bf = new BufferedReader(file);

            // Skips the first line
            String line = bf.readLine();

            while ((line = bf.readLine()) != null) {
                String[] data = line.split(",");

                // date
                stmt.setTimestamp(1, Timestamp.valueOf(data[0]));

                // ID
                stmt.setString(2, data[1]);

                // ClickCost
                stmt.setFloat(3, Float.parseFloat(data[2]));

                // Add batch
                stmt.addBatch();
                batchNum++;
                if (batchNum % 1000 == 0) {
                    logger.info("Executing batch");
                    // Execute batch every 1000
                    stmt.executeBatch();
                }
            }
            file.close();
            bf.close();
            stmt.executeBatch();

            // Commit changes
            connection.commit();
            logger.info("Committing changes");

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Click log data may be invalid. Please import data again using a valid file.");
        }
    }

    /**
     * Normalises the database using the normalise.sql script
     *
     * @throws Exception unable to connect to database
     */
    private void normaliseTable() throws Exception {
        logger.info("Normalising database");

        try {
            ScriptRunner sr = new ScriptRunner(connection);

            // Creating the reader object
            //            InputStream inputStream =
            // getClass().getClassLoader().getResourceAsStream(normaliseSQLPath);
            //            Reader reader = new InputStreamReader(inputStream);

            Reader reader = new StringReader(normaliseSQL());

            logger.info("Running normalise.sql script");
            // Run the sql script
            sr.runScript(reader);
            logger.info("Completed running normalise.sql");

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Unable to normalise database using the normalise.sql file. Please restart program.");
        }
    }

    /**
     * Query the database using an SQL statement
     *
     * @param query SQL statement
     * @return result of statement
     * @throws Exception Unable to connect to database
     */
    public ResultSet queryDatabase(String query) throws Exception {
        try {
            // Create a new statement
            Statement stmt = connection.createStatement();

            // Execute query and return the results
            return stmt.executeQuery(query);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Unable to query database. The connection may have reset with the database. Please restart program");
        }
    }

    /**
     * Returns the connection to the database
     *
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Allows a database file to be deleted
     *
     * @throws Exception Unable to connect to database
     */
    public void deleteDatabase() throws Exception {
        try {
            connection.close();
            File file = new File(databasePath);
            logger.info("Checking if database file exists");

            if (file.exists()) {
                logger.info("Deleting file");
                file.delete();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(
                    "Error: Unable to delete database file. Likely the file either does not exist, or the connection has been severed.");
        }
    }

    /**
     * Returns the contents of createTables.sql in the resource file.
     *
     * @return sql query
     */
    public String createTablesSql() {
        return "PRAGMA foreign_keys=ON;\n"
                + "\n"
                + "DROP TABLE IF EXISTS ClickLog;\n"
                + "DROP TABLE IF EXISTS ImpressionLog;\n"
                + "DROP TABLE IF EXISTS ServerLog;\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS ServerLog(\n"
                + "    \"EntryDate\" TIMESTAMP NOT NULL,\n"
                + "    \"ID\" VARCHAR(20) NOT NULL,\n"
                + "    \"ExitDate\" TIMESTAMP,\n"
                + "    \"PagesViewed\" INTEGER NOT NULL,\n"
                + "    \"Conversion\" BOOL NOT NULL\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS ImpressionLog(\n"
                + "    \"Date\" TIMESTAMP NOT NULL,\n"
                + "    \"ID\" VARCHAR(20) NOT NULL,\n"
                + "    \"Gender\" VARCHAR(6) NOT NULL,\n"
                + "    \"Age\" VARCHAR(6) NOT NULL,\n"
                + "    \"Income\" VARCHAR(5) NOT NULL,\n"
                + "    \"Context\" VARCHAR(12) NOT NULL,\n"
                + "    \"ImpressionCost\" FLOAT NOT NULL\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS ClickLog(\n"
                + "    \"Date\" TIMESTAMP NOT NULL,\n"
                + "    \"ID\" VARCHAR(20) NOT NULL,\n"
                + "    \"ClickCost\" FLOAT NOT NULL\n"
                + ");";
    }

    /**
     * Returns the contents of noramlise.sql in the resource file.
     *
     * @return sql query
     */
    private String normaliseSQL() {
        return "DROP TABLE IF EXISTS Impression;\n"
                + "DROP TABLE IF EXISTS Click;\n"
                + "DROP TABLE IF EXISTS Server;\n"
                + "DROP TABLE IF EXISTS User;\n"
                + "\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS Impression(\n"
                + "    \"Date\" TIMESTAMP NOT NULL,\n"
                + "    \"ID\" VARCHAR(20) NOT NULL,\n"
                + "    \"ImpressionCost\" FLOAT NOT NULL,\n"
                + "    \"Gender\" VARCHAR(6) NOT NULL,\n"
                + "    \"Age\" VARCHAR(6) NOT NULL,\n"
                + "    \"Income\" VARCHAR(5) NOT NULL,\n"
                + "    \"Context\" VARCHAR(12) NOT NULL,\n"
                + "    PRIMARY KEY (Date,ID),\n"
                + "    FOREIGN KEY (ID) references User(ID)\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS Click(\n"
                + "    \"Date\" TIMESTAMP NOT NULL,\n"
                + "    \"ID\" VARCHAR(20) NOT NULL,\n"
                + "    \"ClickCost\" FLOAT NOT NULL,\n"
                + "    \"Gender\" VARCHAR(6) NOT NULL,\n"
                + "    \"Age\" VARCHAR(6) NOT NULL,\n"
                + "    \"Income\" VARCHAR(5) NOT NULL,\n"
                + "    \"Context\" VARCHAR(12) NOT NULL,\n"
                + "    PRIMARY KEY (Date,ID),\n"
                + "    FOREIGN KEY (ID) references User(ID)\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS Server(\n"
                + "    \"EntryDate\" TIMESTAMP NOT NULL,\n"
                + "    \"ID\" VARCHAR(20) NOT NULL,\n"
                + "    \"ExitDate\" TIMESTAMP NULL ,\n"
                + "    \"PagesViewed\" INTEGER NOT NULL,\n"
                + "    \"Conversion\" BOOL NOT NULL,\n"
                + "    \"Gender\" VARCHAR(6) NOT NULL,\n"
                + "    \"Age\" VARCHAR(6) NOT NULL,\n"
                + "    \"Income\" VARCHAR(5) NOT NULL,\n"
                + "    \"Context\" VARCHAR(12) NOT NULL,\n"
                + "    PRIMARY KEY (\"EntryDate\",ID, \"ExitDate\"),\n"
                + "    FOREIGN KEY (ID) references User(ID)\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS User(\n"
                + "    \"ID\" VARCHAR(20) NOT NULL ,\n"
                + "    \"Gender\" VARCHAR(6) NOT NULL,\n"
                + "    \"Age\" VARCHAR(6) NOT NULL,\n"
                + "    \"Income\" VARCHAR(5) NOT NULL,\n"
                + "    \"Context\" VARCHAR(12) NOT NULL,\n"
                + "    PRIMARY KEY (ID)\n"
                + ");\n"
                + "\n"
                + "INSERT INTO User SELECT ID, Gender, Age, Income, Context FROM ImpressionLog GROUP BY ID;\n"
                + "INSERT INTO Impression SELECT Date, ID, ImpressionCost, Gender, Age, Income, Context FROM ImpressionLog GROUP BY Date, ID;\n"
                + "INSERT INTO Click SELECT Date, ClickLog.ID, ClickCost, Gender, Age, Income, Context FROM ClickLog INNER JOIN USER ON User.ID=ClickLog.ID;\n"
                + "INSERT INTO Server SELECT EntryDate ,ServerLog.ID ,ExitDate ,PagesViewed ,Conversion, Gender, Age, Income, Context FROM ServerLog INNER JOIN USER ON User.ID=ServerLog.ID;\n"
                + "\n"
                + "DROP TABLE IF EXISTS ImpressionLog;\n"
                + "DROP TABLE IF EXISTS ClickLog;\n"
                + "DROP TABLE IF EXISTS ServerLog;\n"
                + "\n"
                + "-- TODO: Add indexing";
    }
}
