package es.alvaronieto.pfcdam.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.alvaronieto.pfcdam.Juego;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class DebugHud implements Disposable{
	public Stage stage;
    private Viewport viewport;
    private Player player;
    
    private Label fpsLabel;
    private Label velocityLabel;
    private Label posLabel;
    private Label f9Label;
    private Label f10Label;
    private Label f11Label;
    private Label f12Label;

    public DebugHud(SpriteBatch sb, Player player) {
    	Skin skin =  new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
        viewport = new FitViewport(Juego.V_WIDTH,Juego.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        this.player = player;

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);

        fpsLabel = new Label(String.format("%d FPS", Gdx.graphics.getFramesPerSecond()), skin);
        velocityLabel = new Label(String.format("Vel. x:%.2f y:%.2f", player.getBody().getLinearVelocity().x, player.getBody().getLinearVelocity().y), skin);
        posLabel = new Label(String.format("Pos. x:%.2f y:%.2f", player.getPosition().x, player.getPosition().y), skin);
        f9Label = new Label("F9   - Unassigned", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        f10Label = new Label("F10 - Toggle FPS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        f11Label = new Label("F11 - Toggle Player Info", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        f12Label = new Label("F12 - Toggle Freecamera", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        table.add(velocityLabel).left().expandX();
        table.add(fpsLabel).right();
        table.row();
        table.add(posLabel).left();
        table.row().expandY();
        table.add(f9Label).bottom().left();
        table.row();
        table.add(f10Label).bottom().left();
        table.row();
        table.add(f11Label).bottom().left();
        table.row();
        table.add(f12Label).bottom().left();
        
        stage.addActor(table);
    }
    
    public void update(float dt){
    	fpsLabel.setText(String.format("%d FPS", Gdx.graphics.getFramesPerSecond()));
    	posLabel.setText(String.format("Pos. x:%.2f y:%.2f", player.getPosition().x, player.getPosition().y));
    	velocityLabel.setText(String.format("Vel. x:%.2f y:%.2f", player.getBody().getLinearVelocity().x, player.getBody().getLinearVelocity().y));
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }

    public void toggleInfoPlayer() {
    	velocityLabel.setVisible(!velocityLabel.isVisible());
    	posLabel.setVisible(!posLabel.isVisible());
	}
    
    public void toggleFPS() {
		fpsLabel.setVisible(!fpsLabel.isVisible());
	}
    
    public Viewport getViewPort() {
		return viewport;
	}
}
