package models;

import java.sql.ResultSet;
import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class to test the Data class
 */
public class DataTest extends TestCase {

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(DataTest.class);

	/**
	 * Creates a mock class for the ResultSet data for the calculateData() method
	 * The data is valid data and is an example of what could be possible
	 * @return a list of mock ResultSet data
	 */
	private ResultSet[] getGoodMockData(){
		try{
			Mockery mockery = new Mockery();

			ResultSet[] results = new ResultSet[7];

			final ResultSet resultSet1 = mockery.mock(ResultSet.class, "impressions");
			mockery.checking(new Expectations() {{
				allowing(resultSet1).getInt(1); will(returnValue(123));
			}});
			results[0] = resultSet1;

			final ResultSet resultSet2 = mockery.mock(ResultSet.class, "clicks");
			mockery.checking(new Expectations() {{
				allowing(resultSet2).getInt(1); will(returnValue(15));
			}});
			results[1] = resultSet2;

			final ResultSet resultSet3 = mockery.mock(ResultSet.class, "uniques");
			mockery.checking(new Expectations() {{
				allowing(resultSet3).getInt(1); will(returnValue(2));
			}});
			results[2] = resultSet3;

			final ResultSet resultSet4 = mockery.mock(ResultSet.class, "bounces");
			mockery.checking(new Expectations() {{
				allowing(resultSet4).getInt(1); will(returnValue(15));
			}});
			results[3] = resultSet4;

			final ResultSet resultSet5 = mockery.mock(ResultSet.class, "conversions");
			mockery.checking(new Expectations() {{
				allowing(resultSet5).getInt(1); will(returnValue(23));
			}});
			results[4] = resultSet5;

			final ResultSet resultSet6 = mockery.mock(ResultSet.class, "impressionTotalCost");
			mockery.checking(new Expectations() {{
				allowing(resultSet6).getFloat(1); will(returnValue(152.3f));
			}});
			results[5] = resultSet6;

			final ResultSet resultSet7 = mockery.mock(ResultSet.class, "clickTotalCost");
			mockery.checking(new Expectations() {{
				allowing(resultSet7).getFloat(1); will(returnValue(10.13f));
			}});
			results[6] = resultSet7;

			return results;



		} catch(Exception e){
			logger.info(e.getMessage());
			return null;
		}

	}

	/**
	 * Creates a mock class for the ResultSet data for the calculateData() method
	 * The data is invalid data and would usually not come up
	 * @return a list of mock ResultSet data
	 */
	private ResultSet[] getBadMockData(){
		try{
			Mockery mockery = new Mockery();

			ResultSet[] results = new ResultSet[7];

			final ResultSet resultSet1 = mockery.mock(ResultSet.class, "impressions");
			mockery.checking(new Expectations() {{
				allowing(resultSet1).getInt(1); will(returnValue(-123));
			}});
			results[0] = resultSet1;

			final ResultSet resultSet2 = mockery.mock(ResultSet.class, "clicks");
			mockery.checking(new Expectations() {{
				allowing(resultSet2).getInt(1); will(returnValue(-15));
			}});
			results[1] = resultSet2;

			final ResultSet resultSet3 = mockery.mock(ResultSet.class, "uniques");
			mockery.checking(new Expectations() {{
				allowing(resultSet3).getInt(1); will(returnValue(-2));
			}});
			results[2] = resultSet3;

			final ResultSet resultSet4 = mockery.mock(ResultSet.class, "bounces");
			mockery.checking(new Expectations() {{
				allowing(resultSet4).getInt(1); will(returnValue(-15));
			}});
			results[3] = resultSet4;

			final ResultSet resultSet5 = mockery.mock(ResultSet.class, "conversions");
			mockery.checking(new Expectations() {{
				allowing(resultSet5).getInt(1); will(returnValue(-23));
			}});
			results[4] = resultSet5;

			final ResultSet resultSet6 = mockery.mock(ResultSet.class, "impressionTotalCost");
			mockery.checking(new Expectations() {{
				allowing(resultSet6).getFloat(1); will(returnValue(-152.3f));
			}});
			results[5] = resultSet6;

			final ResultSet resultSet7 = mockery.mock(ResultSet.class, "clickTotalCost");
			mockery.checking(new Expectations() {{
				allowing(resultSet7).getFloat(1); will(returnValue(-10.13f));
			}});
			results[6] = resultSet7;

			return results;



		} catch(Exception e){
			logger.info(e.getMessage());
			return null;
		}

	}

	/**
	 * Test for calculating valid impression data
	 */
	@Test
	public void testCalculateImpressionsGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(123, data.getImpressions());
	}

	/**
	 * Test for calculating invalid impression data
	 */
	@Test
	public void testCalculateImpressionsBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getImpressions());
	}

	/**
	 * Test for calculating valid click data
	 */
	@Test
	public void testCalculateClicksGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(15, data.getClicks());
	}

	/**
	 * Test for calcualting invalid click data
	 */
	@Test
	public void testCalculateClicksBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getClicks());
	}

	/**
	 * Test for calcualting valid unique data
	 */
	@Test
	public void testCalculateUniquesGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(15, data.getClicks());
	}

	/**
	 * Test for calculating invalid unique data
	 */
	@Test
	public void testCalculateUniquesBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getClicks());
	}

	/**
	 * Test for calculating valid bounce data
	 */
	@Test
	public void testCalculateBouncesGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(15, data.getBounces());
	}

	/**
	 * Test for calculating invalid bounce data
	 */
	@Test
	public void testCalculateBouncesBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getBounces());
	}

	/**
	 * Test for calculating valid conversion data
	 */
	@Test
	public void testCalculateConversionsGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(23, data.getConversions());
	}

	/**
	 * Test for calculating invalid conversion data
	 */
	@Test
	public void testCalculateConversionsBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getConversions());
	}

	/**
	 * Test for calculating valid total cost data
	 */
	@Test
	public void testCalculateTotalCostGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals((152.3f + 10.13f)/100, data.getTotalCost());
	}

	/**
	 * Test for calculating invalid total cost data
	 */
	@Test
	public void testCalculateTotalCostBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals((-152.3f + -10.13f)/100, data.getTotalCost());
	}

	/**
	 * Test for clauclating valid CTR data
	 */
	@Test
	public void testCalculateCTRGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(15f/123f, data.getCTR());
	}

	/**
	 * Test for calculating invalid CTR data
	 */
	@Test
	public void testCalculateCTRBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getCTR());
	}

	/**
	 * Test for calculating valid CPA data
	 */
	@Test
	public void testCalculateCPAGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(((152.3f + 10.13f)/100f)/23f, data.getCPA());
	}

	/**
	 * Test for calculating invalid CPA data
	 */
	@Test
	public void testCalculateCPABadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getCPA());
	}

	/**
	 * Test for calculating valid CPC data
	 */
	@Test
	public void testCalculateCPCGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(((152.3f + 10.13f)/100f)/15f, data.getCPC());
	}

	/**
	 * Test for calculating invalid CPC data
	 */
	@Test
	public void testCalculateCPCBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getCPC());
	}

	/**
	 * Test for calculating valid CPM data
	 */
	@Test
	public void testCalculateCPMGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals((((152.3f + 10.13f)/100f)/123f)*1000f, data.getCPM());
	}

	/**
	 * Test for calcilating invalid CPM data
	 */
	@Test
	public void testCalculateCPMBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getCPM());
	}

	/**
	 * Test for calculating valid bounce rate data
	 */
	@Test
	public void testCalculateBounceRateGoodData() {
		Data data = new Data();
		data.calculateData(getGoodMockData());
		Assertions.assertEquals(15f/15f*100f, data.getBounceRate());
	}

	/**
	 * Test for caluclating invalid bounce rate data
	 */
	@Test
	public void testCalculateBounceRateBadData() {
		Data data = new Data();
		data.calculateData(getBadMockData());
		Assertions.assertEquals(0, data.getBounceRate());
	}

}