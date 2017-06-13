package es.alvaronieto.pfcdam.sound;

import java.lang.invoke.LambdaConversionException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static SoundManager instance;
	
	private Sound click;
	private Sound firogball;
	private Sound punch;
	private Sound start;
	private Sound truemoball;
	private Sound venetoBall;
	
	private Music lava;
	private Music lobby;
	private Music water;

	private boolean muted;
	
	private SoundManager(){
		this.click = Gdx.audio.newSound((Gdx.files.internal("sounds/fx/click.mp3")));
		this.firogball = Gdx.audio.newSound((Gdx.files.internal("sounds/fx/firogball.wav")));
		this.punch = Gdx.audio.newSound((Gdx.files.internal("sounds/fx/punch.mp3")));
		this.start = Gdx.audio.newSound((Gdx.files.internal("sounds/fx/start.wav")));
		this.truemoball = Gdx.audio.newSound((Gdx.files.internal("sounds/fx/truemoball.wav")));
		this.venetoBall = Gdx.audio.newSound((Gdx.files.internal("sounds/fx/venetoball.wav")));
		
		this.lava = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/lava.mp3"));
		this.lobby = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/lobby.mp3"));
		this.water = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/water.mp3"));
	
		muted = false;
		
		lava.setLooping(true);
		lobby.setLooping(true);
		water.setLooping(true);
		lobby.setVolume(0.75f);
		lava.setVolume(0.5f);
		water.setVolume(0.5f);
		
	}
	
	public static SoundManager getInstance(){
		if(instance == null)
			instance = new SoundManager();
		return instance;
	}
	
	public void playTruemoBall(){
		if(!muted)
			truemoball.play(1f);
	}
	
	public void playFirogBall(){
		if(!muted)
			firogball.play(1f);
	}
	
	public void playVenetoBall(){
		if(!muted)
			venetoBall.play(1f);
	}
	
	public void startLobby(){
		lobby.play();
	}
	
	public void stopLobby(){
		lobby.stop();
	}

	public Sound getClick() {
		return click;
	}

	public Sound getFirogball() {
		return firogball;
	}

	public Sound getPunch() {
		return punch;
	}

	public Sound getStart() {
		return start;
	}

	public Sound getTruemoball() {
		return truemoball;
	}

	public Music getLava() {
		return lava;
	}

	public Music getLobby() {
		return lobby;
	}

	public Music getWater() {
		return water;
	}

	public void startLava() {
		lava.play();
	}

	public void startWater() {
		water.play();
	}
	
	public void mute(boolean mute){
		this.muted = mute;
		if(mute) {
			water.setVolume(0f);
			lava.setVolume(0f);
			lobby.setVolume(0f);
		}
		else{
			water.setVolume(1f);
			lava.setVolume(1f);
			lobby.setVolume(1f);
		}
	}
	
	public boolean isMuted(){
		return muted;
	}
	
}
