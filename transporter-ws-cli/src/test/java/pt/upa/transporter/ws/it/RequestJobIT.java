package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

import javax.xml.ws.soap.SOAPFaultException;


/**
 * Test suite
 */
public class RequestJobIT extends AbstractIT {

	/**
	 * Request a job (with valid origin, destination and price) with a price of
	 * 10.
	 * 
	 * @result The job should be successfully created and stored by the
	 *         transporter.
	 * @throws Exception
	 */
	@Test
	public void testRequestJob() throws Exception {
		CLIENT.requestJob(LOCATION1, LOCATION2, PRICE_SMALLEST_LIMIT);
	}

	// -------------- invalid inputs test cases ---------------

	/**
	 * Invoke CLIENT.requestJob on an invalid (empty string) origin.
	 * 
	 * @result Should throw BadLocationFault_Exception as the origin is invalid.
	 * @throws Exception
	 */
	@Test(expected = BadLocationFault_Exception.class)
	public void testRequestJobInvalidOrigin() throws Exception {
		CLIENT.requestJob(EMPTY_STRING, LOCATION1, PRICE_SMALLEST_LIMIT);
	}

	/**
	 * Invoke CLIENT.requestJob on an invalid (null) origin.
	 * 
	 * @result Should throw BadLocationFault_Exception as the origin is invalid.
	 * @throws Exception
	 */
	@Test(expected = BadLocationFault_Exception.class)
	public void testRequestJobNullOrigin() throws Exception {
		CLIENT.requestJob(null, LOCATION2, PRICE_SMALLEST_LIMIT);
	}

	/**
	 * Invoke CLIENT.requestJob on an invalid (empty string) destination.
	 * 
	 * @result Should throw BadLocationFault_Exception as the destination is
	 *         invalid.
	 * @throws Exception
	 */
	@Test(expected = BadLocationFault_Exception.class)
	public void testRequestJobInvalidDestination() throws Exception {
		CLIENT.requestJob(LOCATION1, EMPTY_STRING, PRICE_SMALLEST_LIMIT);
	}

	/**
	 * Invoke CLIENT.requestJob on an invalid (null) destination.
	 * 
	 * @result Should throw BadLocationFault_Exception as the destination is
	 *         invalid.
	 * @throws Exception
	 */
	@Test(expected = BadLocationFault_Exception.class)
	public void testRequestJobNullDestination() throws Exception {
		CLIENT.requestJob(LOCATION2, null, PRICE_SMALLEST_LIMIT);
	}

	/**
	 * Invoke CLIENT.requestJob on both invalid (empty string) origin and
	 * destination.
	 * 
	 * @result Should throw BadLocationFault_Exception as both the origin and
	 *         the destination is invalid.
	 * @throws Exception
	 */
	@Test(expected = BadLocationFault_Exception.class)
	public void testRequestJobInvalidOD() throws Exception {
		CLIENT.requestJob(EMPTY_STRING, EMPTY_STRING, PRICE_SMALLEST_LIMIT);
	}

	/**
	 * Invoke CLIENT.requestJob on both invalid (null) origin and destination.
	 * 
	 * @result Should throw BadLocationFault_Exception as both the origin and
	 *         the destination is invalid.
	 * @throws Exception
	 */
	@Test(expected = BadLocationFault_Exception.class)
	public void testRequestJobNullOD() throws Exception {
		CLIENT.requestJob(null, null, PRICE_SMALLEST_LIMIT);
	}

	/**
	 * Invoke CLIENT.requestJob with an invalid (negative) price.
	 * 
	 * @result Should throw BadPriceFault_Exception as the price given was
	 *         negative.
	 * @throws Exception
	 */
	@Test(expected = BadPriceFault_Exception.class)
	public void testRequestJobInvalidPrice() throws Exception {
		CLIENT.requestJob(LOCATION1, LOCATION2, INVALID_PRICE);
	}

	/**
	 * Invoke CLIENT.requestJob with all invalid parameters (empty string
	 * locations and negative price) of origin, destination and price.
	 * 
	 * @result Should throw BadLocationFault_Exception as both the origin and
	 *         the destination are invalid or BadPriceFault_Exception as an
	 *         invalid price given.
	 * @throws Exception
	 */
	public void testRequestJobInvalidArgs1() throws Exception {
		try {
			CLIENT.requestJob(EMPTY_STRING, EMPTY_STRING, INVALID_PRICE);
		} catch (BadLocationFault_Exception | BadPriceFault_Exception e) {
			// do nothing because both exceptions can be expected
		}
	}

	/**
	 * Invoke CLIENT.requestJob with all invalid parameters (null locations and
	 * negative price) of origin, destination and price.
	 * 
	 * @result Should throw BadLocationFault_Exception as both the origin and
	 *         the destination are invalid or BadPriceFault_Exception as an
	 *         invalid price given.
	 * @throws Exception
	 */
	public void testRequestJobInvalidArgs2() throws Exception {
		try {
			CLIENT.requestJob(null, null, INVALID_PRICE);
		} catch (BadLocationFault_Exception | BadPriceFault_Exception e) {
			// do nothing because both exceptions can be expected
		}
	}

	// -------------- reference price > 100 ---------------

	/**
	 * Test that a job request with a price over 100 returns null.
	 * 
	 * @return A null JobView reference.
	 * @throws Exception
	 */
	@Test
	public void testUpperPriceLimit() throws Exception {
		JobView jv1 = CLIENT.requestJob(LOCATION1, LOCATION2, PRICE_UPPER_LIMIT + 1);
		assertNull(jv1);
	}

	// -------------- reference price <= 10 ---------------

	/**
	 * Test that a job requested with a price below 10 returns a positive price
	 * equal to 10.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPriceBelowSmallestLimit() throws Exception {
		final int referencePrice = PRICE_SMALLEST_LIMIT - UNITARY_PRICE;
		JobView jv1 = CLIENT.requestJob(LOCATION1, LOCATION2, referencePrice);
		final int price = jv1.getJobPrice();
		assertTrue(price == 10);
	}

	

	// -------------- reference price >= 10 and <=1000  ---------------

	

	@Test
	public void testPriceAbove10andbelow100() throws Exception {
		final int referencePrice = 20;
		JobView jv1 = CLIENT.requestJob(LOCATION1, LOCATION2, referencePrice);
		final int price = jv1.getJobPrice();
		assertTrue(price == 20);
	}


}

