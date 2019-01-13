package sound;

import plot.VoronoiGrid;
import utilities.JEasyFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Test16BitWav {

    static String path = "sounds/";


    public static void main(String[] args) throws Exception {


        Clip clip = getClip("Pickup_Coin4");
        for (int i = 0; i < 10; i++) {
            playSafe(clip);


            System.out.println(i);
            Thread.sleep(2000);
        }
    }

    public static void playSafe(Clip clip) {
        if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
        clip.setFramePosition(0); // rewind to the beginning
        clip.start();     // Start playing

    }


    public static Clip getClip(String filename) {
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


}
