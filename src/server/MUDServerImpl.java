package server;

import java.rmi.RemoteException;

import mud.MUD;


//Based on ShoutServerImpl.java

public class MUDServerImpl implements MUDServerInterface {
    private MUD gameWorld;
	
	//Default constructor
	public MUDServerImpl() throws RemoteException {
		makeWorld("default");
	}
    
	//This function creates a MUD object depending on the world name
	private void makeWorld(String name) {
		//All three files uniquely identifying a world are stored in a folder named after the world:
		String filepath = "worlds/"+name+"/"+name;
		gameWorld = new MUD(filepath+".edg", filepath+".msg", filepath+".thg");
		
	}
	
	//Retrieves start location
	public String getStart() throws RemoteException {
		String loc = gameWorld.startLocation();
		gameWorld.addThing(loc, "character");
		return loc;
	}
	
	//Moves the character
    public String move( String from, String dir ) throws RemoteException {
        return gameWorld.moveThing(from, dir, "character");
    }
	//Returns information about the current location, excluding the character themselves
	public String see( String loc ) throws RemoteException {
		return gameWorld.locationInfo(loc).replaceFirst(" character ", " ");
	}
	
	public String welcomeMessage() throws RemoteException {
		String msg = "Connection successful. \nWelcome to this assessment-game! \nType 'help' for a list of available actions\n"; //Add other things as needed
		return msg;
	}
}
