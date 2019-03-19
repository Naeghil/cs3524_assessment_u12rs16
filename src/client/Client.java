package client;

import java.util.ArrayList;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import interfaces.*;
import utils.text;

public class Client implements ClientInterface {
	protected Registry registry = null;
	protected MainlineInterface universe = null;
	protected ServerInterface world = null;
	protected String hero = null;
	protected boolean session = true;
	protected ClientInterface stub = null;

//Initialization methods:
	/* Constructor */
	public Client(String host, int port) throws InterruptedException, RemoteException, NotBoundException {
		registry = LocateRegistry.getRegistry(host, port);
		connect();
		world = (ServerInterface)registry.lookup("DefaultWorld");
		//Prepares a callback interface to pass to the servers it logs into
		stub = (ClientInterface)UnicastRemoteObject.exportObject( this, 0 );
	}	
	/* Method to retrieve the mainline handle */
	protected void connect() throws InterruptedException, RemoteException {
		System.out.println( "Contacting the server..." );
		while(universe==null){ 
			try { universe = (MainlineInterface)registry.lookup("worldsMainline"); } 
			catch (java.rmi.NotBoundException | java.rmi.ConnectException e) {
				Thread.sleep(1000);
			}
		}
	}
//World choice methods:
	//Choose the world from the list of words currently running
	public String chooseWorld(String input) throws RemoteException {
		if(input.equals("exit")) return exit();
		if(input.equals("help")) return text.getWorldMessage(printWorldsList());
		System.out.println( "Trying to contact "+input+"..." );
		try { world = (ServerInterface)registry.lookup(input); } 
		catch (java.rmi.NotBoundException | java.rmi.ConnectException e) {
			return "I can't connect to"+input;
		}
		return "Welcome to "+input+"! Who are you?"; 
	}
	//Checks if the universe can create a new world
	public boolean checkSpace(String name) throws RemoteException { return universe.checkSpace(name); }
	//Asks the universe to make a new world and enters it if it does
	public String newWorld(ArrayList<String> edges, ArrayList<String> messages, ArrayList<String> things, String name) throws RemoteException {
		if(universe.newWorld(edges, messages, things, name)) return chooseWorld(name);
		else return "Sorry, something went wrong with the creation of this world...";
	}
//"Multiplexor" methods choose the right "internal" function depending on user command	
	public String enter(ArrayList<String> input) throws RemoteException {
		String cmd = new String();
		if(input.size()>0) cmd = input.get(0);
		if(input.size()==1) {
			if(cmd.equals("exit")) return exit();
			if(cmd.equals("chworld")) return leaveWorld();
		} else if(input.size()==3) {
			if(cmd.equals("log")) return login(input.get(1), input.get(2));
			if(cmd.equals("reg")) return signup(input.get(1), input.get(2));
		}
		return text.BADINPUT;
	}
	public String action(ArrayList<String> input) throws RemoteException { //TODO: this
		String command="";
		if(input.size()>0 && input. size()<3) command = input.get(0);
		if(input.size() == 1) {
			if(command.equals("see")) return world.see(hero);
			if(command.equals("inventory")) return world.inventory(hero);
			if(command.equals("logout")) return logout();
			if(command.equals("chworld")) return leaveWorld();
			if(command.equals("exit")) return exit();
			if(command.equals("help")) return text.HELP;
		}
		if(input.size()==2) {
			if(command.equals("move")) return world.move(hero, input.get(1));
			if(command.equals("pickup")) return world.pickUp(hero, input.get(1));
		}
		return "Pardon?";
	}
//Character session methods
	private String signup(String name, String password) throws RemoteException {
		hero = world.newCharacter(name, password, stub);
		if(hero==null) return "Try again";
		else return text.HELP;
	}
	private String login(String name, String password) throws RemoteException {
		hero = world.loginCharacter(name, password, stub);
		if(hero==null) return "Try again";
		else return text.HELP;
	}
	private String logout() throws RemoteException {
		world.logoutCharacter(hero);
		String msg = "See you again, "+hero+"!";
		hero = null;
		return msg;
	}
	
//Session methods:
	public String exit() throws RemoteException {
		String msg = new String();
		if(world!=null) msg = leaveWorld()+text.nl;
		session = false;
		return msg+"Goodbye!";
	}
	public String leaveWorld() throws RemoteException {
		String msg = new String();
		if(hero!=null) msg = logout()+text.nl;
		world = null;
		return msg+"Leaving world...";
	}
	public void terminate() {
		try {  if(hero!=null) logout(); }
		catch (Exception e) {
			//This is only called in a "unusual termination" or "exception" situation
			//so I wouldn't know what to do about any exception
		}	
	}
	
//Loop guards
	public boolean toLogIn() { return session && world!=null && hero==null; }
	public boolean toChooseWorld() { return session && world==null; }
	public boolean inGame() { return session && world!=null && hero!=null; }
	public boolean on() { return session; }
		  
//Messages:
	public String printWorldsList() throws RemoteException {
		return text.indented(universe.worldsList());
	}
	/* Interface method for server notifications */
	public void notify(String msg) throws RemoteException {
		System.out.println("$>"+msg);
	}
}