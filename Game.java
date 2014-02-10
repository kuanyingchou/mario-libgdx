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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.math.MathUtils;

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

        createPlayer();
        createPlatform();
        for(int i=0; i<50; i++) {
            final Body b = createBoxes();
            b.setUserData("box "+i);
        }

        world.setContactListener(new MarioContactListener());

        Gdx.input.setInputProcessor(stage);
    }

    private void createPlayer() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(1, 2);
        bodyDef.fixedRotation = true;
        marioBody = world.createBody(bodyDef);
        marioBody.setUserData(mario); 
        //] the link between scene2d and box2d
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = .2f;

        final PolygonShape radarShape = new PolygonShape();
        radarShape.setAsBox(1f, 1f);
        final FixtureDef radarDef = new FixtureDef();
        radarDef.shape = radarShape;
        radarDef.isSensor = true;

        marioBody.createFixture(fixtureDef); 
        marioBody.createFixture(radarDef); 
        shape.dispose();
        radarShape.dispose();
        mario.setBody(marioBody);
    }
    private Body createBoxes() {
        float x = MathUtils.random(5);
        float y = 10;


        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = MathUtils.random(1.0f) * MathUtils.PI;
        //bodyDef.fixedRotation = true;
        Body box = world.createBody(bodyDef);

        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = .1f;
        fixtureDef.friction = 3f;
        fixtureDef.restitution = .1f;

        box.createFixture(fixtureDef); 
        shape.dispose();

        return box;
    }

    private void createPlatform() {
        final BodyDef platformDef = new BodyDef();
        platformDef.type = BodyType.StaticBody;
        platformDef.position.set(0, -.1f);
        final Body platformBody = world.createBody(platformDef);
        final PolygonShape platformShape = new PolygonShape();
        platformShape.setAsBox(10, .1f);
        platformBody.createFixture(platformShape, 1);
        platformShape.dispose();
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
        //System.out.println(">>> "+marioBody.getPosition());
    }
    

    @Override
    public void resize (int width, int height) {
        //stage.setViewport(width / 256f, height / 256f);
        //stage.setViewport(width, height);
        stage.setViewport(10, 10);
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
