package es.alvaronieto.pfcdam.Scenes;

import java.util.Date;

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

import es.alvaronieto.pfcdam.Util.Constants;
import es.alvaronieto.pfcdam.gameobjects.Player;

public class TimeHud implements Disposable{
	public Stage stage;
    private Viewport viewport;
    Integer minutos = 0 , segundos = 0;
    Date fechaInicial=new Date();
    Date fechaActual;
    String min="", seg="";
    String cron="2:00";
    private Label timeLabel;
    

    public TimeHud(SpriteBatch sb) {
    	Skin skin =  new Skin(Gdx.files.internal("ui/star-soldier-ui.json"));
        viewport = new FitViewport(Constants.V_WIDTH,Constants.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.pad(5f);
        table.setFillParent(true);

        timeLabel = new Label("2:00", skin);
        
        
        table.add(timeLabel).top().expandY();
        
        
        stage.addActor(table);
    }
    
    public void crono(){    	
    	fechaActual=new Date();
        String min="", seg="";
        	if(!cron.equals("0:00")){
            	if((fechaActual.getSeconds()-fechaInicial.getSeconds())>0){
            		segundos = 60-(fechaActual.getSeconds()-fechaInicial.getSeconds());
            	}else{
            		segundos =(fechaInicial.getSeconds()-fechaActual.getSeconds());
            	}
                if((fechaActual.getSeconds()-fechaInicial.getSeconds())<=0){
                    minutos=2+(fechaInicial.getMinutes()-fechaActual.getMinutes());
                }else{
                	minutos=1+(fechaInicial.getMinutes()-fechaActual.getMinutes());
                }
                if(fechaInicial.getHours()!=fechaActual.getHours()){
                	minutos=2+(fechaInicial.getMinutes()-(fechaActual.getMinutes()+60));
                }
            }
            if(segundos < 10){
            	seg = "0" + segundos;
            }
            else{
            	seg = segundos.toString();
            }
            min = minutos.toString();
            cron=min + ":" + seg;                
            
        
    }
    
    public void update(float dt){
    	crono();
        timeLabel.setText(cron);                
            
    	
    }
    
    @Override
    public void dispose() {
        stage.dispose();
    }

   
    
    public Viewport getViewPort() {
		return viewport;
	}

	

}
