import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
    public static void main (String[] args) {
        LwjglApplication app = new LwjglApplication(
                new Game(), "mario-libgdx", 400, 400, false);
    }
}
