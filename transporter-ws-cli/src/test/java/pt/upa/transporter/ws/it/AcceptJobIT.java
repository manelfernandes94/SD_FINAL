package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

/**
 * Test suite
 */
public class AcceptJobIT extends AbstractIT {

	/**
	 * transporter 
	 * accept the job offer.
	 * 
	 * @result The job's state is JobStateView.ACCEPTED.
	 * @throws Exception
	 */
	@Test
	public void testAcceptJob() throws Exception {
		JobView jv = CLIENT.requestJob(LOCATION1, LOCATION2, PRICE_UPPER_LIMIT);
		assertEquals(JobStateView.ACCEPTED, jv.getJobState());
	}

	

}

