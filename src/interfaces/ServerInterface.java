package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	public String newCharacter(String name, String password, ClientInterface stub) throws RemoteException;
	public String loginCharacter(String name, String password, ClientInterface stub) throws RemoteException;
	public void deleteCharacter(String name) throws RemoteException;
	public void logoutCharacter(String name) throws RemoteException;
	public boolean saveCharacter(String name) throws RemoteException;
    public String move( String character, String dir ) throws RemoteException;
	public String see( String character ) throws RemoteException;
	public String pickUp(String  character, String thing) throws RemoteException;
	public String inventory(String character) throws RemoteException;
}