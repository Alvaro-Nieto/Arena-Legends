package es.alvaronieto.pfcdam.Screens;

import static es.alvaronieto.pfcdam.Util.Constants.*;

import java.awt.SecondaryLoop;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

import es.alvaronieto.pfcdam.Screens.ScreenManager.Screens;
import es.alvaronieto.pfcdam.States.LobbyState;
import es.alvaronieto.pfcdam.States.PlayerSlot;
import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.SecurityUtility;
import es.alvaronieto.pfcdam.gameobjects.GameRules;

public class LobbyScreen extends MenuScreen{
	
	private Table table;
	private Table startTable;
	private boolean admin;
	private int totalPlayers;
	private String level;
	private LobbyState lobbyState;
	private long userID;
	
	private List team1List;
	private List team2List;
	private SelectBox playersList;
	private SelectBox mapsList;
	private SelectBox characterList;
	private TextField txtRounds;
	
	public LobbyScreen(final ScreenManager screenManager, boolean admin, LobbyState lobbyState, long userID){
		super(screenManager);
		this.admin = admin;
		System.out.println("Hay admin" +admin);
		table.setTouchable(admin ? Touchable.enabled : Touchable.disabled);
		//startTable.setTouchable(admin ? Touchable.enabled : Touchable.disabled);	
		this.userID = userID;
		this.lobbyState = lobbyState;
		applyLobbyStateToScene();
		//stage.setDebugAll(true);
	}
	

	@Override
	protected void buildStage(){
		
		this.table = new Table();
		
		Table backTable = new Table();
		backTable.pad(5f);
		backTable.setFillParent(true);
		TextButton backBtn = new TextButton("Atras", getSkin());
				
		backBtn.addListener(new InputListener(){
			 @Override
			 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				 screenManager.showMainScreen();
				 return false;
			 }
		});
		
		backTable.top();
		backTable.right();
		backTable.add(backBtn);
		stage.addActor(backTable);

		
		Label lblTitulo = new Label("Lobby", getSkin());
		lblTitulo.setFontScale(1.5f);
		lblTitulo.setPosition(V_WIDTH/2 - lblTitulo.getWidth() / 2, V_HEIGHT-50f);
		
		stage.addActor(lblTitulo);
		
		configurationBar();
		
		teamLists();
		
		final TextButton startBtn = new TextButton("Comenzar", getSkin());
		
		startBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				screenManager.getTestClient().sendStartRequest(SecurityUtility.getAdminToken());
				return false;
			}
		});
		
		startTable = new Table();
		startTable.setSize(V_WIDTH, V_HEIGHT);
		startTable.bottom().right();
		startTable.add(startBtn);
		
		stage.addActor(startTable);
		
	}
	
	private void configurationBar(){ // TODO Hacer que los jugadores que no sean admins no puedan configurar la partida
		//table.debugTable().debugCell();
		table.setSize(V_WIDTH, V_HEIGHT);
		table.top();
		table.padTop(100);
		
		Label numPlayers = new Label("Numero de \njugadores: ", getSkin());
		table.add(numPlayers).minWidth(50);
		
		playersList = new SelectBox(getSkin());
		String[] playersNumbers = {"1v1", "2v2", "5v5"};
		playersList.setItems(playersNumbers);
		playersList.setSelectedIndex(0);
		
		table.add(playersList);
		
		Label map = new Label("Mapa: ", getSkin());
		table.add(map).minWidth(40).minHeight(50);
		mapsList = new SelectBox(getSkin());
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
		txtRounds = new TextField("", getSkin());
		txtRounds.setTextFieldFilter(new TextFieldFilter(){
			
			@Override
			public boolean acceptChar(TextField textField, char c) {
				return Character.isDigit(c);
			}
			
		});
		txtRounds.setMaxLength(1);
		txtRounds.setText("1");
		table.add(txtRounds).maxWidth(60);
				
		TextButton acceptBtn = new TextButton("Aplicar", getSkin());
		acceptBtn.center();
		acceptBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				setMaxPlayers(playersList);
				setArena(mapsList);
				screenManager.getTestClient().sendGameRulesUpdate(new GameRules(level, totalPlayers, 0, 30, Integer.parseInt(txtRounds.getText())), SecurityUtility.getAdminToken());
				return false;
			}
		});
		
		table.add(acceptBtn);
		stage.addActor(table);
		
		setMaxPlayers(playersList);
		setArena(mapsList);
	}
	

	private void setArena(final SelectBox mapsList) {
		switch(mapsList.getSelectedIndex()){
			case 0: 
				level = Constants.ARENA_LAVA;
				break;
			case 1: level = Constants.ARENA_WATER;
		}
	}
	
	private void setMaxPlayers(final SelectBox playersList) {
		switch(playersList.getSelectedIndex()){
			case 0: 
				totalPlayers = 2;
				break;
			case 1: 
				totalPlayers = 4;
				break;
			case 2: 
				totalPlayers = 10;
		}
	}
	
	private int getIndexOfMaxPlayersList(int maxPlayers){
		int index = 0;
		switch(maxPlayers) {
			case 4:
				index = 1;
				break;
			case 10:
				index = 2;
				break;
			}
		return index;
	}
	
	private void teamLists(){ // Players will appear in these tables
		Table team1 = new Table();
		Table team2 = new Table();
		
		//team1.debugAll();
		//team2.debugAll();
		
		team1.setSize(V_WIDTH, V_HEIGHT);
		team2.setSize(V_WIDTH, V_HEIGHT);
		
		team1.padLeft(300);
		team2.padRight(300);
		
		team1.left();
		team2.right();
		
		
		Label lblTeam1 = new Label("Team 1", getSkin());
		Label lblTeam2 = new Label("Team 2", getSkin());
		team1List = new List(getSkin());
		team1List.setTouchable(Touchable.disabled);
		
		team1.add(lblTeam1);
		team1.row();
		team1.add(team1List).minWidth(250);
		
		team2List = new List(getSkin());
		team2List.setTouchable(Touchable.disabled);
		team2.add(lblTeam2);
		team2.row();
		team2.add(team2List).minWidth(250);
		
		stage.addActor(team1);
		stage.addActor(team2);
		
		playerConfigBar(team1, team2);
	}
	
	private void playerConfigBar(Table team1, Table team2){
		
		Table configTable = new Table();
		String[] characters = {"Truemo", "Firog"};
		
		characterList = new SelectBox(getSkin());
		characterList.setItems(characters);
		
		configTable.add(characterList);	
		
		TextButton changeCharBtn = new TextButton("Cambiar personaje", getSkin());
		
		changeCharBtn.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				pjChange();
				return false;
			}
		});
		
		configTable.add(changeCharBtn);
		
		TextButton changeTeam = new TextButton("Cambiar de equipo", getSkin());
		changeTeam.addListener(new InputListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				changeTeam();
				return false;
			}
		});
		
		configTable.setPosition((team1.getWidth()+team2.getWidth())/4, (team1.getHeight()+team2.getHeight())/8);
		configTable.add(changeTeam);
		
		stage.addActor(configTable);
	}
	
	protected void changeTeam() {
		System.out.println(lobbyState.getPlayersTeam1() + " : " + lobbyState.getPlayersTeam2());
		
		PlayerSlot slot = lobbyState.getPlayerSlots().get(userID);
		if(slot.getTeam() == 1) {
			if(lobbyState.getPlayersTeam2() < lobbyState.getMaxPlayersPerTeam()){
				slot.setTeam(2);
				screenManager.getTestClient().sendSlotUpdate(slot);
				System.out.println("entra");
			}
		}
		else {
			if(lobbyState.getPlayersTeam1() < lobbyState.getMaxPlayersPerTeam()){
				slot.setTeam(1);
				screenManager.getTestClient().sendSlotUpdate(slot);
				System.out.println("entra");
			}
		}
	}


	protected void pjChange() {
		String pj;
		if(characterList.getSelectedIndex() == 0)
			pj = TRUEMO;
		else
			pj = FIROG;
		PlayerSlot slot = lobbyState.getPlayerSlots().get(userID);
		slot.setPj(pj);
		
		screenManager.getTestClient().sendSlotUpdate(slot);
	}


	private int getMapIndex(String arenaPath) {
		int index = 0;
		if(arenaPath.equals(ARENA_WATER))
			index = 1;
		return index;
	}


	public void applyLobbyStateToScene(){
		
		String[] arrTeam1 = new String[lobbyState.getMaxPlayersPerTeam()];
		String[] arrTeam2 = new String[lobbyState.getMaxPlayersPerTeam()];
		
		HashMap<Long, PlayerSlot> slots = lobbyState.getPlayerSlots();
		int t1Index = 0;
		int t2Index = 0;
		for(Long userID : slots.keySet()){
			PlayerSlot slot = slots.get(userID);
			if(slot.getTeam() == 1){
				arrTeam1[t1Index] = slot.getPlayerName() + " (" + slot.getPj() + ")";
				if(slot.getUserID() == this.userID)
					arrTeam1[t1Index] = "> " + arrTeam1[t1Index];
				t1Index++;
			} else {
				arrTeam2[t2Index] = slot.getPlayerName() + " (" + slot.getPj() + ")";
				if(slot.getUserID() == this.userID)
					arrTeam2[t2Index] = "> " + arrTeam2[t2Index];
				t2Index++;
			}
		}
		
		for(int i = t1Index; i < arrTeam1.length; i++){
			arrTeam1[i] = "Empty";
		}
		
		for(int i = t2Index; i < arrTeam2.length; i++){
			arrTeam2[i] = "Empty";
		}
		
		team1List.setItems(arrTeam1);
		team2List.setItems(arrTeam2);
		
		team1List.setSelectedIndex(-1);
		team2List.setSelectedIndex(-1);
		
		playersList.setSelectedIndex(getIndexOfMaxPlayersList(lobbyState.getMaxPlayersPerTeam() * 2));

		mapsList.setSelectedIndex(getMapIndex(lobbyState.getGameRules().getArenaPath()));
		
		txtRounds.setText(Integer.toString(lobbyState.getGameRules().getRounds()));
		
		
	}

	@Override
	protected void beforeBuild() {
		// TODO Auto-generated method stub
	}

	public void updateLobby(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
		applyLobbyStateToScene();
	}
}
