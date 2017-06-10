package es.alvaronieto.pfcdam.Screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuListener extends ClickListener {

	private TextButton[] btnListSelf;
	private TextButton[] btnListOthers;
	private long timeLastEnter;
	private long timeLastExit;
	private boolean isOver = false;
	private float timeOut;
	
	public MenuListener(TextButton[] btnListSelf, TextButton[] btnListOthers, float timeOut) {
		super();
		this.btnListSelf= btnListSelf;
		this.btnListOthers = btnListOthers;
		this.timeOut = timeOut;
		for(TextButton btn : btnListSelf){
			btn.addAction(Actions.alpha(0));
		}
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		isOver = true;
		timeLastEnter = System.currentTimeMillis();
		for(TextButton btn : btnListSelf) {
			btn.addAction(Actions.fadeIn(3f));
			//btn.setVisible(true);
		}
		for(TextButton btn : btnListOthers)
			btn.setVisible(false);
		
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		isOver = false;
		timeLastExit = System.currentTimeMillis();
	}
	
	public void update(float dt) {
		if(!isOver && (System.currentTimeMillis() - timeLastExit) / 1000f > timeOut && !isAnyChildActive()){
			for(TextButton btn : btnListSelf){
				btn.addAction(Actions.fadeOut(0));
				//btn.setVisible(false);
			}
		}	
	}

	private boolean isAnyChildActive() {
		for(TextButton btn : btnListSelf){
			if(btn.isOver())
				return true;
		}
		return false;
	}
	
}
