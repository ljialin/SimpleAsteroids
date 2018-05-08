package sound;

import plot.VoronoiGrid;
import utilities.JEasyFrame;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    public static boolean silent = true;

    public static void main(String[] args) throws Exception {


        SoundManager sm = new SoundManager();
        silent = false;
        for (int i = 0; i < 10; i++) {
            sm.fire();
            Thread.sleep(500);
        }
        System.exit(0);
        Clip[] clips = {sm.bangLarge, sm.bangMedium, sm.bangSmall,
                sm.beat1, sm.beat2, sm.extraShip, sm.saucerBig, sm.saucerSmall,
                sm.thrust,
        };

        for (Clip clip : clips) {
            sm.playSafe(clip);
            Thread.sleep(500);
        }

        new JEasyFrame(new VoronoiGrid().setRandomPoints(30), "Test");

    }

    // the sound manager class

    // this may need modifying
    static String path = "sounds/";
    Clip[] bullets = loadBullets(50);
    int nBullet = 0;
    public Clip bangLarge = getClip("bangLarge");
    public Clip bangMedium = getClip("bangMedium");
    public Clip  bangSmall = getClip("bangSmall");
    Clip beat1 = getClip("beat1");
    Clip beat2 = getClip("beat2");
    Clip extraShip = getClip("extraShip");
    Clip fire = getClip("fire");
    public Clip saucerBig = getClip("saucerBig");
    public Clip saucerSmall = getClip("saucerSmall");
    Clip thrust = getClip("thrust");

    public SoundManager() {
    }

    public void extraShip() {
        play(extraShip);
    }

    public void play(Clip clip) {
        // clip.setFramePosition(0);
        clip.start();

    }

    public void playSafe(Clip clip) {
        if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
        clip.setFramePosition(0); // rewind to the beginning
        clip.start();     // Start playing

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
            if (clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
            clip.start();
            nBullet = (nBullet + 1) % bullets.length;
        }
    }


    static boolean noClick = true;
    public static Clip getClip(String filename) {
        if (noClick) return getClipNoClick(filename);
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream sample =
                    AudioSystem.getAudioInputStream(new File(path + filename + ".wav"));
            System.out.println("Frame length = " + sample.getFrameLength());
            System.out.println("Format = " + sample.getFormat());

            System.out.println("Available: " + sample.available());



            // byte[] bytes = sample.readAllBytes();
            // sample.
            clip.open(sample);
            System.out.println("Available after reading clip: " + sample.available());
            System.out.println();

            // clip.
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }
    public static Clip getClipNoClick(String filename) {
        Clip clip = null;
        try {
            AudioInputStream sample =
                    AudioSystem.getAudioInputStream(new File(path + filename + ".wav"));
            System.out.println(sample.getFormat());
            int len = sample.available();
            if (len % 2 != 0) len--;
            byte[] bytes = new byte[len];
            sample.read(bytes, 0, len);
            // byte[] bytes = sample.readAllBytes();
            // sample.
            // clip.open(sample);
            System.out.println("Available after reading clip: " + sample.available());
            System.out.println();

            clip= byteClip(bytes, 2);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    private static int SAMPLE_RATE = 11025;
    private static int FRAME_SIZE = 2;
    private static final AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
            (float) SAMPLE_RATE, 8, 1, FRAME_SIZE, SAMPLE_RATE, false);


    public static Clip byteClip(byte[] out, int frameSize) throws LineUnavailableException {
        AudioFormat FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED,
                (float) SAMPLE_RATE, 8, 1, frameSize, SAMPLE_RATE, false);
        Clip clip = AudioSystem.getClip();
        clip.open(FORMAT, out, 0, out.length);
        clip.setFramePosition(0);
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
