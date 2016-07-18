package pt.upa.transporter.domain;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import pt.upa.transporter.ws.BadJobFault;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.upa.transporter.domain.TransportState;
import pt.upa.transporter.*;


public  class Transport{
	
	// Variaveis
	
	protected String transportIdentifier;
	protected String origin;
    protected String destination;
    protected Integer price;
    protected String transporterCompany;
    protected TransportState state;
    //protected TransporterClient transporterEndpoint;

	//Mapa das localizacoes disponiveis
	protected static final Set<String> locations = new HashSet<String>(
			Arrays.asList(new String[] {"Alabama", "Alaska", "Arizona", "Arkansas", "California","Colorado","Connecticut","Delaware", "Florida", "Georgia", 
										"Hawaii", "Idaho", "Illinois", "India", "Iowa", "Kansas", "Kentucky","Louisiana","Maine", "Maryland","Massachusetts",
										"Michigan", "Minnesota","Mississippi","Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
										"New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennysylvania", "Rhode Island",
										 "South Caroline", "South Dakota", "Tenessee", "Texas", "Utah", "Vermont", "Virgina", "Washington", "West Virginia",
										 "Wisconsin", "Wyomina"}));
	
	//Construtor de um transporte com algumas verificacoes
	public Transport(String origin, String destination, Integer price, String id) 
			throws BadPriceFault_Exception, BadLocationFault_Exception {
		
		if (price < 0){
			BadPriceFault invalidPrice = new BadPriceFault();
			invalidPrice.setPrice(price);
			throw new BadPriceFault_Exception("Price cannot be below zero", invalidPrice);
		}
		if (!Transport.locations.contains(origin)) {
			BadLocationFault unknownLocation = new BadLocationFault();
			unknownLocation.setLocation(origin);
			throw new BadLocationFault_Exception("Unknown location", unknownLocation);
		}
		if (!Transport.locations.contains(destination)){
			BadLocationFault unknownLocation = new BadLocationFault();
			unknownLocation.setLocation(destination);
			throw new BadLocationFault_Exception("Unknown location", unknownLocation);
		}
		
		this.transportIdentifier = id;
		this.origin = origin;
		this.destination = destination;
		this.price = price;
		this.transporterCompany = null;
		this.state = TransportState.ACCEPTED;
		this.setTimer();
		//this.transporterEndpoint = null;
	}
	
	// Getters e Settters de Transporte

	public void setTimer(){
		new UpdateTransport().schedule();
	}

    public String getTransportIdentifier() {
		return transportIdentifier;
	}

	public void setTransportIdentifier(String jobIdentifier) {
		this.transportIdentifier = jobIdentifier;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getTransporterCompany() {
		return transporterCompany;
	}

	public void setTransporterCompany(String transporterCompany) {
		this.transporterCompany = transporterCompany;
	}

	public TransportState getState() {
		return state;
	}

	public void setState(TransportState value) {
		this.state = value;
	}
	
	

	
	
	//Transicoes de estados de viagem tendo em conta o passar do tempo
	
	private Timer timer = new Timer();
	
    class UpdateTransport extends TimerTask {
    	
    	 @Override
         public void run() {
    		 switch (state) {
				case ACCEPTED:
					setState(TransportState.HEADING);
					schedule();
					
					break;
				case HEADING:
					setState(TransportState.ONGOING);
					schedule();
					
					break;
				case ONGOING:
					setState(TransportState.COMPLETED);
					schedule();
					
					break;
				default:
					timer.cancel();
					break;
    		 }
         }
         
         public void schedule() {
        	 int delay =  1000;
        	 try {
        		 timer.schedule(new UpdateTransport(), delay);
        	 } catch (IllegalStateException e) {
        		 
        	 }
         }
        
    }
    

	
    	

	
	
}		
	

