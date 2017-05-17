package es.alvaronieto.pfcdam.net;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;

public class Packets {
	public static class Packet01Message { 
		public long timeStamp;
		public String message;
		public String clientName;
	} 
	
	public static class Packet02ConnectionRequest { 
		public long timeStamp;
		public String clientName;
	} 
	
	public static class Packet03ConnectionAccepted { 
		public long timeStamp;
		public long userID;
		public PlayerState playerState;
		public GameState gameState;
	}
	
	public static class Packet04ConnectionRejected { 
		public long timeStamp;
	} 
	
	public static class Packet05ClientConnected { 
		public long timeStamp;
		public long userID;
		public PlayerState playerState;
	} 
	
	public static class Packet06ClientDisconnected { 
		public long timeStamp;
		public long userID;
	} 
	
	public static class Packet07PlayerUpdate { 
		public long timeStamp;
		public PlayerState playerState;
	} 
	
	public static class Packet08GameUpdate { 
		public long timeStamp;
		public long userLastInputProccessed;
		public long serverCurrentTick;
		public GameState gameState;
	} 
	
	public static class Packet09UserInput { 
		public long timeStamp;
		public long userID;
		public InputState inputState;
	} 
}
