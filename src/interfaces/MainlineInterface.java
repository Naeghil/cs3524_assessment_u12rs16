package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MainlineInterface extends Remote {
	public ArrayList<String> worldsList() throws RemoteException;
	public boolean checkSpace(String name) throws RemoteException;
	public boolean newWorld(ArrayList<String> edg, ArrayList<String> msg, ArrayList<String> thg, String name) throws RemoteException;
}

