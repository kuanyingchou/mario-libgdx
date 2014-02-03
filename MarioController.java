import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

class MarioController extends InputListener {
    
    private Mario mario;
    //private Camera camera;
    private Vector3 touchPos = new Vector3();
    private int lastKey = -1;

    public MarioController(Mario m/*, Camera cam*/) {
        mario = m;
        //camera = cam;
    }

    public void update() {
        switch(lastKey) {
        case Keys.LEFT:
            mario.moveLeft();
            break;
        case Keys.RIGHT:
            mario.moveRight();
            break;
        case Keys.UP:
            mario.jump();
            break;
        }
    }

    @Override
    public boolean handle(Event e) {
        super.handle(e);
        return false;
    }

/*
    public boolean touchDown(
            InputEvent event, float x, float y, 
            int pointer, int button) {
        touchPos.set(x, y, 0);
        //camera.unproject(touchPos);
        mario.moveTo(touchPos.x - 128);
        return true;
    }
*/    
    @Override
    public boolean keyDown(InputEvent e, int keycode) {
        System.out.println("key down: "+keycode);
        if(lastKey == -1) lastKey = keycode;
        else lastKey = -1;
        return true;
    }

    @Override
    public boolean keyUp(InputEvent e, int keycode) {
        System.out.println("key up: "+keycode);
        lastKey = -1;
        return true;
    }
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("touch down");
        return true;
    }
}
