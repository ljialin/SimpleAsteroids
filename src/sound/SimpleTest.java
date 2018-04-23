package sound;

import plot.VoronoiGrid;
import utilities.JEasyFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SimpleTest {
    static String path = "sounds/";

    public static void main(String[] args) throws Exception {
        Clip fire = getClip("fire");

        SoundManager soundManager = new SoundManager();
        for (int i=0; i<10; i++) {
            soundManager.playSafe(soundManager.fire);
            Thread.sleep(200);
        }

        new JEasyFrame(new VoronoiGrid().setRandomPoints(10), "Test");

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


}
