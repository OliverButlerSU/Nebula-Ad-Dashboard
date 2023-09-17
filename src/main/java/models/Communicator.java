package models;

import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Communicator class for allowing the filter, data, database and chart model classes to communicate
 * with each other
 */
public class Communicator {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(Database.class);

    /** Used to create filters */
    private Filter filter = new Filter();

    /** Stores and manipulates the ad data */
    private Data data = new Data();

    /** Stores the csv files in a database */
    private Database database;

    /** Used to show data in a model view */
    private ChartModel chartModel;

    private static DatabaseCache databaseCache = new DatabaseCache();

    /**
     * Creates the communicator class, used to allow the Filter, Data, Database and ChartModel class
     * to communicate with each other
     *
     * @param databasePath path to database file
     * @throws Exception Unable to connect to database
     */
    public Communicator(String databasePath) throws Exception {
        database = new Database(databasePath);
        chartModel = new ChartModel("Not yet loaded", "", "");
    }

    /**
     * Filters database to calculate the list of data
     *
     * @return list of data
     * @throws Exception Unable to query database
     */
    public Object[] filterDatabase() throws Exception {
        String[] filters = filter.runAllDataFilters();
        ResultSet[] resultSet = new ResultSet[filters.length];
        logger.info("Filtering database");
        for (int i = 0; i < filters.length; i++) {
            String filter = filters[i];

            if (databaseCache.containsResult(filter)) {
                // resultSet[i] = databaseCache.getResult(filter).createCopy();
                resultSet[i] = database.queryDatabase(filter);

            } else {
                ResultSet rs = database.queryDatabase(filter);

                //                CachedRowSet crw =
                // RowSetProvider.newFactory().createCachedRowSet();
                //                crw.populate(rs);
                //                databaseCache.addToCache(filter,crw.createCopy());
                //                resultSet[i] = crw;
                resultSet[i] = rs;
            }
        }
        data.calculateData(resultSet);
        return data.getData();
    }

    /**
     * Querys the database.db file using an sql query
     *
     * @param query sql query
     * @return ResultSet for result
     * @throws Exception Unable to query file
     */
    public ResultSet queryDatabase(String query) throws Exception {
        if (databaseCache.containsResult(query)) {
            return databaseCache.getResult(query).createCopy();
        }

        ResultSet rs = database.queryDatabase(query);
        CachedRowSet crw = RowSetProvider.newFactory().createCachedRowSet();
        crw.populate(rs);
        databaseCache.addToCache(query, crw.createCopy());
        return crw;
    }

    /** Disconnects from the database and resets cache */
    public void disconnect() {
        database.disconnect();
        DatabaseCache.resetCache();
    }

    /**
     * Updates the chart model with a new title, x axis and y axis label
     *
     * @param title Title of the chart
     * @param xLabel X axis label of the chart
     * @param yLabel Y axis label of the chart
     */
    public void updateChartModel(String title, String xLabel, String yLabel) {
        chartModel = new ChartModel(title, xLabel, yLabel);
    }

    public Filter getFilter() {
        return filter;
    }

    public ChartModel getChartModel() {
        return chartModel;
    }

    public Object[] getData() {
        return data.getData();
    }
}
