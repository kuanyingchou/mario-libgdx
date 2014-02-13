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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Mario extends Image {
    private Body body;
    private Sound jumpSound;
    private Sound hitSound;
    public int direction = 0;

    public Mario(String img) {
        super(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(img)))));
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
        jumpSound.play();
    }
    public void setBody(Body b) { body = b; }
    public Body getBody() { return body; }

    @Override public void setPosition(float x, float y) {
        super.setPosition(x, y);
        //sprite.setPosition(x, y); //>>> remove it
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

    public void smack() {
        //System.out.println(speed);
        hitSound.play();
    }

    public void dispose() {
        //texture.dispose();
        jumpSound.dispose();
        hitSound.dispose();
    }
}
