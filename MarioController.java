import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Camera;

class MarioController extends InputAdapter {
    
    private Mario mario;
    private Camera camera;
    private Vector3 touchPos = new Vector3();
    private int lastKey = -1;

    public MarioController(Mario m, Camera cam) {
        mario = m;
        camera = cam;
    }

    public void update() {
        switch(lastKey) {
        case Keys.LEFT:
            mario.moveLeft();
            break;
        case Keys.RIGHT:
            mario.moveRight();
            break;
        }
    }

    public boolean touchDown(
            int screenX, int screenY, 
            int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);
        mario.moveTo(touchPos.x - 128);
        return true;
    }
    
    @Override
    public boolean keyDown(int keycode) {
        
        if(lastKey == -1) lastKey = keycode;
        else lastKey = -1;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        lastKey = -1;
        return true;
    }
}
