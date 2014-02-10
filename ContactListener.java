import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;

class MarioContactListener implements ContactListener {
    public void beginContact(Contact contact) {
        final Object[] objs = getUserData(contact);
        if(objs[0] == null || objs[1] == null) return;
        System.out.println(objs[0] + " hits "+ objs[1]);
    }
    public void endContact(Contact contact) {
        //System.out.println("end contact");
    }
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //System.out.println("post solve");
    }
    public void preSolve(Contact contact, Manifold oldManifold) {
        //System.out.println("pre solve");
    }
    private Object[] getUserData(Contact contact) {
        final Fixture fa = contact.getFixtureA();
        final Fixture fb = contact.getFixtureB();
        final Object a = fa.getBody().getUserData();
        final Object b = fb.getBody().getUserData();
        return new Object[] {a, b};
    }
}
