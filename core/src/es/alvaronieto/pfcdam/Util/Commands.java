package es.alvaronieto.pfcdam.Util;

import java.lang.reflect.Field;

import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.LogLevel;

import es.alvaronieto.pfcdam.Screens.ScreenManager;
import es.alvaronieto.pfcdam.gameobjects.GameRules;

public class Commands extends CommandExecutor {
	private static Commands instance;
	
	private Commands(){
		
	}
	
	public static synchronized Commands getInstance(){
		if(instance == null)
			instance = new Commands();
		return instance;
	}
	
	public void connect(String address){
		ScreenManager.getInstance().launchGameClient().connect(
				address, SecurityUtility.getAdminToken(), Config.getInstance().playerName);
	}
	
	public void show(String fieldName){
		Field field;
		try {
			field = Config.class.getField(fieldName);
			console.log(fieldName+" = "+field.get(Config.getInstance()));
		} catch (NoSuchFieldException e) {
			console.log(fieldName + " doesn't exist.", LogLevel.ERROR);
		} catch (Exception e) {
			
		}
	}
	
	public void demo(){
		ScreenManager sm = ScreenManager.getInstance();
		long adminToken = SecurityUtility.getAdminToken();
		sm.launchDemoServer(GameRules.getDefault(), adminToken);
		sm.launchGameClient().connect("localhost", adminToken, Config.getInstance().playerName);
		sm.getTestClient().sendStartRequest(adminToken);
	}
	
	public void set(String fieldName, String value){
		Config config = Config.getInstance();
		Field field;
		try {
			field = Config.class.getField(fieldName);
			Class<?> clazz = field.getType();
			if ( clazz.equals(String.class) )
				field.set(config, value);
			else if ( clazz.equals(int.class) )
				field.setInt(config, Integer.parseInt(value));
			else if ( clazz.equals(float.class) )
				field.setFloat(config, Float.parseFloat(value));
			else
				console.log("Type not available", LogLevel.ERROR);
		} catch (NoSuchFieldException e) {
			console.log(fieldName + " doesn't exist.", LogLevel.ERROR);
		} catch (IllegalArgumentException e) {
			console.log("Wrong value", LogLevel.ERROR);
		} catch (IllegalAccessException e) {}
	}
	
	public void loadConfig(String fileName){
		Config.getInstance().readConfig(fileName);
	}
}
