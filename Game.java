import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Game implements ApplicationListener {
    private Mario mario;
    private Stage stage;

    @Override
    public void create () {
        System.out.println("game created");
        mario = new Mario("mario.png");

        stage = new Stage();
        stage.addActor(mario);
        stage.setKeyboardFocus(mario);

        Gdx.input.setInputProcessor(stage);
    }

    private void clear() {
        Gdx.gl.glClearColor(.2f, .6f, .8f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render () {
        clear();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    

    @Override
    public void resize (int width, int height) {
        stage.setViewport(width, height);
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
        stage.dispose();
    }
}
