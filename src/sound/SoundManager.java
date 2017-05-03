package sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class SoundManager {

    public static boolean silent = true;

    public static void main(String[] args) throws Exception {

        SoundManager sm = new SoundManager();
        for (int i = 0; i < 25; i++) {
            sm.fire();
            Thread.sleep(20);
        }
        Clip[] clips = {sm.bangLarge, sm.bangMedium, sm.bangSmall,
                sm.beat1, sm.beat2, sm.extraShip, sm.saucerBig, sm.saucerSmall,
                sm.thrust,
        };

        for (Clip clip : clips) {
            sm.play(clip);
            Thread.sleep(500);
        }
    }

    // the sound manager class

    // this may need modifying
    static String path = "sounds/";
    Clip[] bullets = loadBullets(50);
    int nBullet = 0;
    Clip bangLarge = getClip("bangLarge");
    Clip bangMedium = getClip("bangMedium");
    Clip bangSmall = getClip("bangSmall");
    Clip beat1 = getClip("beat1");
    Clip beat2 = getClip("beat2");
    Clip extraShip = getClip("extraShip");
    Clip fire = getClip("fire");
    Clip saucerBig = getClip("saucerBig");
    Clip saucerSmall = getClip("saucerSmall");
    Clip thrust = getClip("thrust");

    public SoundManager() {
    }

    public void extraShip() {
        play(extraShip);
    }

    public void play(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }

    private Clip[] loadBullets(int n) {
        Clip[] clip = new Clip[n];
        for (int i = 0; i < n; i++) {
            clip[i] = getClip("fire");
        }
        return clip;
    }

    public void fire() {
        // fire the n-th bullet and increments the index
        if (!silent) {
            Clip clip = bullets[nBullet];
            clip.setFramePosition(0);
            clip.start();
            nBullet = (nBullet + 1) % bullets.length;
        }
    }

    public static Clip getClip(String filename) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample =
                    AudioSystem.getAudioInputStream(new File(path + filename + ".wav"));
            clip.open(sample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    boolean thrusting = false;

    public void startThrust() {
        // System.out.println("Starting thrust");
        if (!thrusting) {
            // thrust.setFramePosition(0);
            thrust.loop(-1);
            thrusting = true;
        }
    }

    public void stopThrust() {
        thrust.loop(0);
        thrusting = false;
        // get ready to loop again
        // thrust.setFramePosition(0);
    }
}
