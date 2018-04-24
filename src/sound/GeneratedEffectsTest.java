package sound;

// import com.sun.media.sound.AudioSynthesizer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GeneratedEffectsTest {
    /**
     * Use SourceDataLine to read line-by-line from the external sound file.
     * For computer game, use Clip to pre-load short-duration sound files.
     */
    public static void main(String[] args) throws Exception {
        SourceDataLine soundLine = null;
        int BUFFER_SIZE = 64 * 1024;  // 64 KB
        for (int i=0; i<1000; i++ ) {
            // System.out.println(i + "\t " + sinByte(i));
        }

        try {
            // Set up an audio input stream piped from the sound file.
            File soundFile = new File("sounds/fire.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioInputStream.getFormat();
            AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT,
                    11025.0f, 16, 1, 1024, 10, true);
            System.out.println(audioFormat);
            System.out.println(af);
            // audioFormat = af;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            // AudioSynthesizer synthesizer;
            // DataLine.Info
            soundLine = (SourceDataLine) AudioSystem.getLine(info);

            System.out.println(soundLine);

            soundLine.open(audioFormat);
            soundLine.start();

            int len = BUFFER_SIZE;
            byte[] sampledData = new byte[BUFFER_SIZE];



            Random random = new Random();
            for (int i = 0; i < len; i++) {

                sampledData[i] = sinByte(i); // ()byte) random.nextInt(256);

            }

            System.out.println("Writing");
            soundLine.write(sampledData, 0, sampledData.length/10);
            soundLine.drain();

            System.out.println("Sleeping");
            Thread.sleep(2000);

            System.out.println("Writing");
            soundLine.write(sampledData, 0, sampledData.length);
            System.out.println("Sleeping");
            Thread.sleep(2000);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            soundLine.drain();
            soundLine.close();
        }
    }

    static double sampleRate = 11000;

    public static byte sinByte(int i) {
        double freq = 200;

        double x = 127 * Math.sin (freqShift2(i) * freq * Math.PI * 2 / sampleRate);
        return (byte) x;

    }

    public static double freqShift(double x) {
        double freq = 10;
        double v = x * (0.9 + 0.1 * Math.sin(x * freq * Math.PI * 2 / sampleRate));
        return v;
    }

    public static double freqShift2(double x) {
        double freq = 10;
        double v = x + (10 *  Math.sin(x * freq * Math.PI * 2 / sampleRate));
        return v;
    }

}
