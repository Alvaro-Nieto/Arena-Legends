package es.alvaronieto.pfcdam.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountDownTimer {
	
	private static final SimpleDateFormat TFORMAT = new SimpleDateFormat("mm:ss");
	private Date timeRemaining;   // Current time of this timer
	private long startTime; 	  // The instant when the timer started to run
	private long maxTime;   	  // The time used to start the countdown
	
	private boolean running;
	private boolean hasFinished;
	
	public CountDownTimer(int minutes, int seconds) throws IllegalArgumentException {
		running = false;
		hasFinished = false;
		try {
			maxTime = TFORMAT.parse(String.format("%2d:%2d",minutes,seconds)).getTime();
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid time");
		}
		timeRemaining = new Date(maxTime);
	}
	
	public void start(){
		running = true;
		startTime = new Date().getTime();
	}
	
	public void update(){
		if(running){
			timeRemaining = new Date(maxTime - (new Date().getTime() - startTime));
			if(TFORMAT.format(timeRemaining).equals("00:00")){
				stop();
				hasFinished = true;
			}
		}
	}

	private void stop() {
		running = false;
	}
	
	public boolean hasFinished(){
		return hasFinished;
	}

	@Override
	public String toString() {
		return TFORMAT.format(timeRemaining);
	}
	
}
