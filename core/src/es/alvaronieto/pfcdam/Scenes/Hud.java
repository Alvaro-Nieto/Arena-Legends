package es.alvaronieto.pfcdam.Scenes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;
import es.alvaronieto.pfcdam.gameobjects.Game;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class Hud implements Disposable {
	
	private Stage stage;
    private Viewport viewport;
    private Label timeLabel;
    private Label namePlayer;
    private Label nRonda;
    private Label rondasde1;
    private Label rondasde2;
    private Game game;
    private Skin customSkin;
    private HashMap<Long, ProgressBar> healthBars;

    public Hud(SpriteBatch sb, Game game, Player player) {
    	this.game = game;
    	Skin skin = Resources.getInstance().getSkin();
    	customSkin = new Skin(Gdx.files.internal("ui/custom/custom.json"));
    	
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        this.healthBars = new HashMap<>();
        buildHealthBars(player, game);
        
        timeLabel = new Label(game.getTimer().toString(), skin);
        nRonda = new Label( "Ronda 1/3", Resources.getInstance().getSkin());
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        table.add(timeLabel).top().expandY();
        table.row().bottom();
        table.add(nRonda).bottom().expandY();
        stage.addActor(table);
        //stage.setDebugAll(true);
    }
    
    public void buildHealthBars(Player player, Game gameVida){
    	Table table1 = new Table();
    	table1.pad(5f);
        table1.setFillParent(true);
        table1.left();
        
        Table table2 = new Table();
    	table2.pad(5f);
        table2.setFillParent(true);
        table2.right();
        
        HashMap<Long, Player> players = game.getPlayers();
        for(Long userID : players.keySet()) {
        	Player tempPlayer = players.get(userID);
        	ProgressBar healthBar = new ProgressBar(0, tempPlayer.getMaxHealth(), 1, false, customSkin);
            healthBar.setValue(0f);
            healthBar.sizeBy(5f);
            healthBar.getStyle().background.setMinHeight(34f);
            healthBar.getStyle().knobBefore.setMinHeight(30f);
            healthBar.setAnimateDuration(1f);
            healthBar.setValue(tempPlayer.getHealth());
            healthBars.put(userID, healthBar);
            namePlayer = new Label( tempPlayer.getPlayerName(), Resources.getInstance().getSkin());
            
            if(tempPlayer.getTeam() == 1){
            	rondasde1 = new Label( "2 victorias", Resources.getInstance().getSkin());
            	table1.add(rondasde1);
            	table1.row();
            	table1.add(namePlayer);
            	table1.row();
                table1.add(healthBar);
                table1.row();
            }
            else {
            	rondasde2 = new Label( "1 victorias", Resources.getInstance().getSkin());
            	table2.add(rondasde2);
            	table2.row();
            	table2.add(namePlayer);
            	table2.row();
                table2.add(healthBar);
                table2.row();
            }
        }
    	stage.addActor(table1);
    	stage.addActor(table2);
    }
    public void update(float dt){
        timeLabel.setText(game.getTimer().toString());
        stage.act();
        for(Long userID : healthBars.keySet()) {
        	healthBars.get(userID).setValue(game.getPlayer(userID).getHealth());
        }
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }

    public Viewport getViewPort() {
		return viewport;
	}

	public void draw() {
		stage.draw();
	}
	
	public Matrix4 getProjectionMatrix() {
		return stage.getCamera().combined;
	}

}
