package es.alvaronieto.pfcdam.Util;

public class ProjectileContact {
	private long projectilUserID;
	private long seqNo;
	private long userIDhit;
	public ProjectileContact(long projectilUserID, long seqNo, long userIDhit) {
		super();
		this.projectilUserID = projectilUserID;
		this.seqNo = seqNo;
		this.userIDhit = userIDhit;
	}
	public long getProjectilUserID() {
		return projectilUserID;
	}
	public void setProjectilUserID(long projectilUserID) {
		this.projectilUserID = projectilUserID;
	}
	public long getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}
	public long getUserIDhit() {
		return userIDhit;
	}
	public void setUserIDhit(long userIDhit) {
		this.userIDhit = userIDhit;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProjectileContact) {
			ProjectileContact c = (ProjectileContact)obj;
			return c.getProjectilUserID() == this.projectilUserID && c.getSeqNo() == this.seqNo & c.getUserIDhit() == this.getUserIDhit();
		}
		return super.equals(obj);
	}
	
	
}
