package server;

import java.rmi.Naming;
import java.lang.SecurityManager;
import java.rmi.server.UnicastRemoteObject;

import java.net.InetAddress;

//Based on ShoutServerMainline.java 

public class MUDMainline {
	private static boolean serverSetup(int regport, int servport) {
		try {
			String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ;
			// Specify security policy
			System.setSecurityManager( new SecurityManager() ) ;

			// Generate remote objects
			MUDServerImpl serv = new MUDServerImpl();
			MUDServerInterface stub = (MUDServerInterface)UnicastRemoteObject.exportObject( serv, servport );

			String regURL = "rmi://" + hostname + ":" + regport + "/";
			System.out.println("Registering " + "MUD" );
			Naming.rebind( regURL + "MUD", stub );
			System.out.println("Done");
		} catch(java.net.UnknownHostException e) {
			System.err.println( "Cannot get local host name." );
			System.err.println( e.getMessage() );
			return false;
		} catch (java.io.IOException e) {
			System.err.println( "Failed to register." );
			System.err.println( e.getMessage() );
			return false;
		}
		return true;
    }
	
	
	
    public static void main(String args[]) {
		if(args.length != 2 ) {
			System.err.println("Usage:\njava MUDMainline <registryport> <serverport>");
			return;
		}
		if(!serverSetup(Integer.parseInt(args[0]), Integer.parseInt(args[1]))) {
			System.err.println("Failed to setup.");
		}
    }
	
	
	
}
