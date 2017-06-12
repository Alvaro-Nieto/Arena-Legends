package es.alvaronieto.pfcdam.Scenes;

import java.util.Arrays;
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
    private Game game;
    private Skin customSkin;

    public Hud(SpriteBatch sb, Game game, Player player) {
    	this.game = game;
    	Skin skin = Resources.getInstance().getSkin();
    	customSkin = new Skin(Gdx.files.internal("ui/custom/custom.json"));
    	
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        
        /*final ProgressBar healthBar = new ProgressBar(0, player.getMaxHealth(), 1, false, customSkin);
        healthBar.setValue(0f);
        healthBar.sizeBy(5f);
        healthBar.getStyle().background.setMinHeight(34f);
        healthBar.getStyle().knobBefore.setMinHeight(30f);
        healthBar.setAnimateDuration(1f);
        healthBar.setValue(player.getHealth());*/
        crearVidas(player, game);
        
        timeLabel = new Label(game.getTimer().toString(), skin);
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        table.add(timeLabel).top().expandY();
        stage.addActor(table);
        
    }
    public void crearVidas(Player player, Game gameVida){
    	int nplayers=game.getPlayers().size();
    	Set<Long> ids=game.getPlayers().keySet();
    	Object obj[]=ids.toArray();
    	Long[] arr = Arrays.asList(obj).toArray(new Long[obj.length]);
    	Table table1 = new Table();
    	table1.pad(5f);
        table1.setFillParent(true);
        Table table2 = new Table();
    	table2.pad(5f);
        table2.setFillParent(true);
    	while(nplayers>0){
    		ProgressBar healthBar = new ProgressBar(0, game.getPlayers().get(arr[nplayers-1]).getMaxHealth(), 1, false, customSkin);
            healthBar.setValue(0f);
            healthBar.sizeBy(5f);
            healthBar.getStyle().background.setMinHeight(34f);
            healthBar.getStyle().knobBefore.setMinHeight(30f);
            healthBar.setAnimateDuration(1f);
            healthBar.setValue(game.getPlayers().get(arr[nplayers-1]).getHealth());
            //System.out.println(player.getUserID());
            namePlayer = new Label(game.getPlayers().get(arr[nplayers-1]).getPj(), Resources.getInstance().getSkin());
            //game.getPlayers().get(nplayers).getTeam()==1
            
            if(game.getPlayers().get(arr[nplayers-1]).getTeam()==1){
            	table1.left();
            	table1.add(namePlayer);
            	table1.row();
                table1.add(healthBar);
                table1.row();
                System.out.println("hola");
            }else{
            	table2.right();
            	table2.add(namePlayer);
            	table2.row();
                table2.add(healthBar);
                table2.row();
                System.out.println("hola");
            }
                       
    		nplayers--; 
    	}
    	stage.addActor(table1);
    	stage.addActor(table2);
    }
    public void update(float dt){
        timeLabel.setText(game.getTimer().toString());
        stage.act();
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
