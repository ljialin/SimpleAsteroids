package sound;

import plot.VoronoiGrid;
import utilities.JEasyFrame;

import javax.sound.sampled.Clip;

public class Test16BitWav {


    public static void main(String[] args) throws Exception {


        SoundManager sm = new SoundManager();
        // System.exit(0);
        Clip clip = SoundManager.getClip("bangLarge16b");
        for (int i = 0; i < 10; i++) {
            sm.playSafe(clip);
            System.out.println(i);
            Thread.sleep(1000);
        }
    }
}
