package kuanying.mario;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Game implements ApplicationListener {
    private Mario mario;
    private Stage stage;

    private Body marioBody;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create () {
        System.out.println("game created");
        init();
    }

    private void init() {
        mario = new Mario("mario.png");
        mario.setZIndex(0);

        stage = new Stage();
        uiStage = new Stage();
        
        stage.addActor(mario);
        stage.setKeyboardFocus(mario);
        Gdx.input.setInputProcessor(new InputMultiplexer(
            stage,
            uiStage,
            new GestureDetector(new GestureAdapter() {
                @Override
                public boolean fling(float dx, float dy, int button) {
                    System.out.println("fling: "+dx + ", " + dy);
                    if(dy < -1000f) {
                        mario.jump();
                    }
                    if(dx > 1000f) {
                        mario.direction = 1;
                    } 
                    if(dx < 1000f) {
                        mario.direction = -1;
                    }
                    return true;
                }
            
        })));

        createPhysicsWorld();
        createUI();
/*
        Actor a = new Actor();
        a.addListener(new InputListener() {
            public boolean handle(Event e) {
                super.handle(e);
                return false;
            }
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>");
                return false;
            }
        });
        a.setBounds(0, 0, 10, 10);
        stage.addActor(a);
*/
        setViewport(512, 512);
    }

    private Stage uiStage;
    private Table table;

    private void createUI() {

        final Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a table that fills the screen. Everything else will go inside this table.
        //
        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button = new TextButton("Reload", skin);
        //button.setZIndex(2);

        final HorizontalGroup g = new HorizontalGroup();
        g.setAlignment(Align.left);
        g.addActor(button);

        Table table = new Table();
        table.setZIndex(1);
        table.align(Align.top);
        table.setFillParent(true);
        table.add(g);

        uiStage.addActor(table);


        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                destroy();
                init();
            }
        });
    }

    private void createPhysicsWorld() {
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        createPlayer();
        createPlatform();
        createBoxes();

        world.setContactListener(new MarioContactListener());
    }

    private void createBoxes() {
        for(int i=0; i<50; i++) {
            final Body b = createBox();
            b.setUserData("box "+i);
        }
    }

    private void createPlayer() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(1, 2);
        //bodyDef.fixedRotation = true;
        marioBody = world.createBody(bodyDef);
        marioBody.setUserData(mario); 
        //] the link between scene2d and box2d
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(.5f, .5f);

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = .2f;
        fixtureDef.restitution = .5f;

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
    private Body createBox() {
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
        uiStage.draw();

        table.drawDebug(uiStage);

        debugRenderer.render(world, stage.getCamera().combined);
        world.step(1/60f, 6, 2);
        mario.setPosition(
                marioBody.getPosition().x - .5f, 
                marioBody.getPosition().y - .5f);
        mario.setRotation(marioBody.getTransform().getRotation() * MathUtils.radDeg);
        //System.out.println(">>> "+mario.getRotation());

    }
    

    @Override
    public void resize (int width, int height) {
        setViewport(width, height);
    }

    private void setViewport(int sw, int sh) {
        //stage.setViewport(sw / 256f, sh / 256f);
        //stage.setViewport(sw, sh);
        stage.setViewport(sw / 100.0f, sh / 100.0f);
        uiStage.setViewport(512, 512);
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
        destroy();
    }

    private void destroy() {
        mario.dispose();
        stage.dispose();
        uiStage.dispose();
        world.dispose();
    }
}
