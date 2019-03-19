package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.rmi.RemoteException;
import interfaces.ClientInterface;
import utils.text;

public class PlayingCharacter {
	protected String password;
	protected String location;
	private ClientInterface client;
	private ArrayList<String> inventory;
	
	public PlayingCharacter(ArrayList<String> file) {
		this.password = file.get(0);
		this.location = file.get(1);
		this.inventory = new ArrayList<String>(Arrays.asList(file.get(2).split(" ")));
	}
	public PlayingCharacter(String password, String startLocation) {
		this.password = password;
		this.location = startLocation;
		this.inventory = new ArrayList<String>();
	}
	
	public void setClient(ClientInterface stub) {
		this.client = stub;
	}
	public void logout() {
		this.client = null;
	}
	public boolean isIn() { return client!=null; }

	public String showInventory() {
		return text.oneLine(inventory);
	}
	public void setLocation(String newLoc) {
		location = newLoc;
	}
	public String getLocation() {
		return location;
	}
	public void pickUp(String thing) {
		inventory.add(thing);
	}
	

	public void notify(String msg) throws RemoteException {
		client.notify(msg);
	}
	public String toFile() {
		String nl = System.getProperty("line.separator");
		String file = password+nl+location+nl;
		for(String item : inventory) file+=item+" ";
		//Linux text files want a newline at the end
		return file+nl;
	}
	public boolean checkPw(String passwordIn) {
		return password.equals(passwordIn);
	}



}