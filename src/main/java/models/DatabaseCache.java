package models;

import java.util.HashMap;
import javax.sql.rowset.CachedRowSet;

/** A class used to cache results from a database */
public class DatabaseCache {

    /** String -> SQL Query
     * CachedRowSet -> Cached ResultSet result from querying database */
    private static HashMap<String, CachedRowSet> resultCache = new HashMap();

    /** Resets the cache with to no values */
    public static void resetCache() {
        resultCache = new HashMap<>();
    }

    /**
     * Adds a filter and a result to the cache
     *
     * @param filter sql filter
     * @param result result of querying database
     */
    public void addToCache(String filter, CachedRowSet result) {
        resultCache.put(filter, result);
    }

    /**
     * Checks to see if a filter has been calcualted before
     *
     * @param filter sql filter
     * @return boolean if it exists
     */
    public boolean containsResult(String filter) {
        return resultCache.containsKey(filter);
    }

    /**
     * Gets the cached result of the filter
     *
     * @param filter sql filter
     * @return result of querying database
     */
    public CachedRowSet getResult(String filter) {
        return resultCache.get(filter);
    }
}
