package client;

import java.rmi.RemoteException;
import java.lang.SecurityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import utils.text;

public class Main {
	private static BufferedReader in = null;
	
	public static void main(String args[]) throws InterruptedException {
		if (args.length < 2) usage(); 
		try {
			
			//Initialization:
			if (System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());
			in = new BufferedReader(new InputStreamReader(System.in));
			final Client client = new Client(args[0], Integer.parseInt(args[1]));
			Runtime.getRuntime().addShutdownHook(new Thread() {
         		public void run() { 
					try { 
						if(in!=null) in.close(); 
						if(client!=null) client.terminate();
					} catch (Exception e) {} //Well, what are you gonna do...
				}
      		});
			System.out.println(text.INITIAL_MESSAGE);
			//Main loop
			while(client.on()) {
				while(client.toChooseWorld()) {
					System.out.println(text.getWorldMessage(client.printWorldsList()));
					System.out.println(getWorld(client, getInput()));
				}
				//Character login
				if(client.toLogIn()) System.out.println(text.ENTER_MESSAGE);
				while(client.toLogIn()) System.out.println(client.enter(getInput()));
				//Actual game
				if(client.inGame()) System.out.println(client.action(new ArrayList<String>(Arrays.asList("see"))));
				while(client.inGame()) System.out.println(client.action(getInput()));
			}		
		} catch (InterruptedException a) { throw a; }
		catch (Exception e) {
			System.out.println(text.fatal(e.getMessage()));
			System.exit(1);
		}
		System.exit(0);
	}
	
	private static String getWorld(Client client, ArrayList<String> input) throws RemoteException {
		if(input.size()==1) return client.chooseWorld(input.get(0));
		if(input.size()==2 && input.get(0).equals("makeworld")) return worldDesign(client, input.get(1));
		return text.BADINPUT;
	}
	private static String worldDesign(Client client, String name) throws RemoteException {
		if(name.matches("makeworld|exit|help|worldsMainline")) return "Name not allowed";
		if(!client.checkSpace(name)) return text.noWorldCreation(name);
		ArrayList<String> input;
		//Get edges
		System.out.println(text.GET_EDGES);
		ArrayList<String> edges = new ArrayList<String>();
		while(!(input = getInput()).get(0).equals("")) {
			if(input.size()<3 || !input.get(1).matches("north|south|east|west")) System.out.println(text.BADINPUT);
			else edges.add(text.oneLine(input));
		}
		//Get messages
		System.out.println(text.GET_MESSAGES);
		ArrayList<String> messages = new ArrayList<String>();
		while(!(input = getInput()).get(0).equals("")) {
			if(input.size()<2) System.out.println("If you don't want a place to have a message, just don't include it here.");
			else messages.add(text.oneLine(input));
		}
		//Get things
		System.out.println(text.GET_THINGS);
		ArrayList<String> things = new ArrayList<String>();
		while(!(input = getInput()).equals("")) {
			if(input.size()!=2) System.out.println(text.BADINPUT);
			else things.add(text.oneLine(input));
		}
		return client.newWorld(edges, messages, things, name);	
	}
	private static void usage() {
	    System.err.println( "Usage:\njava MUDClient <host> <port>" );
	    System.exit(1);
	}
	private static ArrayList<String> getInput() {
		try { return new ArrayList<String>(Arrays.asList(in.readLine().split(" "))); } 
		catch (java.io.IOException e) { System.exit(1); }
		return null;
	}
	
}