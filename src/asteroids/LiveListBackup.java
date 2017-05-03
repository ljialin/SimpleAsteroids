package asteroids;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static asteroids.Constants.*;

/**
 * Created by Simon M. Lucas
 * sml@essex.ac.uk
 * Date: 26/12/11
 * Time: 17:04
 *
 *  This Backup version works for normal operation, but there
 *  is a problem when using the copy method (missiles added
 *  to the pending list can be lost before being added to
 *  the main objects list)
 */

public class LiveListBackup {
    List<GameObject> objects;
    List<GameObject> pending;
    int n = 0;

    public LiveListBackup() {
        objects = new LinkedList<GameObject>();
        pending = new LinkedList<GameObject>();
    }

    public synchronized LiveListBackup copy() {
        // interesting point here: would normally need to copy all of them,
        // but what about the ship?  The ship has already been copied!
        LiveListBackup ll = new LiveListBackup();
        
        for (GameObject ob : objects) {
            if ( !(ob instanceof Ship) ){
                ll.objects.add(ob);
            }
        }
        for (GameObject ob : pending) {
            if ( !(ob instanceof Ship) ){
                ll.pending.add(ob);
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
        for (GameObject ob : objects) {
            ob.update();
            wrap(ob);
            checkCollision(ob);
        }
        // add all live objects to the pending list
        for (GameObject ob : objects) {
            if (!ob.dead()) {
                pending.add(ob);
            }
        }
        objects.clear();
        // copy pending list back to objects
        for (GameObject ob : pending) {
            objects.add(ob);
        }
        pending.clear();
        n++;
        // System.out.println("nObjects = " + objects.size());
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

    public synchronized void add(GameObject ob) {
        pending.add(ob);
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
        pending.clear();
    }
}
