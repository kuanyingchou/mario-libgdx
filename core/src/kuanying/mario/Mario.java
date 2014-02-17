package kuanying.mario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Bone;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

class Mario extends Actor {
    private Body body;
    private Sound jumpSound;
    private Sound hitSound;
    public int direction = 0;

    private Animation animation;
    private Skeleton skeleton;
    private SkeletonData skeletonData;
    private SkeletonRenderer renderer;
    private Array<Event> events = new Array();
    private float time;
    private Bone root;

    public Mario(String img) {
        //super(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(img)))));
        setSize(1, 1); //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> this line took me all day while implementing gesture input
        setOrigin(.5f, .5f);
        //setBounds(0, 0, 256, 256);

        addListener(new MarioController(this));
        //addListener(new MarioActorGestureListener(this));

        //setTouchable(Touchable.enabled);
        //setVisible(true);
        //addCaptureListener(marioController);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));

        initSpine();
    }    

    
    private void initSpine() {
        final FileHandle atlasFile = Gdx.files.internal("spineboy.atlas");
        final TextureAtlasData data = !atlasFile.exists() ? null : new TextureAtlasData(atlasFile, atlasFile.parent(), false);
        final TextureAtlas atlas = new TextureAtlas(data);
        final SkeletonJson json = new SkeletonJson(atlas);
        json.setScale(.005f);
        skeletonData = json.readSkeletonData(Gdx.files.internal("spineboy.json"));
        animation = skeletonData.findAnimation("walk");

        skeleton = new Skeleton(skeletonData);
        skeleton = new Skeleton(skeleton);
        skeleton.updateWorldTransform();
        
        renderer = new SkeletonRenderer();
        root = skeleton.findBone("root");
    }

    static class MarioActorGestureListener extends ActorGestureListener {
        private final Mario mario;

        public MarioActorGestureListener(Mario m) {
            mario = m;
        } 

        @Override
        public void tap(InputEvent event, float x, float y, int pointer, int button) {
            System.out.println("tap: "+event);
        }

        @Override
        public void fling(InputEvent event, float dx, float dy, int button) {
            System.out.println("fling: "+dx + ", " + dy);
            if(dy > .1f) {
                mario.jump();
            }
            if(dx > .1f) {
                mario.direction = 1;
            } 
            if(dx < .1f) {
                mario.direction = -1;
            }
        }

        @Override
        public boolean longPress (Actor actor, float x, float y) {
            System.out.println("long press: "+x +", "+y);
            return false;
        }
    }
    /*
    @Override
    public boolean fire(Event e) {
        System.out.println("fire: "+e);
        return super.fire(e);
    }
    */
    public void moveTo(float x) {
        setX(x);
    }
    public void moveLeft() {
        //sprite.setX(sprite.getX() - speed * Gdx.graphics.getDeltaTime());
        //body.applyForceToCenter(-10, 0, true);
        final Vector2 vel = body.getLinearVelocity();
        final Vector2 pos = body.getPosition();
        if(vel.x > -2) body.applyLinearImpulse(-.5f, 0, pos.x, pos.y, true);
    }
    public void moveRight() {
        //sprite.setX(sprite.getX() + speed * Gdx.graphics.getDeltaTime());
        //body.applyForceToCenter(10, 0, true);
        final Vector2 vel = body.getLinearVelocity();
        final Vector2 pos = body.getPosition();
        if(vel.x < 2 ) body.applyLinearImpulse(.5f, 0, pos.x, pos.y, true);
    }
    public void jump() {
        final Vector2 pos = body.getPosition();
        //if(pos.y < .6f) {
            body.applyLinearImpulse(0, 5f, pos.x, pos.y, true);
        //}
        jumpSound.play(.2f);
    }
    public void setBody(Body b) { body = b; }
    public Body getBody() { return body; }

    @Override public void setPosition(float x, float y) {
        super.setPosition(x, y);
        //sprite.setPosition(x, y); //>>> remove it
        skeleton.setX(x+.5f);
        skeleton.setY(y+.5f);
    }
    @Override public void setRotation(float deg) {
        super.setRotation(deg);
        //skeleton.setRotation(deg);
        root.getData().setRotation(deg);
        //skeleton.updateWorldTransform();
    }

/*
    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }
*/
    @Override 
    public void act(float deltaTime) {
        super.act(deltaTime);
        if(direction > 0) moveRight();
        else if(direction < 0) moveLeft();
    }

/*
    @Override 
    public Actor hit(float a, float b, boolean c) {
        Actor actor = super.hit(a, b, c);
        System.out.println(a+", "+b + " over "+getWidth()+", "+getHeight());
        if(actor != null) System.out.println("hit "+actor);
        return actor;

    }
 */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        float lastTime = time;
        time += Gdx.graphics.getDeltaTime();

/*
        float x = skeleton.getX() + 160 * Gdx.graphics.getDeltaTime() * (skeleton.getFlipX() ? -1 : 1);
        if (x > Gdx.graphics.getWidth()) skeleton.setFlipX(true);
        if (x < 0) skeleton.setFlipX(false);
        skeleton.setX(x);
        skeleton.setX(300);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
*/

        events.clear();
        animation.apply(skeleton, lastTime, time, true, events);
        if (events.size > 0) System.out.println(events);

        skeleton.updateWorldTransform();
        skeleton.update(Gdx.graphics.getDeltaTime());

        //batch.begin();
        renderer.draw(batch, skeleton);
        //batch.end();
    }

    public void smack() {
        //System.out.println(speed);
        hitSound.play(.2f);
    }

    public void dispose() {
        //texture.dispose();
        jumpSound.dispose();
        hitSound.dispose();
    }
}
