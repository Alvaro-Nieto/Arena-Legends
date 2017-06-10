package es.alvaronieto.pfcdam.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.strongjoshua.console.Console;
import com.strongjoshua.console.LogLevel;

public class Config {
	
	private static Config config;
	
	/*
	 * All public fields in this class will be accessible from console commands set and show
	 */
	public String playerName;
	
	private Config() {
		
	}
	
	public static synchronized Config getInstance() {
		if(config == null) {
			config = new Config();
			config.readConfig();
		}
		return config;
	}
	
	private void readConfig() {
		File file = new File("cfg/user.cfg");
		if(file.exists())
			readConfig(file.getName());
		else
			readConfig("default.cfg");
	}

	public void readConfig(String fileName){
		Console console = Resources.getInstance().getConsole();
		File file = new File("cfg/"+fileName);
		if(file.exists() && file.isFile()){
			BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader( new FileReader( file ) );
                String line;
                
                console.log("Loading " + fileName + " configuration file...");
                
                while((line = bufferedReader.readLine()) != null){
                	if(line.isEmpty() || line.charAt(0) == '#')
                		continue;
                	else {
                		String[] parts = line.split("=");
                		if(parts.length == 2){
                			Commands.getInstance().set(parts[0].trim(), parts[1].trim());
                		}
                	}
                }
                console.log("Config file " + fileName + " has been loaded", LogLevel.SUCCESS);                
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
            	if(bufferedReader != null)
					try {
						bufferedReader.close();
					} catch (IOException e) {}
            }
        } else
            console.log("File doesn't exist", LogLevel.ERROR);
	}
	
	
}
