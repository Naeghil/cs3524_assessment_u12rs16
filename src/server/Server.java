package server;

import java.util.HashMap;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import mud.MUD;
import interfaces.ServerInterface;
import interfaces.ClientInterface;
import utils.GameFile;

//Based on ShoutServerImpl.java
public class Server implements ServerInterface {
    private MUD gameWorld;
	private String worldName;
	private HashMap<String, PlayingCharacter> characters;
	private int maxChars;
	
//Default constructor
	public Server(String name, int mChars) throws RemoteException {
		worldName = name;
		maxChars = mChars;
		makeWorld();
		loadCharacters();
	}   
//This function creates a MUD object depending on the world name
	private void makeWorld() {
		//All three files uniquely identifying a world are stored in a folder named after the world:
		String filepath = "worlds/"+worldName+"/"+worldName;
		gameWorld = new MUD(filepath+".edg", filepath+".msg", filepath+".thg");	
	}
//This function loads the known characters from the character list file
	private void loadCharacters(){
		characters = new HashMap<String, PlayingCharacter>();
		String cliFile = "worlds/"+worldName+"/"+worldName+".cli";
		try {
			for(String line : new GameFile("worlds/"+worldName+"/"+worldName+".cli").readAll()) 
				characters.put(line, null);
		} catch( IOException e ) { // In line with the other world creation functions in the class mud.MUD
			System.err.println( "MUDServer.loadCharacters( String " +  cliFile + ")\n" + e.getMessage() );
		}
	}
//Registers a new character for this world
	public String newCharacter(String name, String password, ClientInterface stub) throws RemoteException {
		if(characters.keySet().size()>=maxChars) stub.notify("Sorry, "+worldName+" is not accepting more heroes.");
		else if (characters.keySet().contains(name)) stub.notify("Sorry, there is already a "+name+" in "+worldName);
		else {
			//Creation
			PlayingCharacter newC = new PlayingCharacter(password, gameWorld.startLocation());
			newC.setClient(stub);
			//Registration
			try{
				//Add to list
				new GameFile("worlds/"+worldName+"/"+worldName+".cli").addLine(name);
				//Write file
				GameFile.updateCharacter(name, worldName, newC.toFile());
				//Runtime adjustments
				notifyAll("The newbie "+name+" has entered "+worldName);
				gameWorld.addThing(gameWorld.startLocation(), name);
				stub.notify("A new weirdo rises in "+worldName+"! Their name is "+name);
				characters.put(name, newC);
				//TODO: does a server have a welcome message? Does the server have an owner that can delete it?
				return name;
			} catch (IOException e) {
				System.err.println("Uknown error while trying to create "+name+" in world "+worldName+System.getProperty("line.separator")+e.getMessage());
				stub.notify("Sorry, there was a problem while registering your character");
				GameFile.deleteCharacter(name, worldName);
			}
		}
		return null;
	}
//Retrieves a registered character, letting the user play it
	public String loginCharacter(String name, String password, ClientInterface stub) throws RemoteException {
		if(!characters.keySet().contains(name)) 
			stub.notify("Sorry, we found no "+name+" in "+worldName);
		else if(characters.get(name)!=null) 
			stub.notify(name+" is already playing in "+worldName+"!");
		else { 
			try {
				//Load the character
				String filePath = "worlds/"+worldName+"/characters/"+name;
				PlayingCharacter toLogIn = new PlayingCharacter(new GameFile(filePath).readAll());
				toLogIn.setClient(stub);
				//Check password
				if(!toLogIn.checkPw(password)) {
					stub.notify("Wrong password.");
					return null;
				}
				//Runtime adjustments:
				notifyAll(name+" has entered "+worldName);
				gameWorld.addThing(toLogIn.getLocation(), name);
				characters.put(name, toLogIn);
				//TODO: does a server have a welcome message? Does the server have an owner that can delete it?
				return name;	 
			} catch (IOException e) {
				System.err.println("Uknown error while trying to load "+name+" in world "+worldName+"\n"+e.getMessage());
				stub.notify("Sorry, there was a problem while loading your character");
			}
		}
		return null;
	}
	//Deletes a character; this assumes the client cannot call this method if a character is not logged in
	public void deleteCharacter(String name) throws RemoteException {
		gameWorld.delThing(characters.get(name).getLocation(), name);
		GameFile.deleteCharacter(name, worldName);
		characters.remove(name);
	}
	//Logs out a character
	public void logoutCharacter(String name) throws RemoteException {
		saveCharacter(name);
		characters.put(name, null);
	}
	//Save a character to a file on the server
	public boolean saveCharacter(String name) throws RemoteException {
		try {
			GameFile.updateCharacter(name, worldName, characters.get(name).toFile());
			characters.get(name).notify("Character data saved");
			return true;
		} catch (IOException e) {
			System.err.println("Unknown error while trying to save "+name+" in "+worldName+"\n"+e.getMessage());
			return false;
		} 
	}

//These functions all assume a character is currently logged in:
	//Moves the character
    public String move(String character, String dir) throws RemoteException {
		PlayingCharacter actor = characters.get(character);
		String newLocation = gameWorld.moveThing(actor.getLocation(), dir, character);
		if(newLocation.equals(actor.getLocation())) return "It takes a bit of willpower, but you manage to bump into something!";
		actor.setLocation(newLocation);
		return see(character);
    }
	//Returns information about the current location, excluding the character themselves
	public String see( String character ) throws RemoteException {
		String location = characters.get(character).getLocation();
		return gameWorld.locationInfo(location).replaceFirst(" "+character+" ", " ");
	} 
	//A character picks up an item
	public String pickUp(String  character, String thing) throws RemoteException {
		PlayingCharacter actor = characters.get(character);
		try{
			gameWorld.delThing(actor.getLocation(), thing);
			actor.pickUp(thing);
			return "Congratulations, you just got a "+thing+"!";
		} catch (NullPointerException e) {
			return "There's no "+thing+" here!";
		}
	}
	public String inventory(String character) throws RemoteException {
		return "You have: "+System.getProperty("line.separator")+characters.get(character).showInventory();
	}
	
	//TODO: Do something to save the current state of the server, the mud and such
	
	private void notifyAll(String msg) {
		for(String cName : characters.keySet()) if(characters.get(cName)!=null) {
			try {
				characters.get(cName).notify(msg);
			} catch (RemoteException e) {
				//Their client crashed
			}
		}					
	}
}

