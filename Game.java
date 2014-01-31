import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

public class Game implements ApplicationListener {
    private SpriteBatch batch;
    private com.badlogic.gdx.graphics.OrthographicCamera camera;
    private BitmapFont font;
    private Mario mario;
    private MarioController marioController;

    @Override
    public void create () {
        System.out.println("game created");

        batch = new SpriteBatch();

        font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        mario = new Mario("mario.png");
        marioController = new MarioController(mario, camera);

        Gdx.input.setInputProcessor(marioController);
    }

    private void clear() {
        Gdx.gl.glClearColor(.2f, .6f, .8f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render () {
        marioController.update();

        clear();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mario.draw(batch);
        font.draw(batch, "Hello!", 10, 20);
        batch.end();
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
        mario.dispose();
        batch.dispose();
    }
}
