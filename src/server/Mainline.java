package server;

import interfaces.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.lang.SecurityManager;
import java.util.ArrayList;
import java.io.IOException;
import utils.*;


public class Mainline implements MainlineInterface {
	private static Registry registry;
	private ArrayList<String> universe;
	private int maxWorlds;
	private int maxChars;
	
	public Mainline (Registry reg, int mWorlds, int mChars) throws RemoteException {
		registry = reg;
		universe = new ArrayList<String>();
		//Read from file and initialize existing worlds:
		maxWorlds = mWorlds;
		maxChars = mChars;
		GameFile wList = new GameFile("worlds/worldslist");
		try {
			for(String world : wList.readAll()) initWorld(world);
		} catch (IOException e) {
			if(!universe.contains("DefaultWorld")) initWorld("DefaultWorld");
		}
	}
	
	public static void main(String args[]) {
		if(args.length!= 4) usage();
		try { 
			if (System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());
			int regport = Integer.parseInt(args[0]);
			LocateRegistry.createRegistry(regport);
            Registry registry = LocateRegistry.getRegistry(regport);
			
			int servport = Integer.parseInt(args[1]);
			int mWorlds = Integer.parseInt(args[2]);
			int mChars = Integer.parseInt(args[3]);
			
			System.out.println("Initializing server...");
			Mainline worlds = new Mainline(registry, mWorlds, mChars);
			MainlineInterface worldsStub = (MainlineInterface)UnicastRemoteObject.exportObject(worlds, servport);
			
			System.out.println("Registering " + "worldsMainline" );
			registry.rebind("worldsMainline", worldsStub );
			System.out.println("Server fully initialized.");
		} catch (java.io.IOException e) {
			System.err.println( "Failed to register world server." );
			System.err.println( e.getMessage() );
		}
    
	}
	
//Interface implementation
	public boolean checkSpace(String name) throws RemoteException {
		if(universe.contains(name)) return false;
		if(universe.size()>=maxWorlds) return false;
		return true;
	}
	public boolean newWorld(ArrayList<String> edg, ArrayList<String> msg, ArrayList<String> thg, String name) throws RemoteException {
		System.out.println("New world data received.");
		if(!writeWorldFiles(text.oneString(edg), text.oneString(msg), text.oneString(thg), name)) return false;
		System.out.println("Initializing new world...");
		if(initWorld(name)) return true;
		else return false;		
	}
	public ArrayList<String> worldsList() throws RemoteException {
		return universe; 
	}
//Private methods:
	private boolean initWorld(String world)  {
		try {
		// Generate remote objects
			Server serv = new Server(world, maxChars);
			ServerInterface stub = (ServerInterface)UnicastRemoteObject.exportObject( serv, 0 );
			
			System.out.println("\tRegistering " + world );
			registry.rebind( world, stub );
			System.out.println("\tDone");
			universe.add(world);
			return true;
		} catch (IOException e) {
			System.err.println( "\tFailed to initialize." );
			System.err.println( e.getMessage() );
			return false;
		}
	}
	private boolean writeWorldFiles(String edg, String msg, String thg, String name) {
		System.out.println("\tWriting new world files...");
		try {
			//Init
			GameFile.initWorldDirs(name);
			//Write files
			GameFile.updateWorldFile(name, "edg", edg);
			GameFile.updateWorldFile(name, "msg", msg);
			GameFile.updateWorldFile(name, "thg", thg);
			//Add to list
			new GameFile("worlds/worldslist").addLine(name);
			
			System.out.println("\tSuccess.");
			return true;	
		} catch (IOException e) {
			System.out.println("\tFailed to write.");
			GameFile.deleteWorld(name);
			return false;
		}
	}
	private static void usage() {
			System.err.println("Usage:\njava MUDMainline <registryport> <serverport> <max worlds> <max characters per world>");
			System.exit(1);
		}
}