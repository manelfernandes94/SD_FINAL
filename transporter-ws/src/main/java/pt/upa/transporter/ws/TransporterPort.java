package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.HandlerChain;
import javax.xml.registry.JAXRException;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.EndpointManager;
import pt.upa.transporter.domain.Transport;
import pt.upa.transporter.domain.TransportState;
import pt.upa.transporter.domain.TransporterJob;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.ws.handler.AuthenticationHandler;

@WebService(
	    endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	    wsdlLocation="transporter.1_0.wsdl",
	    name="TransporterWebService",
	    portName="TransporterPort",
	    targetNamespace="http://ws.transporter.upa.pt/",
	    serviceName="TransporterService"
	)

@HandlerChain(file = "/upa_handler-chain.xml")
public class TransporterPort implements TransporterPortType {
	protected int id_counter;
	protected boolean even;
	protected boolean isPrimary;
	protected TransporterPortType backTransporter;
	protected UDDINaming uddiNaming;
	private static final String name = "UpaTransporter";
	private String transportName;
	private EndpointManager transporterEndpoint;
	protected Map<String, Transport> transports = new HashMap<String, Transport>();
	protected Map<String, TransporterClient> transporterCompanies = new HashMap<String, TransporterClient>();
	protected static final Set<String> locations = new HashSet<String>(
			Arrays.asList(new String[] {"Alabama", "Alaska", "Arizona", "Arkansas", "California","Colorado","Connecticut","Delaware", "Florida", "Georgia", 
										"Hawaii", "Idaho", "Illinois", "India", "Iowa", "Kansas", "Kentucky","Louisiana","Maine", "Maryland","Massachusetts",
										"Michigan", "Minnesota","Mississippi","Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
										"New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennysylvania", "Rhode Island",
										 "South Caroline", "South Dakota", "Tenessee", "Texas", "Utah", "Vermont", "Virgina", "Washington", "West Virginia",
										 "Wisconsin", "Wyomina"}));
			
	//Construtores 
    
    public TransporterPort(EndpointManager transporterEndpointManager) {
		this.transporterEndpoint = transporterEndpointManager;
	}
    
    public TransporterPort (String name) {
		this.transportName = name;
		
		if (name != null && name.length() != 0) {
			String transportNumber = name.substring("UpaTransporter".length());
			int upatransporter = Integer.parseInt(transportNumber);
			this.even = (((upatransporter & 1) == 0)? true : false);
		}
		
		this.id_counter = 0;
	}

    //Funcoes remotas
    
    
    //PING
	@Override
	public String ping(String name) {
		return transportName + ": " + name;
	}
	
	//REQUESTJOB = REQUESTTRANSPORT
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		 
		
		if (!locations.contains(origin) || !locations.contains(destination)) {
			BadLocationFault fault = new BadLocationFault();
			fault.setLocation(destination);
			throw new BadLocationFault_Exception("no such destination", fault);}
		 
		else if (price > 100) {
			return null;
		}
		
		else if (price <10){
			String newid = String.valueOf(getNextTransportId());
			System.out.println(newid);
			Transport transport = new Transport(origin, destination, price, newid);
			transport.setPrice(10);
			//transport.setState(TransportState.ACCEPTED);
			transports.put(newid, transport);
			return createView(transport);
			
		} 
		
		else if (price>=10 && price<=100){
			String newid = String.valueOf(getNextTransportId());
			System.out.println(newid);
			Transport transport = new Transport(origin, destination, price, newid);
			transport.setPrice(price);
			//transport.setState(TransportState.ACCEPTED);
			transports.put(newid, transport);
			return createView(transport);
		}
		return null;
		
		
		
		
	}

	
	//Consultar o estado de um transporte

	@Override
	public JobView jobStatus(String id) {
		Transport transport = this.transports.get(id);
		if (transport != null) {
			return createView(transport);
		}
		return null;
	}

	//Consultar a lista de transportes 
	@Override
	public List<JobView> listJobs() {
		List<JobView> listJobs = new ArrayList<JobView>();
		for(Entry<String, Transport> mapEntry : transports.entrySet()) {
			listJobs.add(createView(mapEntry.getValue()));
		}
		return listJobs;
	}
	
	// FAz clear de todos transportes da transportadora
	@Override
	public void clearJobs() {
		this.transports.clear();
	}

	//Metodo nao implementado mas tem de ser gerado
	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	// FUNCOES AUX
	private int getNextTransportId(){
		id_counter++;
		return this.id_counter; 
	}
	
	private JobView createView(Transport transport) {
		JobView viewJob = new JobView();
		viewJob.setCompanyName(transport.getTransporterCompany());
		viewJob.setJobDestination(transport.getDestination());
		viewJob.setJobOrigin(transport.getOrigin());
		viewJob.setJobIdentifier(transport.getTransportIdentifier());
		viewJob.setJobPrice(transport.getPrice());
		viewJob.setJobState(JobStateView.fromValue(transport.getState().value()));
		return viewJob;
    }


}

