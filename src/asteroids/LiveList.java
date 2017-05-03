package asteroids;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static asteroids.Constants.*;

/**
 * Created by Simon M. Lucas
 * sml@essex.ac.uk
 * Date: 26/12/11
 * Time: 17:04
 */

public class LiveList {
    List<GameObject> objects;
    int n = 0;

    public LiveList() {
        objects = new LinkedList<GameObject>();
    }

    public synchronized LiveList copy() {
        // interesting point here: would normally need to copy all of them,
        // but what about the ship?  The ship has already been copied!
        LiveList ll = new LiveList();
        
        for (GameObject ob : objects) {
            if (ob instanceof Missile)
                System.out.println("Found a missile in the main list copy!");
            if ( !(ob instanceof Ship) ){
                ll.objects.add(ob);
            }
        }

        return ll;
    }

    public synchronized int nAsteroids() {
        int n = 0;
        for (GameObject ob : objects) {
            if (ob instanceof Asteroid) n++;
        }
        return n;
    }

    public synchronized int nShips() {
        int n = 0;
        for (GameObject ob : objects) {
            if (ob instanceof Ship) n++;
        }
        return n;
    }

    public synchronized void update() {
        // update them
        // System.out.println("In update(): " + n);

        // use this iteration method to avoid ConcurrentModificationExceptions
        int nObjects = objects.size();

        for (int i=0; i<nObjects; i++) {
            GameObject ob = objects.get(i);
            ob.update();
            wrap(ob);
            checkCollision(ob);
        }

        // now purge any dead objects
        // add all live objects to the pending list
        List<GameObject> pending = new ArrayList<>();
        // System.out.println(nObjects + " ==? " + objects.size());
        for (GameObject ob : objects) {
            if (!ob.dead()) {
                pending.add(ob);
            }
        }
        objects = pending;
        // copy pending list back to objects
        n++;
        // System.out.println("nObjects = " + objects.size());
    }

    public synchronized void add(GameObject ob) {
        objects.add(ob);
    }

    public synchronized void moveAsteroids() {
        // update them
        // System.out.println("In update(): " + n);
        for (GameObject ob : objects) {
            if (ob instanceof Asteroid) {
                ob.update();
                wrap(ob);
            }
        }
    }

    public void checkCollision(GameObject actor) {
        // check with all other game objects
        // but use a hack to only consider interesting interactions
        // e.g. asteroids do not collide with themselves
        if (!actor.dead() &&
                (actor instanceof Missile
                        || actor instanceof Ship)) {
            if (actor instanceof Missile) {
                // System.out.println("Missile: " + actor);
            }
            for (GameObject ob : objects) {
                if (overlap(actor, ob)) {
                    // the object is hit, and the actor is also
                    ob.hit();
                    actor.hit();
                    return;
                }
            }
        }
    }

    private boolean overlap(GameObject actor, GameObject ob) {
        if (actor.equals(ob)) {
            return false;
        }
        if (ob instanceof PolyContains) {
            // System.out.println("Checking containment");
            return ((PolyContains) ob).contains(actor.s);
        }
        // otherwise do the default check
        double dist = actor.s.dist(ob.s);
        boolean ret = dist < (actor.r() + ob.r());
        return ret;
    }

    private void wrap(GameObject ob) {
        // only wrap objects which are wrappable
        if (ob.wrappable()) {
            ob.s.x = (ob.s.x + width) % width;
            ob.s.y = (ob.s.y + height) % height;
        }
    }

    public synchronized void draw(Graphics2D g) {
        // System.out.println("In draw(): " + n);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(bg);
        g.fillRect(0, 0, size.width, size.height);
        for (GameObject go : objects) {
            go.draw(g);
        }
    }

    public synchronized boolean isSafe(Ship ship) {
        for (GameObject ob : objects) {
            if ((ob instanceof Asteroid)
                    && ship.s.dist(ob.s) < ship.r() + ob.r() + safeRadius) return false;
        }
        return true;
    }

    public synchronized void clear() {
        objects.clear();
        // pending.clear();
    }
}
