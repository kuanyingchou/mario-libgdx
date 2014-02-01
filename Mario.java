import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Mario extends Actor {
    private Sprite sprite;
    private Texture texture;
    private float speed = 400;
    private MarioController marioController;

    public Mario(String img) {
        texture = new Texture(Gdx.files.internal(img));
        sprite = new Sprite(texture, 0, 0, 256, 256);
        sprite.setPosition(0, 0);
        setBounds(0, 0, 256, 256);

        marioController = new MarioController(this);
        addListener(marioController);
        //addCaptureListener(marioController);
    }    
    public void moveTo(float x) {
        sprite.setX(x);
    }
    public void moveLeft() {
        sprite.setX(sprite.getX() - speed * Gdx.graphics.getDeltaTime());
    }
    public void moveRight() {
        sprite.setX(sprite.getX() + speed * Gdx.graphics.getDeltaTime());
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override 
    public void act(float deltaTime) {
        marioController.update();
    }

    public void dispose() {
        texture.dispose();
    }
}
