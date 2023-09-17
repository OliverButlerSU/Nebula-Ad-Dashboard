package models;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class used for creating the SQL queries and applying filters
 */
public class Filter {

    /** Logger */
    private static final Logger logger = LogManager.getLogger(Filter.class);

    /**
     * The beginning date to filter by
     */
    private String dateStart;

    /**
     * The end date to filter by
     */
    private String dateEnd;

    /**
     * The time granularity to filter by in the form %(time), e.g. %d for day
     */
    private String timeGranularity;

    /**
     * The age of the User to filter by
     */
    private String ageFilter;

    /**
     * The context of the User to filter by
     */
    private String contextFilter;

    /**
     * The gender of the User to filter by
     */
    private String genderFilter;

    /**
     * The income of the User to filter by
     */
    private String incomeFilter;

    /**
     * Initializer for the filter class, sets the initial filter data
     */
    public Filter(){
        logger.info("Initialising filter data");
        this.dateStart = "1970-01-01";
        this.dateEnd = "3000-01-14";
        setTimeGranularity("%d");
        setAgeFilter(new String[] {});
        setIncomeFilter(new String[] {});
        setGenderFilter(new String[] {});
        setContextFilter(new String[] {});
//		setAgeFilter(new String[] {"<25", "25-34", "35-44", "45-54", ">54"});
//		setIncomeFilter(new String[] {"High", "Medium", "Low"});
//		setGenderFilter(new String[] {"Male", "Female"});
//		setContextFilter(new String[] {"News", "Shopping", "Social Media", "Blog", "Hobbies", "Travel"});
    }


    /**
     * Creates all sql querys for calculating data
     * @return List of sql queries
     */
    public String[] runAllDataFilters(){
        ArrayList<String> filters = new ArrayList<>();

        filters.add(createImpressionsData());
        filters.add(createClicksData());
        filters.add(createUniquesData());
        filters.add(createBouncesData());
        filters.add(createConversionsData());
        filters.add(createTotalCostImpressionData());
        filters.add(createTotalCostClickData());

        return filters.toArray(new String[0]);
    }

    /**
     * Returns an SQL query for selecting the total impressions over time in the form of graph data
     * @return SQL Query
     */
    public String createImpressionsGraph(){
        String query =
                "Select count(Impression.ID) as data, strftime('" + timeGranularity + "', DATETIME(Impression.Date/1000, 'unixepoch')) as time "
                        + "From Impression "
                        + "Where strftime('%Y-%m-%d', DATETIME(Impression.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total impressions over time used for data
     * @return SQL Query
     */
    public String createImpressionsData(){
        String query =
                "Select count(Impression.ID) as data "
                        + "From Impression "
                        + "Where strftime('%Y-%m-%d', DATETIME(Impression.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + ";";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total clicks over time in the form of graph data
     * @return SQL Query
     */
    public String createClicksGraph(){
        String query =
                "Select count(Click.ID) as data, strftime('" + timeGranularity + "', DATETIME(Click.Date/1000, 'unixepoch')) as time "
                        + "From Click "
                        + "Where strftime('%Y-%m-%d', DATETIME(Click.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total clicks over time used for data
     * @return SQL Query
     */
    public String createClicksData(){
        String query =
                "Select count(Click.ID) as data "
                        + "From Click "
                        + "Where strftime('%Y-%m-%d', DATETIME(Click.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + ";";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total uniques over time in the form of graph data
     * @return SQL Query
     */
    public String createUniquesGraph(){
        String query =
                "Select count(distinct(Click.ID)) as data, strftime('" + timeGranularity + "', DATETIME(Click.Date/1000, 'unixepoch')) as time "
                        + "From Click "
                        + "Where strftime('%Y-%m-%d', DATETIME(Click.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total uniques over time used for data
     * @return SQL Query
     */
    public String createUniquesData(){
        String query =
                "Select count(distinct(Click.ID)) as data  "
                        + "From Click "
                        + "Where strftime('%Y-%m-%d', DATETIME(Click.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + ";";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total bounces over time in the form of graph data
     * @return SQL Query
     */
    public String createBouncesGraph(){
        String query =
                "Select count(Server.ID) as data, strftime('" + timeGranularity + "', DATETIME(Server.EntryDate/1000, 'unixepoch')) as time "
                        + "From Server "
                        + "Where strftime('%Y-%m-%d', DATETIME(Server.EntryDate/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "AND PagesViewed = 1 "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    //TODO: NOT RIGHT
    /**
     * Returns an SQL query for selecting the total bounces over time used for data
     * @return SQL Query
     */
    public String createBouncesData(){
        String query =
                "Select count(Server.ID) as data "
                        + "From Server "
                        + "Where strftime('%Y-%m-%d', DATETIME(Server.EntryDate/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "AND PagesViewed = 1;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total conversions over time in the form of graph data
     * @return SQL Query
     */
    public String createConversionsGraph(){
        String query =
                "Select count(Server.Conversion) as data, strftime('" + timeGranularity + "', DATETIME(Server.EntryDate/1000, 'unixepoch')) as time "
                        + "From Server "
                        + "Where strftime('%Y-%m-%d', DATETIME(Server.EntryDate/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "AND Conversion = 1 "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total conversions over time used for data
     * @return SQL Query
     */
    public String createConversionsData(){
        String query =
                "Select count(Server.Conversion) as data "
                        + "From Server "
                        + "Where strftime('%Y-%m-%d', DATETIME(Server.EntryDate/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "AND Conversion = 1;";

        return query;
    }


    /**
     * Returns an SQL query for selecting the total impression cost over time in the form of graph data
     * @return SQL Query
     */
    public String createTotalCostImpressionGraph(){
        String query =
                "Select sum(Impression.ImpressionCost) as data, strftime('" + timeGranularity + "', DATETIME(Impression.Date/1000, 'unixepoch')) as time "
                        + "From Impression "
                        + "Where strftime('%Y-%m-%d', DATETIME(Impression.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total impression cost over time used for data
     * @return SQL Query
     */
    public String createTotalCostImpressionData(){
        String query =
                "Select sum(Impression.ImpressionCost) as data "
                        + "From Impression "
                        + "Where strftime('%Y-%m-%d', DATETIME(Impression.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + ";";

        return query;
    }

    public String getDateStart() {
        String query =
                "Select min(strftime('%Y-%m-%d', DATETIME(Impression.Date/1000, 'unixepoch'))) as data "
                        + "From Impression;";

        return query;
    }

    public String getDateEnd() {
        String query =
                "Select max(strftime('%Y-%m-%d', DATETIME(Impression.Date/1000, 'unixepoch'))) as data "
                        + "From Impression;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total click cost over time in the form of graph data
     * @return SQL Query
     */
    public String createTotalCostClickGraph(){
        String query =
                "Select sum(Click.ClickCost) as data, strftime('" + timeGranularity + "', DATETIME(Click.Date/1000, 'unixepoch')) as time "
                        + "From Click "
                        + "Where strftime('%Y-%m-%d', DATETIME(Click.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + " "
                        + "Group By time "
                        + "Order by time ASC;";

        return query;
    }

    /**
     * Returns an SQL query for selecting the total click cost over time used for data
     * @return SQL Query
     */
    public String createTotalCostClickData(){
        String query =
                "Select sum(Click.ClickCost) as data "
                        + "From Click "
                        + "Where strftime('%Y-%m-%d', DATETIME(Click.Date/1000, 'unixepoch')) BETWEEN '" + dateStart + "' and '" + dateEnd+ "' "
                        + ageFilter + " " + genderFilter + " " + incomeFilter + " " + contextFilter + ";";

        return query;
    }


    public String createCPC(){
        String query = "";
        // TotalCost / Clicks
        return query;
    }

    public String createCTR(){
        String query = "";
        // Clicks / Impressions
        return query;
    }


    public String createCPA(){
        String query = "";
        // TotalCost / Conversions
        return query;
    }

    public String createCPM(){
        String query = "";
        // TotalCost / Impressions * 1000
        return query;
    }


    public String createBounceRate(){
        String query = "";
        // Bounces/Clicks

        return query;
    }

    /**
     * Set the time granularity for filtering
     * Should be in the form %(time), default set to %d
     * @param timeGranularity time filter
     */
    public void setTimeGranularity(String timeGranularity){
        this.timeGranularity = timeGranularity;
    }

    /**
     * Set the ages to filter by
     * @param ages age of Users
     */
    public void setAgeFilter(String[] ages){
        logger.info("Updating ages filter");
        if(ages.length == 0){
            ageFilter = "";
        } else{
            StringBuilder sb = new StringBuilder("AND (");
            for(int i = 0; i < ages.length-1; i++){
                sb.append("Age = '"+ages[i]+"' OR ");
            }
            sb.append("Age = '" + ages[ages.length-1] +"')");
            ageFilter = sb.toString();
        }
    }

    /**
     * Set the income to filter by
     * @param incomes Income of User
     */
    public void setIncomeFilter(String[] incomes){
        logger.info("Updating income filter");
        if(incomes.length == 0){
            incomeFilter = "";
        } else{
            StringBuilder sb = new StringBuilder("AND (");
            for(int i = 0; i < incomes.length-1; i++){
                sb.append("Income = '"+incomes[i]+"' OR ");
            }
            sb.append("Income = '" + incomes[incomes.length-1] +"')");
            incomeFilter = sb.toString();
        }
    }

    /**
     * Set the gender to filter by
     * @param genders Gender of User
     */
    public void setGenderFilter(String[] genders){
        logger.info("Updating gender filter");
        if(genders.length == 0){
            genderFilter = "";
        } else{
            StringBuilder sb = new StringBuilder("AND (");
            for(int i = 0; i < genders.length-1; i++){
                sb.append("Gender = '"+genders[i]+"' OR ");
            }
            sb.append("Gender = '" + genders[genders.length-1] +"')");
            genderFilter = sb.toString();
        }
    }



    /**
     * Set the context to filter by
     * @param contexts Context of the User
     */
    public void setContextFilter(String[] contexts){
        logger.info("Updating context filter");
        if(contexts.length == 0){
            contextFilter = "";
        } else{
            StringBuilder sb = new StringBuilder("AND (");
            for(int i = 0; i < contexts.length-1; i++){
                sb.append("Context = '"+contexts[i]+"' OR ");
            }
            sb.append("Context = '" + contexts[contexts.length-1] +"')");
            contextFilter = sb.toString();
        }
    }

    /**
     * Set the lower end date to filter by
     * @param date date in form (YYYY-MM-DD)
     */
    public void setDateStart(String date){
        logger.info("Updating the start date");
        this.dateStart = date;
    }
    /**
     * Set the upper end date to filter by
     * @param date date in form (YYYY-MM-DD)
     */
    public void setDateEnd(String date){
        logger.info("Updating the end date");
        this.dateEnd = date;
    }

}
