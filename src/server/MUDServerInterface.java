package server;

import java.rmi.Remote;
import java.rmi.RemoteException;


//Based on ShoutServerInterface.java

public interface MUDServerInterface extends Remote {
	public String getStart() throws RemoteException;
    public String move( String from, String dir ) throws RemoteException;
	public String see( String loc ) throws RemoteException;
    public String welcomeMessage() throws RemoteException;
}
