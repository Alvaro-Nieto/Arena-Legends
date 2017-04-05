package es.alvaronieto.pfcdam.net;

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
	
	public static class Packet03ConnectionResponse { 
		public long timeStamp;
		public long userID;
		public boolean accepted;
	} 
	
	public static class Packet04ClientConnected { 
		public long timeStamp;
		public long userID;
	} 
	
	public static class Packet05ClientDisconnected { 
		public long timeStamp;
		public long userID;
	} 
}
