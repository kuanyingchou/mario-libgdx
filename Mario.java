import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Mario {
    private Sprite sprite;
    private float speed = 400;
    private Texture texture;

    public Mario(String img) {
        texture = new Texture(Gdx.files.internal(img));
        sprite = new Sprite(texture, 0, 0, 256, 256);
        sprite.setPosition(0, 0);
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

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
    public void dispose() {
        texture.dispose();
    }
}
