package es.alvaronieto.pfcdam.net;

import es.alvaronieto.pfcdam.GameRules;
import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerState;

public interface ClientListener {
	public void couldNotConnect();
	public void startGame(PlayerState playerState, GameState gameState);
	public void newPlayerConnected(long userID);
	public void snapShotReceived(long timeStamp, GameState gameState, long sequenceNumber);
	public void newServerDiscovered(String name, GameRules gameRules, int connectedPlayers, String ipAddress);
	public void connectionAccepted(long userID, LobbyState lobbyState, boolean admin, String ipAddress);
	public void connectionRejected(String ipAddress, String reason);
	public void lobbyUpdate(LobbyState lobbyState);
}
