package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

/**
 * Test suite
 */
public class JobStatusIT extends AbstractIT {

	/**
	 * Test an invocation of jobStatus on an invalid (empty string) job
	 * identifier. Implementation-dependent.
	 */
	@Test
	public void testInvalidJobStatus() {
		assertEquals(null, CLIENT.jobStatus(EMPTY_STRING));
	}

	/**
	 * Test an invocation of jobStatus on an invalid (null) job identifier.
	 * Implementation-dependent.
	 */
	@Test
	public void testNullJobStatus() throws Exception {
		assertEquals(null, CLIENT.jobStatus(null));
	}

	

	
	

}

