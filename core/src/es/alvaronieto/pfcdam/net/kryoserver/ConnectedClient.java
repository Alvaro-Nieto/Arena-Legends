package es.alvaronieto.pfcdam.net.kryoserver;

import com.esotericsoftware.kryonet.Connection;

public class ConnectedClient {
	
	private long userID;
	private Connection connection;
	private long lastInputAccepted;
	
	public ConnectedClient(long userID, Connection connection) {
		super();
		this.userID = userID;
		this.connection = connection;
		this.lastInputAccepted = -1;
	}
	
	public ConnectedClient(long userID, Connection connection, long lastInputAccepted) {
		this(userID, connection);
		this.lastInputAccepted = lastInputAccepted;
	}

	public void sendTCP(Object obj){
		this.connection.sendTCP(obj);
	}
	
	public void sendUDP(Object obj){
		this.connection.sendUDP(obj);
	}
	
	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public long getLastInputAccepted() {
		return lastInputAccepted;
	}

	public void setLastInputAccepted(long lastInputAccepted) {
		this.lastInputAccepted = lastInputAccepted;
	}
		
}
