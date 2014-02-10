import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Sound;

class Mario extends Actor {
    private Sprite sprite;
    private Texture texture;
    private float speed = 400;
    private MarioController marioController;
    private Body body;
    private Audio audio;
    private Sound jumpSound;
    private Sound hitSound;

    public Mario(String img) {
        texture = new Texture(Gdx.files.internal(img));
        texture.setFilter(
                TextureFilter.Linear, 
                TextureFilter.Linear);
        sprite = new Sprite(texture, 0, 0, 256, 256);
        sprite.setPosition(0, 0);
        sprite.setSize(1, 1);
        //setBounds(0, 0, 256, 256);

        marioController = new MarioController(this);
        addListener(marioController);
        //addCaptureListener(marioController);
        audio = Gdx.audio;
        jumpSound = audio.newSound(Gdx.files.internal("jump.mp3"));
        hitSound = audio.newSound(Gdx.files.internal("hit.mp3"));
    }    
    public void moveTo(float x) {
        sprite.setX(x);
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
        sprite.setPosition(x, y); //>>> remove it
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override 
    public void act(float deltaTime) {
        marioController.update();
    }

    public void hit() {
        //System.out.println(speed);
        hitSound.play();
    }

    public void dispose() {
        texture.dispose();
        jumpSound.dispose();
        hitSound.dispose();
    }
}
