package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;

public class LobbyScreen extends MenuScreen{
	
	private Table table;
	
	public LobbyScreen(final ScreenManager screenManager){
		super(screenManager);
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
				 screenManager.setCurrentScreen(Screens.MainScreen); // TODO Cambiar
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
	}
	
	private void configurationBar(){
		table.debugTable().debugCell();
		table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		table.top();
		table.padTop(100);
		
		Label numPlayers = new Label("Numero de \njuagadores: ", getSkin());
		table.add(numPlayers).minWidth(50);
		
		SelectBox playersList = new SelectBox(getSkin());
		String[] playersNumbers = {"1v1", "2v2", "5v5"};
		playersList.setItems(playersNumbers);
		table.add(playersList);
		
		Label map = new Label("Mapa: ", getSkin());
		table.add(map).minWidth(40).minHeight(50);
		SelectBox mapsList = new SelectBox(getSkin());
		String[] mapsNames = {"Lava arena", "Water arena"};
		mapsList.setItems(mapsNames);
		table.add(mapsList);
		
		Label mode = new Label("Modo: ", getSkin());
		table.add(mode).minHeight(50);
		Label duelMode = new Label("Duelo por \nequipos", getSkin());
		table.add(duelMode);
		
		Label rounds = new Label("Rondas: ", getSkin());
		table.add(rounds).minWidth(40).minHeight(50);
		TextField txtRounds = new TextField("", getSkin());
		txtRounds.setMaxLength(1);
		table.add(txtRounds).minWidth(50);
		
		stage.addActor(table);
	}
	
	private void teamLists(){
		Table team1 = new Table();
		Table team2 = new Table();
		
		team1.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		team2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/4*2);
		team1.left();
		team1.debugTable();
		team2.right();
		team2.debugTable();	
		
		SelectBox team1List = new SelectBox(getSkin());
		String s = "Team 1";
		team1List.setItems(s);
		team1.add(team1List);
		
		SelectBox team2List = new SelectBox(getSkin());
		String s2 = "Team 2";
		team2List.setItems(s2);
		team2.add(team2List);
		
		stage.addActor(team1List);
		stage.addActor(team2List);
	}

}
