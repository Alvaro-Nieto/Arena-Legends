package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.Util.Constants;

public class LobbyScreen extends MenuScreen{
	
	private Table table;
	private boolean admin;
	private int totalPlayers;
	private String level;
		
	public LobbyScreen(final ScreenManager screenManager, boolean admin){
		super(screenManager);
		this.admin = admin;
	}
	
	@Override
	protected void stageDefinition(){
		this.table = new Table();
		
		Table backTable = new Table();
		backTable.pad(5f);
		backTable.setFillParent(true);
		TextButton backBtn = new TextButton("Atras", getSkin());
				
		backBtn.addListener(new InputListener(){
			 @Override
			 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				 screenManager.setMainScreen(new MainScreen(screenManager));
				 screenManager.setCurrentScreen(Screens.LobbyScreen);
				 screenManager.getScreen().dispose();
				 screenManager.setScreen(screenManager.getMainScreen());
				 return false;
			 }
		});
		
		backTable.top();
		backTable.right();
		backTable.add(backBtn);
		stage.addActor(backTable);

		
		Label config = new Label("CONFIGURAR PARTIDA", getSkin());
		config.setFontScale(1.5f);
		config.setPosition(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()-50f);
		
		stage.addActor(config);
		
		configurationBar();
		
		teamLists();
		
		final TextButton startBtn = new TextButton("Comenzar", getSkin());
		
		startBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				startBtn.setTouchable(Touchable.disabled);
				screenManager.launchGameServer();
				screenManager.launchGameClient();
				return false;
			}
		});
		
		Table startTable = new Table();
		startTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		startTable.bottom().right();
		startTable.add(startBtn);
		
		stage.addActor(startTable);
	}
	
	private void configurationBar(){ // TODO Hacer que los jugadores que no sean admins no puedan configurar la partida
		//table.debugTable().debugCell();
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.top();
		table.padTop(100);
		
		Label numPlayers = new Label("Numero de \njuagadores: ", getSkin());
		table.add(numPlayers).minWidth(50);
		
		final SelectBox playersList = new SelectBox(getSkin());
		String[] playersNumbers = {"1v1", "2v2", "5v5"};
		playersList.setItems(playersNumbers);
		playersList.setSelectedIndex(0);
		
		table.add(playersList);
		
		Label map = new Label("Mapa: ", getSkin());
		table.add(map).minWidth(40).minHeight(50);
		final SelectBox mapsList = new SelectBox(getSkin());
		String[] mapsNames = {"Lava arena", "Water arena"};
		mapsList.setItems(mapsNames);
		table.add(mapsList);
		mapsList.setSelectedIndex(0);
		
		Label mode = new Label("Modo: ", getSkin());
		table.add(mode).minHeight(50);
		Label duelMode = new Label("Duelo por \nequipos", getSkin());
		table.add(duelMode);
		
		Label rounds = new Label("Rondas: ", getSkin());
		table.add(rounds).minWidth(40).minHeight(50);
		TextField txtRounds = new TextField("", getSkin());
		txtRounds.setMaxLength(1);
		txtRounds.setText("1");
		table.add(txtRounds).maxWidth(60);
				
		TextButton acceptBtn = new TextButton("Aplicar", getSkin());
		acceptBtn.center();
		acceptBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				switch(playersList.getSelectedIndex()){
					case 0: totalPlayers = 2;break;
					case 1: totalPlayers = 4;break;
					case 2: totalPlayers = 10;
				}
				
				switch(mapsList.getSelectedIndex()){
					case 0: level = Constants.ARENA_LAVA;break;
					case 1: level = Constants.ARENA_WATER;
				}
				return false;
			}
		});
		
		table.add(acceptBtn);
		
		stage.addActor(table);
	}
	
	private void teamLists(){ // Players would appear in these tables
		Table team1 = new Table();
		Table team2 = new Table();
		
		//team1.debugAll();
		//team2.debugAll();
		
		team1.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		team2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		team1.padLeft(300);
		team2.padRight(300);
		
		team1.left();
		team2.right();
		
		List team1List = new List(getSkin());
		String s = "Team 1";
		team1List.setItems(s);
		team1.add(team1List).minWidth(250);
		
		List team2List = new List(getSkin());
		String s2 = "Team 2";
		team2List.setItems(s2);
		team2.add(team2List).minWidth(250);
		
		stage.addActor(team1);
		stage.addActor(team2);
	}
}
