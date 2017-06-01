package es.alvaronieto.pfcdam;

import java.util.Random;

public class SecurityUtility {
	private static Long adminToken;
	
	public static long getAdminToken(){
		if(adminToken == null)
			adminToken = new Random().nextLong();
		return adminToken;
	}
}
