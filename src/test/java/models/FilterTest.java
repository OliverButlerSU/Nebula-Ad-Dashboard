package models;

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing the Filter class
 */
public class FilterTest extends TestCase {

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(FilterTest.class);

	/**
	 * Test for setting time granularity to dat
	 */
	@Test
	public void testSetTimeGranularityDay() {
		Filter filter = new Filter();
		filter.setTimeGranularity("%d");

		Assertions.assertTrue(filter.createClicksGraph().contains("strftime('%d'"));
	}

	/**
	 * Test for setting time granulating to week
	 */
	@Test
	public void testSetTimeGranularityWeek() {
		Filter filter = new Filter();
		filter.setTimeGranularity("%w");
		Assertions.assertTrue(filter.createClicksGraph().contains("strftime('%w'"));
	}

	/**
	 * Test for setting time granularity to month
	 */
	@Test
	public void testSetTimeGranularityMonth() {
		Filter filter = new Filter();
		filter.setTimeGranularity("%m");
		Assertions.assertTrue(filter.createClicksGraph().contains("strftime('%m'"));
	}

	/**
	 * Test for setting age filter to nothing
	 */
	@Test
	public void testSetAgeFilterNoValues() {
		Filter filter = new Filter();
		filter.setAgeFilter(new String[] {});
		Assertions.assertFalse(filter.createClicksGraph().contains("Age ="));
	}

	/**
	 * Test for setting age filter to one value
	 */
	@Test
	public void testSetAgeFilterOneValue() {
		Filter filter = new Filter();
		filter.setAgeFilter(new String[] {"<25"});
		Assertions.assertTrue(filter.createClicksGraph().contains("Age = '<25'"));
	}

	/**
	 * Test for setting age filter to multiple values
	 */
	@Test
	public void testSetAgeFilterMultipleValues() {
		Filter filter = new Filter();
		filter.setAgeFilter(new String[] {"<25", "25-40", ">52"});
		Assertions.assertTrue(filter.createClicksGraph().contains("AND (Age = '<25' OR Age = '25-40' OR Age = '>52'"));
	}

	/**
	 * Test for setting income filter to nothing
	 */
	@Test
	public void testSetIncomeFilterNoValues() {
		Filter filter = new Filter();
		filter.setIncomeFilter(new String[] {});
		Assertions.assertFalse(filter.createClicksGraph().contains("Income ="));
	}

	/**
	 * Test for setting income filter to one value
	 */
	@Test
	public void testSetIncomeFilterOneValue() {
		Filter filter = new Filter();
		filter.setIncomeFilter(new String[] {"Low"});
		Assertions.assertTrue(filter.createClicksGraph().contains("Income = 'Low'"));
	}

	/**
	 * Test for setting income filter to multiple values
	 */
	@Test
	public void testSetIncomeFilterMultipleValues() {
		Filter filter = new Filter();
		filter.setIncomeFilter(new String[] {"Low", "Medium", "High"});
		Assertions.assertTrue(filter.createClicksGraph().contains("AND (Income = 'Low' OR Income = 'Medium' OR Income = 'High')"));
	}

	/**
	 * Test for setting gender filter to nothing
	 */
	@Test
	public void testSetGenderFilterNoValues() {
		Filter filter = new Filter();
		filter.setGenderFilter(new String[] {});
		Assertions.assertFalse(filter.createClicksGraph().contains("Gender = "));
	}

	/**
	 * Test for setting gender filter to one value
	 */
	@Test
	public void testSetGenderFilterOneValue() {
		Filter filter = new Filter();
		filter.setGenderFilter(new String[] {"Male"});
		Assertions.assertTrue(filter.createClicksGraph().contains("Gender = 'Male'"));
	}

	/**
	 * Test for setting gender filter to multiple values
	 */
	@Test
	public void testSetGenderFilterMultipleValues() {
		Filter filter = new Filter();
		filter.setGenderFilter(new String[] {"Male", "Female"});
		Assertions.assertTrue(filter.createClicksGraph().contains("AND (Gender = 'Male' OR Gender = 'Female')"));
	}

	/**
	 * Test for setting context filter to nothing
	 */
	@Test
	public void testSetContextFilterNoValues() {
		Filter filter = new Filter();
		filter.setGenderFilter(new String[] {});
		Assertions.assertFalse(filter.createClicksGraph().contains("Context = "));
	}

	/**
	 * Test for setting context filter to one value
	 */
	@Test
	public void testSetContextFilterOneValue() {
		Filter filter = new Filter();
		filter.setContextFilter(new String[] {"Shopping"});
		Assertions.assertTrue(filter.createClicksGraph().contains("Context = 'Shopping'"));
	}

	/**
	 * Test for setting context filter to multiple values
	 */
	@Test
	public void testSetContextFilterMultipleValues() {
		Filter filter = new Filter();
		filter.setContextFilter(new String[] {"Shopping", "Blog", "Hobbies"});
		Assertions.assertTrue(filter.createClicksGraph().contains("AND (Context = 'Shopping' OR Context = 'Blog' OR Context = 'Hobbies')"));
	}

	/**
	 * Test for setting the start and end date
	 */
	@Test
	public void testSetDateStartAndEnd() {
		Filter filter = new Filter();
		filter.setDateEnd("2015-01-01");
		filter.setDateStart("2014-01-01");
		logger.info(filter.createClicksGraph());
		Assertions.assertTrue(filter.createClicksGraph().contains("BETWEEN '2014-01-01' and '2015-01-01'"));
	}
}