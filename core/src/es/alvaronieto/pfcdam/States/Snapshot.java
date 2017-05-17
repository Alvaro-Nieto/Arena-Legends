package es.alvaronieto.pfcdam.States;

public class Snapshot {
	
	private GameState gameState;
	private long sequenceNumber;
	private long timeStamp;
	private long serverCurrentTick;
	
	public Snapshot(GameState gameState, long sequenceNumber, long timeStamp, long serverCurrentTick) {
		this.gameState = gameState;
		this.sequenceNumber = sequenceNumber;
		this.timeStamp = timeStamp;
		this.serverCurrentTick = serverCurrentTick;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getServerCurrentTick() {
		return serverCurrentTick;
	}

	public void setServerCurrentTick(long serverCurrentTick) {
		this.serverCurrentTick = serverCurrentTick;
	}
	
}
