package es.alvaronieto.pfcdam.Input;

import es.alvaronieto.pfcdam.States.GameState;
import es.alvaronieto.pfcdam.States.Snapshot;

public class SnapshotsManager {

	private static SnapshotsManager snapshotManager;
	
	private Snapshot previousSnapshot;
	private Snapshot newSnapshot;
	
	private SnapshotsManager(){
		
	}
	
	public static synchronized SnapshotsManager getInstance(){
		if(snapshotManager == null)
			snapshotManager = new SnapshotsManager();
		return snapshotManager;
	}
	
	public synchronized boolean snapshotAvailable(){
		return this.newSnapshot != null;
	}
	
	public synchronized void newSnapshot(Snapshot snapshot){
		this.newSnapshot = snapshot;
	}
	/*
	public synchronized GameState getInterpolatedState(){
		
		
		
		this.previousSnapshot = this.newSnapshot;
		this.newSnapshot = null;
	}*/
}
