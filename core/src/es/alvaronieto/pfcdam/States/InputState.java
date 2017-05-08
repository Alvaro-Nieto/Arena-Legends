package es.alvaronieto.pfcdam.States;

public class InputState {
	
	private int sequenceNumber;
	private boolean upKey;
	private boolean downKey;
	private boolean leftKey;
	private boolean rightKey;
	
	public InputState(){
		
	}
	
	public InputState(boolean upKey, boolean downKey, boolean leftKey, boolean rightKey, int sequenceNumber){
		this.upKey = upKey;
		this.downKey = downKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.sequenceNumber = sequenceNumber;
	}
	
	public boolean isUpKey() {
		return upKey;
	}
	
	public void setUpKey(boolean upKey) {
		this.upKey = upKey;
	}
	
	public boolean isDownKey() {
		return downKey;
	}
	
	public void setDownKey(boolean downKey) {
		this.downKey = downKey;
	}
	
	public boolean isLeftKey() {
		return leftKey;
	}
	
	public void setLeftKey(boolean leftKey) {
		this.leftKey = leftKey;
	}
	
	public boolean isRightKey() {
		return rightKey;
	}
	
	public void setRightKey(boolean rightKey) {
		this.rightKey = rightKey;
	}
	
	

	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public String toString() {
		return (upKey ? "1" : "0")+ "," + (downKey ? "1" : "0")+ "," + (leftKey ? "1" : "0")+ "," + (rightKey ? "1" : "0");
	}
	
	
	
}
