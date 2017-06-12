package es.alvaronieto.pfcdam.net;

import com.badlogic.gdx.math.Vector2;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerSlot;
import es.alvaronieto.pfcdam.States.PlayerState;
import es.alvaronieto.pfcdam.gameobjects.GameRules;

public class Packets {
	
	public static class Packet01Message { 
		public long timeStamp;
		public String message;
		public String clientName;
	} 
	
	public static class Packet02ConnectionRequest { 
		public long timeStamp;
		public String clientName;
		public long adminToken;
	} 
	
	public static class Packet03ConnectionAccepted { 
		public long timeStamp;
		public long userID;
		public LobbyState lobbyState;
		public boolean admin;
		
	}
	
	public static class Packet04ConnectionRejected { 
		public long timeStamp;
		public String reason;
	} 
	
	public static class Packet05ClientConnected { 
		public long timeStamp;
		public long userID;
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
	
	public static class Packet10RequestInfo { 
		public long timeStamp;
	} 
	
	public static class Packet11InfoAnswer { 
		public long timeStamp;
		public String name;
		public GameRules gameRules;
		public int connectedPlayers;
	} 
	
	public static class Packet12GameStarted { 
		public long timeStamp;
		public long userID;
		public PlayerState playerState;
		public GameState gameState;
	}
	
	public static class Packet13StartRequest { 
		public long timeStamp;
		public long adminToken;
	}
	
	public static class Packet14GameRulesChangeRequest { 
		public long timeStamp;
		public long adminToken;
		public GameRules gameRules;
	}
	
	public static class Packet15SlotUpdate { 
		public long timeStamp;
		public PlayerSlot playerSlot;
	}
	
	public static class Packet16LobbyUpdate { 
		public long timeStamp;
		public LobbyState lobbyState;
	}
	
	public static class Packet17Attack1Request {
		public long userID;
		public Vector2 dir;
	}
}
