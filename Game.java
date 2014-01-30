import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Input.Keys;

public class Game implements ApplicationListener {
    private Texture texture;
    private SpriteBatch batch;
    private Sprite sprite;
    private com.badlogic.gdx.graphics.OrthographicCamera camera;
    private Vector3 touchPos = new Vector3();
    private float speed = 400;

    @Override
    public void create () {
        System.out.println("game created");

        texture = new Texture(Gdx.files.internal("mario.png"));
        batch = new SpriteBatch();
        sprite = new Sprite(texture, 0, 0, 256, 256);
        sprite.setPosition(0, 0);
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render () {
        clear();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        //sprite.setColor(1, 0, 1f, 1);
        batch.end();

        processInput();

    }
    
    private void processInput() {
        processTouch();
        processKeys();
    }
    private void processTouch() {
        if(Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            sprite.setX(touchPos.x - 128);
        }
    }
    private void processKeys() {
        if(Gdx.input.isKeyPressed(Keys.LEFT)) 
                sprite.setX(sprite.getX() - speed * Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) 
                sprite.setX(sprite.getX() + speed * Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize (int width, int height) {
        System.out.println("game resized");
    }

    @Override
    public void pause () {
        System.out.println("game paused");
    }

    @Override
    public void resume () {
        System.out.println("game resumed");
    }

    @Override
    public void dispose () {
        texture.dispose();
        batch.dispose();
    }
}
