package pt.upa.ca;

import pt.upa.ca.ws.cli.CAClient;

public class CAClientApplication {

	public static void main(String[] args) throws Exception {
		System.out.println(CAClientApplication.class.getSimpleName() + " starting...");
		// Check arguments
		if (args.length < 1) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s wsURL%n", CAClientApplication.class.getName());
			return;
		}

		
	}
}
