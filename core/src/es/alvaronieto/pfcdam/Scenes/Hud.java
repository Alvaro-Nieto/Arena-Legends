package es.alvaronieto.pfcdam.Scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.Util.Resources;
import es.alvaronieto.pfcdam.gameobjects.Game;

public class Hud implements Disposable {
	
	private Stage stage;
    private Viewport viewport;
    private Label timeLabel;
    private Game game;

    public Hud(SpriteBatch sb, Game game) {
    	this.game = game;
    	Skin skin = Resources.getInstance().getSkin();
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        
        timeLabel = new Label("00:00", skin);
        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        
        table.add(timeLabel).top().expandY();
        stage.addActor(table);
        
    }
    
    public void update(float dt){
        timeLabel.setText(game.getTimer().toString());
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
