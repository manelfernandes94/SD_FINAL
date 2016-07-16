package pt.upa.transporter.ws;

import org.junit.*;
import pt.upa.transporter.domain.Transport;
import pt.upa.transporter.domain.TransportState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *  Unit Test example
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class TransporterWSTest {

    // static members
	private static TransporterPort transporterPort2;

    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {
		transporterPort2 = new TransporterPort("UpaTransporter2");
    }

    @AfterClass
    public static void oneTimeTearDown() {
		transporterPort2 = null;
    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    // tests

    @Test
    public void test() {

        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }


	@Test
	public void ping() {
		TransporterPort transporterPort = new TransporterPort("UpaTransporter1");
		String ping = transporterPort.ping("B");
		assertEquals(ping, "UpaTransporter1: B");
	}

	@Test
	public void requestJobNull() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jobView = transporterPort2.requestJob("Alaska", "New York", 150);
		assertNull(jobView);
	}

	@Test(expected = BadPriceFault_Exception.class)
	public void requestJobBadPrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
		transporterPort2.requestJob("Alaska", "New York", -1);
	}

	@Test
	public void requestJobSuccess() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jobView = transporterPort2.requestJob("Alaska", "New York", 50);
		assertEquals("Alaska", jobView.getJobOrigin());
		assertEquals("New York", jobView.getJobDestination());
		assertEquals(JobStateView.ACCEPTED, jobView.getJobState());
	}

	@Test(expected = BadLocationFault_Exception.class)
	public void requestJobOriginBadLocation() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jobView = transporterPort2.requestJob("Faro", "New York", 50);
		
	}

	@Test(expected = BadLocationFault_Exception.class)
	public void requestJobDestinationBadLocation() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jobView = transporterPort2.requestJob("Alaska", "Lisboa", 50);
		
	}

	


	@Test
	public void jobStatusNull() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jobView = transporterPort2.jobStatus("New id");
		assertNull(jobView);
	}

	

	@Test
	public void listJobsEmpty() {
		List<JobView> jobViewList = transporterPort2.listJobs();
		assertTrue(jobViewList.isEmpty());
	}


	@Test
	public void listJobs() throws BadLocationFault_Exception, BadPriceFault_Exception {
		transporterPort2.requestJob("Alaska", "New York", 50);
		List<JobView> jobViewList = transporterPort2.listJobs();
		assertTrue((!jobViewList.isEmpty()));
	}

	

	@Test
	public void clearJobs() throws BadLocationFault_Exception, BadPriceFault_Exception {
		transporterPort2.requestJob("Alaska", "New York", 50);
		transporterPort2.clearJobs();
		assertTrue(transporterPort2.listJobs().isEmpty());
	}











}