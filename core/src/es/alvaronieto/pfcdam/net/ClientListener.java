package es.alvaronieto.pfcdam.net;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.InputState;
import es.alvaronieto.pfcdam.States.PlayerState;

public interface ClientListener {
	public void couldNotConnect();
	public void connectionAccepted(PlayerState playerState, GameState gameState);
	public void newPlayerConnected(PlayerState playerState);
	public void inputReceived(InputState inputState, long userID);
	public void snapShotReceived(long timeStamp, GameState gameState);
}
