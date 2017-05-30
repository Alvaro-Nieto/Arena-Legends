package es.alvaronieto.pfcdam.Scenes;

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
    private Game game;

    public Hud(SpriteBatch sb, Game game, Player player) {
    	this.game = game;
    	Skin skin = Resources.getInstance().getSkin();
    	Skin customSkin = new Skin(Gdx.files.internal("ui/custom/custom.json"));
    	
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        
        final ProgressBar healthBar = new ProgressBar(0, player.getMaxHealth(), 1, false, customSkin);
        healthBar.setValue(0f);
        healthBar.sizeBy(5f);
        healthBar.getStyle().background.setMinHeight(34f);
        healthBar.getStyle().knobBefore.setMinHeight(30f);
        healthBar.setAnimateDuration(1f);
        healthBar.setValue(player.getHealth());
        
        timeLabel = new Label(game.getTimer().toString(), skin);
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        
        table.add(timeLabel).top().expandY();
        table.row().bottom();
        table.add(healthBar);
        stage.addActor(table);
        
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
