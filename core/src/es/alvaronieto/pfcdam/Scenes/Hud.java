package es.alvaronieto.pfcdam.Scenes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class Hud implements Disposable {
	
	private Stage stage;
    private Viewport viewport;
    private Date fechaInicial;
    private Date fechaActual;
    private Label timeLabel;
    private Date maxTime;
    private SimpleDateFormat sDate;

    public Hud(SpriteBatch sb, int minutes) {
    	Skin skin = Resources.getInstance().getSkin();
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);
        
        sDate = new SimpleDateFormat("mm:ss");
        
        try {
			maxTime = sDate.parse(minutes+":00");
		} catch (ParseException e) {}
        
        timeLabel = new Label(sDate.format(maxTime), skin);
        
        
        table.add(timeLabel).top().expandY();
        stage.addActor(table);
        
        fechaInicial = new Date();
    }
    
    public void update(float dt){
    	Date timeRemaining = new Date(maxTime.getTime() - (new Date().getTime() - fechaInicial.getTime()));
        timeLabel.setText(sDate.format(timeRemaining));
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
