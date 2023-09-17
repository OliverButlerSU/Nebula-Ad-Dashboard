package models;

import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Used to calculate and store a set of data such as impressions, clicks etc. */
public class Data {
    /** Logger */
    private static final Logger logger = LogManager.getLogger(Data.class);

    private int impressions = 0;
    private int clicks = 0;
    private int uniques = 0;
    private int bounces = 0;
    private int conversions = 0;
    private float totalCost = 0;
    private float CTR = 0;
    private float CPA = 0;
    private float CPC = 0;
    private float CPM = 0;
    private float bounceRate = 0;

    /**
     * Calculates all the data variables in the class
     *
     * @param data List of SQL Query Results to calculate each value
     */
    public void calculateData(ResultSet[] data) {
        try {
            logger.info("Calculating data");
            calculateImpressions(data[0]);
            calculateClicks(data[1]);
            calculateUniques(data[2]);
            calculateBounces(data[3]);
            calculateConversions(data[4]);
            calculateTotalCost(data[5], data[6]);
            calculateCTR();
            calculateCPA();
            calculateCPC();
            calculateCPM();
            calculateBounceRate();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Calculates the total impressions
     *
     * @param data SQL query result
     */
    private void calculateImpressions(ResultSet data) {
        try {
            int amount = data.getInt(1);
            if (amount >= 0) {
                this.impressions = amount;
                return;
            }
            this.impressions = 0;

        } catch (Exception e) {
            logger.error(e.getMessage());
            this.impressions = 0;
        }
    }

    /**
     * Calculates the total clicks
     *
     * @param data SQL query result
     */
    private void calculateClicks(ResultSet data) {
        try {
            int amount = data.getInt(1);
            if (amount >= 0) {
                this.clicks = amount;
                return;
            }
            this.clicks = 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.clicks = 0;
        }
    }

    /**
     * Calculates the total uniques
     *
     * @param data SQL query result
     */
    private void calculateUniques(ResultSet data) {
        try {
            int amount = data.getInt(1);
            if (amount >= 0) {
                this.uniques = amount;
                return;
            }
            this.uniques = 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.uniques = 0;
        }
    }

    /**
     * Calculates the total bounces
     *
     * @param data SQL query result
     */
    private void calculateBounces(ResultSet data) {
        try {
            int amount = data.getInt(1);
            if (amount >= 0) {
                this.bounces = amount;
                return;
            }
            this.bounces = 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.bounces = 0;
        }
    }

    /**
     * Calculates the total conversions
     *
     * @param data SQL query result
     */
    private void calculateConversions(ResultSet data) {
        try {
            int amount = data.getInt(1);
            if (amount >= 0) {
                this.conversions = amount;
                return;
            }
            this.conversions = 0;
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.conversions = 0;
        }
    }

    /**
     * Calculates the total cost
     *
     * @param impressionCost SQL query result for impression cost
     * @param clickCost SQL query result for click cost
     */
    private void calculateTotalCost(ResultSet impressionCost, ResultSet clickCost) {
        try {
            Float impressionC = impressionCost.getFloat(1);
            Float clickC = clickCost.getFloat(1);

            this.totalCost = (impressionC + clickC) / 100;
        } catch (Exception e) {
            logger.error(e.getMessage());
            this.totalCost = 0;
        }
    }

    /** Calculates the CTR */
    private void calculateCTR() {
        if (impressions == 0) {
            this.CTR = 0;
            return;
        }

        this.CTR = clicks / (float) impressions;
    }

    /** Calculates the CPA */
    private void calculateCPA() {
        if (conversions == 0) {
            this.CPA = 0;
            return;
        }
        this.CPA = totalCost / (float) conversions;
    }

    /** Calculates the CPC */
    private void calculateCPC() {
        if (clicks == 0) {
            this.CPC = 0;
            return;
        }
        this.CPC = totalCost / (float) clicks;
    }

    /** Calculates the CPM */
    private void calculateCPM() {
        if (impressions == 0) {
            this.CPC = 0;
            return;
        }
        this.CPM = (totalCost / (float) impressions) * 1000f;
    }

    /** Calculates the bounce rate */
    private void calculateBounceRate() {
        if (clicks == 0) {
            this.CPC = 0;
            return;
        }

        this.bounceRate = bounces / (float) clicks * 100;
    }

    /**
     * Groups all the variables into one array
     *
     * @return data variables
     */
    public Object[] getData() {
        Object[] data = new Object[11];
        data[0] = impressions;
        data[1] = clicks;
        data[2] = uniques;
        data[3] = bounces;
        data[4] = conversions;
        data[5] = totalCost;
        data[6] = CTR;
        data[7] = CPA;
        data[8] = CPC;
        data[9] = CPM;
        data[10] = bounceRate;
        return data;
    }

    public float getBounceRate() {
        return bounceRate;
    }

    public float getCPM() {
        return CPM;
    }

    public float getCPC() {
        return CPC;
    }

    public float getCPA() {
        return CPA;
    }

    public float getCTR() {
        return CTR;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public int getConversions() {
        return conversions;
    }

    public int getBounces() {
        return bounces;
    }

    public int getUniques() {
        return uniques;
    }

    public int getClicks() {
        return clicks;
    }

    public int getImpressions() {
        return impressions;
    }
}
