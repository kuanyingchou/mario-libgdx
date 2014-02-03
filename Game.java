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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Game implements ApplicationListener {
    private Mario mario;
    private Stage stage;

    private Body marioBody;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create () {
        System.out.println("game created");
        mario = new Mario("mario.png");

        stage = new Stage();
        stage.addActor(mario);
        stage.setKeyboardFocus(mario);

        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(1, 2);
        marioBody = world.createBody(bodyDef);
        marioBody.setUserData(mario); 
        //] the link between scene2d and box2d
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);
        marioBody.createFixture(shape, 1); 
        shape.dispose();

        final BodyDef platformDef = new BodyDef();
        platformDef.type = BodyType.StaticBody;
        platformDef.position.set(0, 0);
        final Body platformBody = world.createBody(platformDef);
        final PolygonShape platformShape = new PolygonShape();
        platformShape.setAsBox(5, .1f);
        platformBody.createFixture(platformShape, 1);
        platformShape.dispose();

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

        debugRenderer.render(world, stage.getCamera().combined);
        world.step(1/60f, 6, 2);
        mario.setPosition(
                marioBody.getPosition().x - .5f, 
                marioBody.getPosition().y - .5f);
        System.out.println(">>> "+marioBody.getPosition());
    }
    

    @Override
    public void resize (int width, int height) {
        //stage.setViewport(width / 256f, height / 256f);
        //stage.setViewport(width, height);
        stage.setViewport(2, 2);
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
