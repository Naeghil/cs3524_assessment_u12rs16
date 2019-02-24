import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import java.rmi.Naming;
import java.lang.SecurityManager;
import java.rmi.RemoteException;

import server.MUDServerInterface;

//Based on the ShoutClient.java provided in practical 1 example

public class MUDClient {
	public static void main(String args[]) throws RemoteException {
		if (args.length < 2) {
	    	System.err.println( "Usage:\njava MUDClient <host> <port>" );
	    	return;
		}
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		// Set the security manager
		System.setSecurityManager( new SecurityManager() ) ;

        // Obtain the server handle from the RMI registry
	    String regURL = "rmi://" + hostname + ":" + port + "/";
	    System.out.println( "Waiting for server..." );
        MUDServerInterface serv = null;
		//It may happen that the server is offline; in this case, the client keeps trying to connect
		boolean ready = false;
		while(!ready){ 
			try {	
				serv = (MUDServerInterface)Naming.lookup(regURL+"MUD");
				ready = true;
			} catch (java.rmi.NotBoundException e) { 
			} catch(java.net.MalformedURLException e) {
				System.err.println( e.getMessage() );
				return;
			} catch (java.rmi.ConnectException e) {
				System.out.println("Connection refused. The server might not be online.");
			}
		}
		
		//Starting session:
		System.out.println( serv.welcomeMessage() );
		boolean session = true;
		//A "current location" needs to be preserved sonewhere
		String location = serv.getStart();
		System.out.println( serv.see(location) );
		
		try (BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
			 BufferedWriter out = new BufferedWriter( new OutputStreamWriter( System.out));) {
			while(session) {
				//Read and process user input:
				String command = in.readLine();
				ArrayList<String> commandArgs = new ArrayList<String>(Arrays.asList(command.split(" ")));
				//Identify and execute the action:
				switch(commandArgs.get(0)) {
					case "exit": session = false;
					break;
					case "move": {
						if(commandArgs.size()!=2) out.write("You can only move one direction at a time\n");
						else {
							String dir = commandArgs.get(1);
							if(!dir.equals("north") && !dir.equals("south") && !dir.equals("west") && !dir.equals("east")) out.write("I'm sorry, where?");
							else {
								String newLoc = serv.move(location, commandArgs.get(1));
								if(newLoc.equals(location)) out.write("It takes a bit of willpower, but you manage to bump into a tree!\n");
								location = newLoc;
								out.write( serv.see(location) );
							}
						}
					}break;
					case "help": out.write( help );
					break;
					default: out.write( "I'm sorry, what?" );
				}
				out.flush();
			}
        } catch (java.io.IOException e) {
        	System.err.println( "I/O error." );
	    	System.err.println( e.getMessage() );
    	}
	}
															   
	private static String help = "In this game you can:\n\tmove dir - where 'dir' is one of the four cardinal directions, provided there is a path in that direction\n\texit - if you have other things to do\n\n";
}
