package es.alvaronieto.pfcdam.net;

import es.alvaronieto.pfcdam.States.PlayerState;

public interface ClientListener {
	public void PlayerStateReceived(PlayerState playerState);
	public void couldNotConnect();
}
